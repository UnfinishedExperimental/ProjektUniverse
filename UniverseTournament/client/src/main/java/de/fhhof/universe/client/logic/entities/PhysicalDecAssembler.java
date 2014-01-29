package de.fhhof.universe.client.logic.entities;


import de.fhhof.universe.client.core.ClientMainController;
import de.fhhof.universe.client.logic.entities.controller.LerpMovment;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.util.*;

public class PhysicalDecAssembler implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getData() instanceof EntityContainer)
		{
			EntityContainer container =
				(EntityContainer)event.getData();
			
			@SuppressWarnings("unchecked")
			WorldEntity<PhysicContainer> we =
				(WorldEntity<PhysicContainer>) container.getData();
			
			ClientMainController.getInstance().getEntities().
				addWorldEntity(we);
			
			EntityController<WorldEntity<PhysicContainer>> controller =
				new EntityController<WorldEntity<PhysicContainer>>(we);
			UTEntityManager.getInstance().setController(we.getId(), controller);
			
			LerpMovment mov = new LerpMovment(we);
			controller.addSubController(mov);
			controller.addTimedController(mov);
			
			try
			{
				Renderable rend = we.buildRenderObject();
				Scene.getInstance().addRenderable(rend);
			}
			catch (InstantiationException ex)
			{
				// TODO: Exception handling verbessern
				throw new RuntimeException(
						"RenderObject konnte nicht initialisiert werden." +
						"Fehler in der RenderConfiguration(\""
						+ we.getConfiguration().getName() + "\")");
			}
			catch (IllegalAccessException ex)
			{
				throw new RuntimeException(
						"RenderObject konnte nicht initialisiert"
						+ " werden. Fehler in der RenderConfiguration(\""
						+ we.getConfiguration().getName() + "\")");
			}
		}
	}
}
