package universetournament.server.core;

import universetournament.server.communication.PJUTConnManager;
import universetournament.server.communication.PJUTConnectionListener;
import universetournament.server.communication.PJUTServerSocket;
import universetournament.server.communication.events.PJUTReceiverManager;
import universetournament.server.communication.events.PJUTServerReceiver;
import universetournament.server.config.ServerConfig;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.server.logic.gamemode.UTGameModeController;
import universetournament.shared.core.GameSubType;
import universetournament.shared.core.InitContainer;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventBus;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.util.EntityCollection;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.logic.gamemode.events.GameModeSubType;

/**
 * Hauptcontroller des Servers, welcher die Authentifizierung und die
 * initiale Datenübermittlung für Clients handhabt.
 * 
 * @author Florian Holzschuher
 *
 */
public class ServerMainController implements PJUTEventHandler,
	PJUTConnectionListener
{
	private static final ServerMainController instance =
		new ServerMainController();
	
	private EntityCollection entities;
	
	private UTGameModeController gameModeController;
	
	/**
	 * @return Instanz der Klasse.
	 */
	public static synchronized ServerMainController getInstance()
	{
		return instance;
	}
	
	private ServerMainController()
	{
		entities = new EntityCollection();
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getSub() != null
				&& event.getSub() instanceof GameSubType)
		{
			GameSubType type = (GameSubType) event.getSub();
			
			switch(type)
			{
				case PASSWORD:
					checkPassword(event.getData());
					break;
					
				case REQUEST_GAME:
					sendStatus(event.getData());
					break;
			}
		}
	}
	
	private void checkPassword(Object o)
	{
		if(o instanceof InitContainer)
		{
			InitContainer container = (InitContainer) o;
			
			if(container.getData() instanceof String
					&& ((String)container.getData()).equals(
							ServerConfig.getInstance().getPassword()))
			{
				//Passwort stimmt -> Bestätigung
				GameEvent event = new GameEvent(MainType.TYPE_GAME,
					GameSubType.PWD_CONFIRM, null);
				PJUTConnManager.getInstance().sendToID(event,
						container.getClientId());
			}
			else
			{
				//Passwort falsch -> Ablehnung
				GameEvent event = new GameEvent(MainType.TYPE_GAME,
					GameSubType.PWD_REJECT, null);
				PJUTConnManager.getInstance().sendToID(event,
					container.getClientId());
			}
			
		}
	}
	
	private void sendStatus(Object o)
	{
		if(o instanceof Byte)
		{
			byte id = (Byte)o;
			PJUTEventBuffManager buffer = PJUTEventBuffManager.getInstance();
			
			//Spielmodusdaten
			EntityContainer container = new EntityContainer(
					EntityType.GAME_MODE, entities.getGameData());
			EntityEvent ee = new EntityEvent((short)0,
					Entity.SubEvents.CREATE, container);
			buffer.sendToId(id, ee);
			
			//Spieler
			for(PJUTPlayer pl : entities.getPlayers())
			{
				container = new EntityContainer(
						EntityType.PLAYER, pl);
				ee = new EntityEvent((short)0,
						Entity.SubEvents.CREATE, container);
				buffer.sendToId(id, ee);
			}
			
			//Schiffe
			for(ShipEntity se : entities.getShips())
			{
				container = new EntityContainer(
						EntityType.SHIP, se);
				ee = new EntityEvent((short)0,
						Entity.SubEvents.CREATE, container);
				buffer.sendToId(id, ee);
			}
			
			//Dekoration
			for(WorldEntity<PhysicContainer> we :
				entities.getWorldEntities())
			{
				container = new EntityContainer(
						EntityType.PHYSICAL_DEC, we);
				ee = new EntityEvent((short)0,
						Entity.SubEvents.CREATE, container);
				buffer.sendToId(id, ee);
			}
			
			//Schüsse und Raketen nicht, weil zu kurzlebig
			
			//Spiel starten lassen
			GameEvent event = new GameEvent(MainType.TYPE_GAME,
					GameSubType.START_GAME, null);
			buffer.sendToId(id, event);
		}
	}

	@Override
	public void connectionAdded(PJUTServerSocket sock)
	{
		
        //schauen ob zu viele Spieler im Spiel
        if(entities.getPlayers().size()
        		< ServerConfig.getInstance().getPlayerLimit())
        {
        	//Server nicht voll
        	
    		//Receiver und Puffer erzeugen
    		PJUTEventBuffManager.getInstance().createBuffer(sock.getId());
            PJUTReceiverManager.getInstance().createReceiver(sock);
            
            //Handler einhängen
            PJUTServerReceiver receiver = PJUTReceiverManager.getInstance().
            	getReceiver(sock.getId());
            receiver.setHandler(MainType.TYPE_GAME, this);
            receiver.setHandler(MainType.TYPE_ENTITY,
            		UTEntityManager.getInstance());
            receiver.setHandler(MainType.TYPE_GAMEMODE, gameModeController);
            
            //Receiver starten
            receiver.start();
    		
    		//Spieler hinzugekommen -> Authorisierung anfragen
    		GameEvent event = new GameEvent(MainType.TYPE_GAME,
    				GameSubType.AUTH_REQUEST, sock.getId());
    		sock.send(event);
        }
        else
        {
        	//Spieler wieder rauswerfen
        	GameEvent event = new GameEvent(MainType.TYPE_GAME,
    				GameSubType.KICK, null);
    		sock.send(event);
        }
	}

	@Override
	public void connectionClosed(byte id)
	{
		//Spieler disconnected -> aus System entfernen
		PJUTEventBuffManager.getInstance().removeBuffer(id);
		PJUTReceiverManager.getInstance().removeReceiver(id);
		
		//per Event aus Spielmodus entfernen
		GameEvent ge = new GameEvent(MainType.TYPE_GAMEMODE,
				GameModeSubType.PLAYER_LEFT, id);
		gameModeController.handleEvent(ge);
		
		//Spieler und Schiff entfernen
		PJUTPlayer player = entities.getPlayer(id);
		ShipEntity ship = entities.getShip(id);
		
		EntityEvent ee = new EntityEvent(player.getId(),
				Entity.SubEvents.DELETE, new EntityContainer(
				EntityType.PLAYER, null));
		PJUTEventBus.getInstance().handleEvent(ee);
		
		if(ship != null)
		{
			ee = new EntityEvent(ship.getId(), Entity.SubEvents.DELETE,
					new EntityContainer(EntityType.SHIP, null));
			PJUTEventBus.getInstance().handleEvent(ee);
		}
	}
	
	/**
	 * @param controller neuer Controller, der Spielmodus handhabt.
	 */
	public void setGameModeController(UTGameModeController controller)
	{
		gameModeController = controller;
	}
	
	/**
	 * @return aktueller Spielmodus-Controller
	 */
	public UTGameModeController getGameModeController()
	{
		return gameModeController;
	}

	/**
	 * @return Kollektion aller intern gehaltenen Entities
	 */
	public EntityCollection getEntities()
	{
		return entities;
	}
}
