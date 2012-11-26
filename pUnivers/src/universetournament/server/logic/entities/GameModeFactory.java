package universetournament.server.logic.entities;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.server.logic.gamemode.UTDeathMatch;
import universetournament.server.logic.gamemode.UTGameModeController;
import universetournament.server.logic.gamemode.UTLastManStanding;
import universetournament.server.logic.gamemode.UTLooting;
import universetournament.server.logic.gamemode.UTTeamDeathMatch;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.requests.GameModeRequest;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.logic.gamemode.GameModeType;
import universetournament.shared.logic.gamemode.UTGameModeData;
import universetournament.shared.logic.gamemode.UTLastManStandingData;
import universetournament.shared.logic.gamemode.UTTeamModeData;

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
