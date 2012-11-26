package universetournament.server.logic.controller;

import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.controller.MoveData;
import universetournament.shared.logic.entities.ingame.controller.SimpleMovement;
import universetournament.shared.util.math.Vec3;

/**
 * Einfaches Movement, welches selbst Bewegung berechnet und allen Clients
 * übermittelt.
 * 
 * @author Florian Holzschuher
 *
 */
public class SimpleExtMovement extends SimpleMovement
{
	private final PhysicContainer pcon;

	public SimpleExtMovement(
			WorldEntity<? extends PhysicContainer> ent)
	{
		super(ent);
		pcon = ent.getTransformation();
	}

	@Override
	public void refresh(float timeDiff)
	{
        super.refresh(timeDiff);
        
        MoveData data = new MoveData(pcon.getPosition(), new Vec3(),
        		pcon.getVelocity());
        EntityEvent event = new EntityEvent(getEntity().getId(),
        		WorldEntity.SubEvents.MOVE, data);
        
        PJUTEventBuffManager.getInstance().sendToAll(event);
    }
}
