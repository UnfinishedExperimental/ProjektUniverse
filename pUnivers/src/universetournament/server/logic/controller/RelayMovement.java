package universetournament.server.logic.controller;

import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.controller.MoveData;

/**
 * Controller, welcher Bewegungen weiterleitet, aber auch intern auf die Entity
 * abbildet.
 * 
 * @author Florian Holzschuher
 *
 */
public class RelayMovement extends 
	RelayController<WorldEntity<? extends PhysicContainer>>
{
	private static final SubType types[] = {WorldEntity.SubEvents.MOVE};
	
	private final PhysicContainer container;
	
	public RelayMovement(WorldEntity<? extends PhysicContainer> entity)
	{
		super(entity, types);
		container = entity.getTransformation();
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getData() instanceof MoveData)
		{
			MoveData data = (MoveData)event.getData();
			
			container.setWorldPosition(data.getTranslation());
			container.setVelocity(data.getVelocity());
			container.rotateEuler(data.getRotation());
			
			PJUTEventBuffManager.getInstance().sendToAll(event);
		}
	}
}
