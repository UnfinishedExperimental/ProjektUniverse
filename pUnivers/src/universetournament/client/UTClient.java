package universetournament.client;

import java.io.File;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.client.config.ClientConfig;
import universetournament.client.core.ClientMainController;
import universetournament.client.core.PJUTClientCore;
import universetournament.client.gui.Gui_Client;
import universetournament.client.input.PJUTInputDistributor;
import universetournament.client.logic.entities.DecorationAssembler;
import universetournament.client.logic.entities.EntityAssembler;
import universetournament.client.logic.entities.GameModeAssembler;
import universetournament.client.logic.entities.PhysicalDecAssembler;
import universetournament.client.logic.entities.PlayerAssembler;
import universetournament.client.logic.entities.RocketAssembler;
import universetournament.client.logic.entities.ShipAssembler;
import universetournament.client.logic.entities.ShotAssembler;
import universetournament.shared.communication.PJUTSocket;
import universetournament.shared.communication.events.PJUTEventReceiver;
import universetournament.shared.data.proto.util.ConfigurationLoader;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventBus;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.util.io.UTXMLReader;

/**
 * Klasse die zur Initialisierung einer Instanz des Clients dient und dessen
 * Kern bereitstellt.
 * 
 * @author Florian Holzschuher
 *
 */
public class UTClient
{
	/**
	 * Standard Client-Konfigurationspfad.
	 */
	public static final String configPath = "data/client_config.xml";
	private static PJUTClientCore core = new PJUTClientCore();
	
	/**
	 * Lädt die Konfiguration und zeigt das GUI an.
	 * 
	 * @param args übergebene Argumente
	 */
	public static void main(String[] args)
	{
        Handler handler = new ConsoleHandler();
        handler.setLevel(Level.INFO);

//        Logger logger = Logger.getLogger("universetournament");
//        logger.addHandler(handler);
//        logger.setLevel(Level.INFO);
        
        Logger logger = Logger.getLogger("punivers");
        logger.addHandler(handler);
        logger.setLevel(Level.INFO);

        
        /*
		 * Konfiguration wird nur eingebunden, wenn erfolgreich geladen.
		 * Ansonsten verbleibt die Standard-Konfiguration.
		 */

		UTXMLReader reader = new UTXMLReader();
		ClientConfig conf = reader.read(ClientConfig.class,
				new File(configPath));
		ClientConfig.setInstance(conf);

		loadRessources();
		
		try
		{
			Gui_Client gui = new Gui_Client();
			gui.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Konnte GUI nicht anzeigen");
		}
	}
	
	private static void loadRessources()
	{
		//liest alle über XML definierten Objekte ein
		try
		{
			ConfigurationLoader cLoader = new ConfigurationLoader(
					ConfigurationLoader.STD_PATH);
			cLoader.readAll();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println("Konnte Konfigurationen nicht laden");
		}
	}
	
	/**
	 * Initialisiert einen Client mit den Parametern aus der Konfiguration.
	 */
	public UTClient()
	{
		loadRessources();
		
		createConnection();
		
		prepareLogic();
		
		//Event-Empfang aktivieren
		PJUTCliConnector.getInstance().getReceiver().start();
		PJUTCliConnector.getInstance().getSocket().start();
	}
	
	private void createConnection()
	{
		ClientConfig conf = ClientConfig.getInstance();
		
		PJUTCliConnector connector = PJUTCliConnector.getInstance();
		try
		{
			//Verbindung aufbauen
			PJUTSocket socket = connector.connectTo(conf.getHost(),
					conf.getPort());
			
			//Puffer für ausgehende Events erstellen
			connector.createBuffer(socket);
			
			//Receiver für eingehende Events erstellen
			connector.createReceiver(socket);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Verbindung fehlgeschlagen");
		}
	}
	
	private void prepareLogic()
	{
		//Kern als zentraler Impulsgeber
		PJUTClientCore core = new PJUTClientCore();
		
		//Event-System
		PJUTEventBus eb = PJUTEventBus.getInstance();
		PJUTEventReceiver receiver =
			PJUTCliConnector.getInstance().getReceiver();
		
		//Entity-System
		UTEntityManager em = UTEntityManager.getInstance();
		EntityAssembler ea = EntityAssembler.getInstance();
		
		//Factories
		ea.setSubAssembler(EntityType.SHIP, new ShipAssembler());
		ea.setSubAssembler(EntityType.PLAYER, new PlayerAssembler());
		ea.setSubAssembler(EntityType.GAME_MODE, new GameModeAssembler());
		ea.setSubAssembler(EntityType.SHOT, new ShotAssembler());
		ea.setSubAssembler(EntityType.ROCKET, new RocketAssembler());
		ea.setSubAssembler(EntityType.DECORATION, new DecorationAssembler());
		ea.setSubAssembler(EntityType.PHYSICAL_DEC, new PhysicalDecAssembler());
		
		//Systeme beieinander registrieren
		em.setEntityCreator(ea);
		em.setEntityDestroyer(ea);
		core.addTimedRefreshable(em);
		
		eb.register(em, MainType.TYPE_ENTITY);
		receiver.setHandler(MainType.TYPE_ENTITY, em);
		
		receiver.setHandler(MainType.TYPE_GAME,
				ClientMainController.getInstance());
		
		//Input-System
		core.addTimedRefreshable(PJUTInputDistributor.getInstance());
		
		//Impulsgeber starten
		setCore(core);
		new Thread(core).start();
	}
	
	private static synchronized void setCore(PJUTClientCore newCore)
	{
		core = newCore;
	}
	
	/**
	 * @return Kern des Clients, falls schon vorhanden.
	 */
	public static synchronized PJUTClientCore getCore()
	{
		return core;
	}
}
