package universetournament.client.core;

import javax.swing.JOptionPane;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.client.config.ClientConfig;
import universetournament.client.config.KeyBindings;
import universetournament.client.config.KeyBindings.CommandType;
import universetournament.client.input.PJUTInputDistributor;
import universetournament.client.input.commands.BlasterCommand;
import universetournament.client.input.commands.MovementCommand;
import universetournament.client.input.commands.MovementCommand.MOVE_DIRECTION;
import universetournament.client.input.commands.RocketCommand;
import universetournament.client.input.commands.RotationCommand;
import universetournament.client.logic.entities.controller.PlayerMovement;
import universetournament.client.logic.gamemode.GameModeController;
import universetournament.shared.core.GameSubType;
import universetournament.shared.core.InitContainer;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.requests.PlayerRequest;
import universetournament.shared.logic.entities.requests.ShipRequest;
import universetournament.shared.logic.entities.util.EntityCollection;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * Hauptcontroller des Clients, welcher die Authentifizierung am Server
 * übernimmt und Spieler-Entity, Spielerschiff und Spieldaten anfragt, sowie
 * letztendlich Engine und Spiellogik startet.
 * 
 * @author Florian Holzschuher
 *
 */
public class ClientMainController implements PJUTEventHandler
{
	private static final ClientMainController instance =
		new ClientMainController();
	
	boolean auth = false, reqPlayer = false, started = false,
		requested = false;
	private byte clientId;
	
	//Fenster-Objekt
	private ClientWindow window;
	
	private final EntityCollection entities;
	private ShipEntity playerShip;
	private PJUTPlayer player;
        private GameModeController gmcontroller;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized ClientMainController getInstance()
	{
		return instance;
	}
	
	private ClientMainController()
	{
		clientId = -1;
		
		window = null;
		
		entities = new EntityCollection();
		
		playerShip = null;
		player = null;
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
				//Server fragt Authentifikation an
				case AUTH_REQUEST:
					if(!auth)
					{
						sendAuth(event.getData());
						auth = true;
					}
					break;
					
				//Passwort wurde akzeptiert - Spieler erstellen lassen
				case PWD_CONFIRM:
					if(!reqPlayer)
					{
						createPlayer();
						reqPlayer = true;
					}
					break;
					
				//Passwort falsch - Fehler anzeigen
				case PWD_REJECT:
					passwordRejected();
					break;
					
				//alles bereit - Schiff erstellen lassen, Engine, Logik starten
				case START_GAME:
					if(!started)
					{
						started = true;
						createShip();
						startGame();
					}
					break;
					
				case KICK:
					kicked();
					break;
			}
		}
	}
	
	private void sendAuth(Object o)
	{
		if(o instanceof Byte)
		{
			clientId = (Byte)o;
			
			//Passwort schicken
			GameEvent event = new GameEvent(MainType.TYPE_GAME,
					GameSubType.PASSWORD, new InitContainer(clientId,
							ClientConfig.getInstance().getPassword()));
			
			PJUTCliConnector.getInstance().getSocket().send(event);
		}
		else
		{
			System.err.println("keine brauchbare ID erhalten" +
					" - Authentifizierung fehlgeschlagen");
		}
	}
	
	private void createPlayer()
	{
		ClientConfig conf = ClientConfig.getInstance();
		
		PlayerRequest request = new PlayerRequest(conf.getPlayerName(),
				conf.getPlayerColor(), clientId);
		EntityContainer container = new EntityContainer(
				EntityType.PLAYER, request);
		EntityEvent event = new EntityEvent((short)0, Entity.SubEvents.CREATE,
				container);
		
		PJUTCliConnector.getInstance().getSocket().send(event);
	}

	private void passwordRejected()
	{
		JOptionPane.showMessageDialog(null,
				"falsches Passwort - Programm wird beendet");
		System.exit(0);
	}
	
	private void kicked()
	{
		JOptionPane.showMessageDialog(null,
				"Sie wurden gekickt oder der Server ist voll");
		System.exit(0);
	}
	
	private void createShip()
	{
		ShipRequest request = new ShipRequest(
				ClientConfig.getInstance().getShipConfig(), clientId);
		EntityContainer container = new EntityContainer(
				EntityType.SHIP, request);
		EntityEvent event = new EntityEvent((short)0, Entity.SubEvents.CREATE,
				container);
		
		PJUTCliConnector.getInstance().getSocket().send(event);
	}
	
	private void startGame()
	{
		ClientConfig conf = ClientConfig.getInstance();
		
		//Fullscreen-Feature nicht in Engine integriert
		window = new ClientWindow(conf.getxResolution(), conf.getyResolution(),
				false);
	}
	
	private void requestData()
	{
		GameEvent event = new GameEvent(MainType.TYPE_GAME,
			GameSubType.REQUEST_GAME, clientId);
		PJUTCliConnector.getInstance().getSocket().send(event);
	}
	
	/**
	 * @return erhaltene Spieler-ID
	 */
	public byte getClientId()
	{
		return clientId;
	}
	
	/**
	 * Setzt das intern gehaltene, eigene Spielerobjekt.
	 * Ist dieses != null werden Spieldaten angefordert.
	 * Null wird ignoriert.
	 * 
	 * @param player eigene Spieler-Entity
	 */
	public void setPlayer(PJUTPlayer player)
	{
		if(player != null)
		{
			if(!requested)
			{
				requested = true;
				this.player = player;
				//Spieler bereit, Daten anfordern
				requestData();
			}
		}
	}
	
	/**
	 * @return eigener Spieler oder null
	 */
	public PJUTPlayer getPlayer()
	{
		return player;
	}

	/**
	 * Setzt das Spielerschiff, die Verfolgerkamera und bindet die
	 * entsprechenden Kommandos an den Input.
	 * Wird null übergeben wird die Kamera auf statisch gesetzt und die
	 * Schiffskommandos werden deregistriert.
	 * 
	 * @param playerShip Spielerschiff
	 */
	public void setPlayerShip(ShipEntity playerShip, PlayerMovement pm)
	{
		this.playerShip = playerShip;
		
		if(playerShip != null)
		{
			window.setFollowCamera(playerShip);
			
			bindShipCommands(playerShip, pm);
		}
		else
		{
			window.setFirstPersonCam(0.f, -1.f, -5.f);
		}
	}
	
	private void bindShipCommands(ShipEntity ship, PlayerMovement pm)
	{
		KeyBindings kb = ClientConfig.getInstance().getBindings();
		PJUTInputDistributor dist = PJUTInputDistributor.getInstance();
		
		//vorwärts
		MovementCommand mc = new MovementCommand(playerShip, pm,
				MOVE_DIRECTION.MAIN_THRUST);
		int keyCode = kb.getKeyBinding(CommandType.ACCEL_FORWARD);
		dist.bindToKeyPress(keyCode, mc);
		dist.bindToKeyHold(keyCode, mc);
		
		//rückwärts
		mc = new MovementCommand(playerShip, pm,
				MOVE_DIRECTION.BACK_THRUST);
		keyCode = kb.getKeyBinding(CommandType.ACCEL_BACKWARD);
		dist.bindToKeyPress(keyCode, mc);
		dist.bindToKeyHold(keyCode, mc);
		
		//links
		mc = new MovementCommand(playerShip, pm,
				MOVE_DIRECTION.STRAFE_LEFT);
		keyCode = kb.getKeyBinding(CommandType.STRAFE_LEFT);
		dist.bindToKeyPress(keyCode, mc);
		dist.bindToKeyHold(keyCode, mc);
		
		//rechts
		mc = new MovementCommand(playerShip, pm,
				MOVE_DIRECTION.STRAFE_RIGHT);
		keyCode = kb.getKeyBinding(CommandType.STRAFE_RIGHT);
		dist.bindToKeyPress(keyCode, mc);
		dist.bindToKeyHold(keyCode, mc);
		
		//Rotation
		UTEntityManager.getInstance().getController(ship.getId()).
			addTimedController(new RotationCommand(ship, pm));
		
		//Blaster
		BlasterCommand bc = new BlasterCommand(ship);
		keyCode = kb.getMouseBinding(CommandType.FIRE_BLASTER);
		dist.bindToMousePress(keyCode, bc);
		dist.bindToMouseHold(keyCode, bc);
		
		//Raketenwerfer
		RocketCommand rc = new RocketCommand(ship);
		keyCode = kb.getMouseBinding(CommandType.FIRE_ROCKET);
		dist.bindToMousePress(keyCode, rc);
		dist.bindToMouseHold(keyCode, rc);
	}

	/**
	 * @return aktuelles Schiff des Spielers oder null, falls nicht vorhanden
	 */
	public ShipEntity getPlayerShip()
	{
		return playerShip;
	}

	/**
	 * @return Kollektion aller intern gehaltetenen Entities
	 */
	public EntityCollection getEntities()
	{
		return entities;
	}
	
	/**
	 * @return Spielfenster
	 */
	public ClientWindow getWindow()
	{
		return window;
	}

	/**
	 * @return aktueller Spielmodus-Controller
	 */
    public GameModeController getGameModeController()
    {
        return gmcontroller;
    }

    /**
     * @param gmcontroller neuer Spielmodus-Controller
     */
    public void setGameModeController(GameModeController gmcontroller)
    {
        this.gmcontroller = gmcontroller;
    }
}
