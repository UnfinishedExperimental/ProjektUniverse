package de.fhhof.universe.server;

import java.io.File;

import de.fhhof.universe.server.communication.PJUTConnManager;
import de.fhhof.universe.server.communication.PJUTServerThread;
import de.fhhof.universe.server.config.ServerConfig;
import de.fhhof.universe.server.core.PJUTServerCore;
import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.gui.Gui_Serverans;
import de.fhhof.universe.server.gui.Gui_Serverconf;
import de.fhhof.universe.server.gui.LMSViewUpdater;
import de.fhhof.universe.server.gui.TeamUpdater;
import de.fhhof.universe.server.gui.ViewUpdater;
import de.fhhof.universe.server.logic.controller.AgeController;
import de.fhhof.universe.server.logic.controller.CollisionController;
import de.fhhof.universe.server.logic.entities.DecorationFactory;
import de.fhhof.universe.server.logic.entities.EntityFactory;
import de.fhhof.universe.server.logic.entities.GameModeFactory;
import de.fhhof.universe.server.logic.entities.PhysicalDecFactory;
import de.fhhof.universe.server.logic.entities.PlayerFactory;
import de.fhhof.universe.server.logic.entities.RocketFactory;
import de.fhhof.universe.server.logic.entities.ShipFactory;
import de.fhhof.universe.server.logic.entities.ShotFactory;
import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.events.util.PJUTEventBus;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.requests.GameModeRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;
import de.fhhof.universe.shared.logic.gamemode.UTGameModeData;
import de.fhhof.universe.shared.logic.gamemode.UTLastManStandingData;
import de.fhhof.universe.shared.logic.gamemode.UTTeamModeData;
import de.fhhof.universe.shared.util.io.UTXMLReader;
import de.fhhof.universe.shared.data.proto.util.ConfigurationLoader;

/**
 * Klasse die zur Initialisierung einer Instanz des Servers dient und dessen
 * Kern bereitstellt.
 *
 * @author Florian Holzschuher
 *
 */
public class UTServer
{
	/**
	 * Standard Server-Konfigurationspfad.
	 */
	public static final String configPath = "data/server_config.xml";
	private static PJUTServerCore core;

	/**
	 * Initialisiert einen Server mit den gegebenen Einstellungen und
	 * Parametern.
	 *
	 * @param args übergebene Argumente
	 */
	public static void main(String[] args)
	{
		/*
		 * Konfiguration wird nur eingebunden, wenn erfolgreich geladen.
		 * Ansonsten verbleibt die Standard-Konfiguration.
		 */
		UTXMLReader reader = new UTXMLReader();
		ServerConfig conf = reader.read(ServerConfig.class,
				new File(configPath));
		ServerConfig.setInstance(conf);

		try
		{
			Gui_Serverconf gui = new Gui_Serverconf();
			gui.setVisible(true);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Konnte Server-GUI nicht anzeigen");
		}
	}

	/**
	 * Initialisiert den Server mit den Werten aus der Konfiguration.
	 */
	public UTServer()
	{
		loadRessources();

		//interne Logik vorbereiten
		prepareLogic();

		//Spielmodus starten
		startGameMode();

		//Port auf dem gelauscht wird
		createListener();

		//GUI anzeigen und auffrischen lassen
		try
		{
			Gui_Serverans gui = new Gui_Serverans();
			gui.setVisible(true);

			UTGameModeData data = ServerMainController.getInstance().
				getEntities().getGameData();

			if(data instanceof UTTeamModeData)
			{
				new Thread(new TeamUpdater(gui, (UTTeamModeData)data)).start();
			}
			else if(data instanceof UTLastManStandingData)
			{
				new Thread(new LMSViewUpdater(gui,
						(UTLastManStandingData)data)).start();
			}
			else
			{
				new Thread(new ViewUpdater<UTGameModeData>(gui, data)).start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Konnte GUI nicht anzeigen");
		}
	}

	private void loadRessources()
	{
		//liest alle über XML definierten Objekte ein
		try
		{
			ConfigurationLoader cLoader = new ConfigurationLoader(
					ConfigurationLoader.STD_PATH);
			cLoader.readAll();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Konnte Konfigurationen nicht laden");
		}
	}

	private void createListener()
	{
		try
		{
			new Thread(new PJUTServerThread(
					ServerConfig.getInstance().getPort())).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("ServerSocket-Öffnung fehlgeschlagen");
		}
	}

	private void prepareLogic()
	{
		//Kern als zentraler Impulsgeber
		PJUTServerCore core = new PJUTServerCore();
		PJUTEventBus eb = PJUTEventBus.getInstance();

		//Factories erstellen
		UTEntityManager em = UTEntityManager.getInstance();

		EntityFactory ef = EntityFactory.getInstance();
		ef.setSubFactory(EntityType.SHIP, new ShipFactory());
		ef.setSubFactory(EntityType.PLAYER, new PlayerFactory());
		ef.setSubFactory(EntityType.GAME_MODE, new GameModeFactory());
		ef.setSubFactory(EntityType.SHOT, new ShotFactory());
		ef.setSubFactory(EntityType.ROCKET, new RocketFactory());
		ef.setSubFactory(EntityType.DECORATION, new DecorationFactory());
		ef.setSubFactory(EntityType.PHYSICAL_DEC, new PhysicalDecFactory());

		//Systeme beieinander registrieren
		em.setEntityCreator(ef);
		em.setEntityDestroyer(ef);

		eb.register(em, MainType.TYPE_ENTITY);
		core.addTimedRefreshable(em);

		PJUTConnManager.getInstance().setListener(
				ServerMainController.getInstance());

		//Kollisionen
		core.addTimedRefreshable(new CollisionController());
		
		//Alterung - Löschung von Schüssen etc.
		core.addTimedRefreshable(new AgeController());
		
		//Impulsgeber starten
		setCore(core);
		core.setActive(true);
		new Thread(core).start();
	}

	private void startGameMode()
	{
		//Request an Factory senden, welche den Modus initialisiert
		ServerConfig conf = ServerConfig.getInstance();
		try
		{
			//Parameter stehen in der Konfiguration
			GameModeRequest request = new GameModeRequest(
					conf.getGameType(), conf.getTimeLimit(),
					conf.getScoreLimit());
			EntityContainer container = new EntityContainer(
					EntityType.GAME_MODE, request);
			EntityEvent event = new EntityEvent((short)0,
					Entity.SubEvents.CREATE, container);

			EntityFactory.getInstance().handleEvent(event);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.err.println("Konnte Spielmodus nicht erstellen");
		}
	}

	private static synchronized void setCore(PJUTServerCore newCore)
	{
		core = newCore;
	}

	/**
	 * @return Kern des Servers, falls schon vorhanden.
	 */
	public static synchronized PJUTServerCore getCore()
	{
		return core;
	}
}
