package de.fhhof.universe.server.logic.entities;

import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.server.logic.gamemode.UTDeathMatch;
import de.fhhof.universe.server.logic.gamemode.UTGameModeController;
import de.fhhof.universe.server.logic.gamemode.UTLastManStanding;
import de.fhhof.universe.server.logic.gamemode.UTLooting;
import de.fhhof.universe.server.logic.gamemode.UTTeamDeathMatch;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.requests.GameModeRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;
import de.fhhof.universe.shared.logic.gamemode.GameModeType;
import de.fhhof.universe.shared.logic.gamemode.UTGameModeData;
import de.fhhof.universe.shared.logic.gamemode.UTLastManStandingData;
import de.fhhof.universe.shared.logic.gamemode.UTTeamModeData;

/**
 * Factory, die Spielmodi erstellt und sie an den entsprechenden Stellen
 * registriert.
 * Spielmodi sollten erstellt werden bevor sich die Clients verbinden.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author  Florian Holzschuher
 *
 */
public class GameModeFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof GameModeRequest)
			{
				GameModeRequest request = (GameModeRequest)container.getData();
				
				switch(request.getType())
				{
					case DEATH_MATCH:
						createDeathMatch(request);
						break;
						
					case TEAM_DEATH_MATCH:
						createTeamDeathMatch(request);
						break;
						
					case LAST_MAN_STANDING:
						createLastManStanding(request);
						break;
						
					case LOOTING:
						createLooting(request);
						break;
				}
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent
					&& container.getData() instanceof GameModeType)
			{
				short gmid = ((EntityEvent)event).getId();
				
				//TODO: in der Logik deregistrieren
				UTEntityManager.getInstance().setController(gmid, null);
				ServerMainController.getInstance().getEntities().
					setGameData(null);
				
				//bei Clients löschen (mit Typangabe)
				container = new EntityContainer(EntityType.GAME_MODE,
						container.getData());
				
				EntityEvent ee = new EntityEvent(gmid,
						Entity.SubEvents.DELETE, container);
				PJUTEventBuffManager.getInstance().sendToAll(ee);
			}
		}
	}
	
	private void createDeathMatch(GameModeRequest request)
	{
		//Daten-Entity
		UTGameModeData data = new UTGameModeData(
				EntityFactory.getInstance().getUniqueId(),
				request.getTimeLimit(), request.getScoreLimit());
		
		//dazugehörender Controller
		UTDeathMatch controller = new UTDeathMatch(data);
		
		//Spielmodus registrieren
		register(data, controller);
	}
	
	private void createTeamDeathMatch(GameModeRequest request)
	{
		//Daten-Entity
		UTTeamModeData data = new UTTeamModeData(
				EntityFactory.getInstance().getUniqueId(),
				request.getTimeLimit(), request.getScoreLimit());
		
		//dazugehörender Controller
		UTTeamDeathMatch controller = new UTTeamDeathMatch(data);
		
		//Spielmodus registrieren
		register(data, controller);
	}
	
	private void createLastManStanding(GameModeRequest request)
	{
		//Daten-Entity
		UTLastManStandingData data = new UTLastManStandingData(
				EntityFactory.getInstance().getUniqueId(),
				request.getTimeLimit(), request.getScoreLimit());
		
		//dazugehörender Controller
		UTLastManStanding controller = new UTLastManStanding(data);
		
		//Spielmodus registrieren
		register(data, controller);
	}
	
	private void createLooting(GameModeRequest request)
	{
		//Daten-Entity
		UTTeamModeData data = new UTTeamModeData(
				EntityFactory.getInstance().getUniqueId(),
				request.getTimeLimit(), request.getScoreLimit());
		
		//dazugehörender Controller
		UTLooting controller = new UTLooting(data);
		
		//Spielmodus registrieren
		register(data, controller);
	}
	
	private void register(UTGameModeData data, UTGameModeController controller)
	{
		UTEntityManager.getInstance().setController(data.getId(), controller);
		
		//TODO: in der Logik registrieren
		ServerMainController.getInstance().setGameModeController(controller);
		ServerMainController.getInstance().getEntities().setGameData(data);
	}
}
