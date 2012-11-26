package universetournament.server.logic.entities;

import java.awt.Color;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.controller.PlayerController;
import universetournament.shared.logic.entities.requests.PlayerRequest;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.logic.gamemode.events.GameModeSubType;

/**
 * Factory, die Spieler erstellt, sie an den entsprechenden Stellen registriert
 * und sie an die Clients verteilt.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author Florian Holzschuher
 *
 */
public class PlayerFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof PlayerRequest)
			{
				PlayerRequest request = (PlayerRequest) container.getData();
				
				//Spieler erstellen
				PJUTPlayer player = new PJUTPlayer(request.getName(),
						EntityFactory.getInstance().getUniqueId(),
						request.getClientId());
				player.setColor(new Color(request.getColorRGB()));
				
				
				//Spieler registrieren
				ServerMainController.getInstance().getEntities().
					addPlayer(player);
				
				EntityController<PJUTPlayer> controller =
					new EntityController<PJUTPlayer>(player);
				controller.addSubController(new PlayerController(player));
				
				//per Event der Spielmodus-Logik zukommen lassen
				ServerMainController.getInstance().getGameModeController().
					handleEvent(new GameEvent(MainType.TYPE_GAMEMODE,
							GameModeSubType.PLAYER_JOINED, player));
				
				//Spieler an Clients verteilen
				container = new EntityContainer(EntityType.PLAYER, player);
				
				EntityEvent ee = new EntityEvent(player.getId(),
						Entity.SubEvents.CREATE, container);
				
				PJUTEventBuffManager.getInstance().sendToAll(ee);
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short pid = ((EntityEvent)event).getId();
				
				ServerMainController.getInstance().getEntities().
					removePlayer(pid);
				
				UTEntityManager.getInstance().setController(pid, null);
				
				//bei Clients löschen lassen
				container = new EntityContainer(EntityType.PLAYER, null);
				
				EntityEvent ee = new EntityEvent(pid,
						Entity.SubEvents.DELETE, container);
				PJUTEventBuffManager.getInstance().sendToAll(ee);
				
				//ID freigeben
				EntityFactory.getInstance().freeUniqueId(pid);
			}
		}
	}
}
