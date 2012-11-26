package universetournament.client.logic.entities;

import java.util.HashMap;

import universetournament.client.core.ClientMainController;
import universetournament.client.rendering.*;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.*;
import universetournament.shared.logic.entities.ingame.ShotEntity;
import universetournament.shared.logic.entities.ingame.controller.SimpleMovement;
import universetournament.shared.logic.entities.util.*;

/**
 * Erhält vom Server erzeugte Schuss-Entites und übernimmt sie ins lokale
 * System des Clients oder entfernt sie wieder.
 * 
 * @author sylence
 *
 */
public class ShotAssembler implements PJUTEventHandler
{
	private final HashMap<Short, Renderable> renderables;
	
	public ShotAssembler()
	{
		renderables = new HashMap<Short, Renderable>();
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof ShotEntity)
			{
				ShotEntity shot = (ShotEntity) container.getData();
				
				try
				{
					Renderable rend = shot.buildRenderObject();
					renderables.put(shot.getId(), rend);
					Scene.getInstance().addRenderable(rend);
				}
				catch (InstantiationException ex)
				{
					// TODO: Exception handling verbessern
					throw new RuntimeException(
							"RenderObject konnte nicht initialisiert werden." +
							"Fehler in der RenderConfiguration(\""
							+ shot.getConfiguration().getName() + "\")");
				}
				catch (IllegalAccessException ex)
				{
					throw new RuntimeException(
							"RenderObject konnte nicht initialisiert"
							+ " werden. Fehler in der RenderConfiguration(\""
							+ shot.getConfiguration().getName() + "\")");
				}
				
				//Controller registrieren
				EntityController<ShotEntity> controller =
					new EntityController<ShotEntity>(shot);
				UTEntityManager.getInstance().setController(shot.getId(),
						controller);
				
				//Bewegung
				SimpleMovement mov = new SimpleMovement(shot);
				
				controller.addSubController(mov);
				controller.addTimedController(mov);
				
				//Schuss ins System übernehmen
				ClientMainController.getInstance().getEntities().
					addShot(shot);
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short sid = ((EntityEvent)event).getId();
				
				//Schuss mit ID entfernen
				ClientMainController.getInstance().getEntities().
					removeShot(sid);
				
				UTEntityManager.getInstance().setController(sid, null);
				
				Renderable rend = renderables.get(sid);
				Scene.getInstance().removeRenderable(rend);
			}
		}
	}
}
