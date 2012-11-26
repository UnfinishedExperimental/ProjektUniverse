package fholzschuher;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.util.EntitySubController;
import universetournament.testing.dheinrich.MoveData;

/**
 * 
 * @author  Florian Holzschuher
 *
 */
public class TestMovementController extends EntitySubController<SquareEntity>
{
	
	public TestMovementController(SquareEntity entity)
	{
		super(entity);
	}

	@Override
	public SubType[] getTypes()
	{
		SubType list[] = {TestEvent.SubEvents.MOVE};
		return list;
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof MoveData)
		{
			MoveData data = (MoveData) event.getData();
			entity.setPos(data.getX(), data.getY());
		}
	}
}
