package de.fhhof.universe.server.logic.controller;

import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.ingame.controller.MoveData;
import de.fhhof.universe.shared.logic.entities.ingame.controller.SimpleMovement;

/**
 * Einfaches Movement, welches selbst Bewegung berechnet und allen Clients
 * übermittelt.
 *
 * @author Florian Holzschuher
 *
 */
public class SimpleExtMovement extends SimpleMovement {

    private final PhysicContainer pcon;

    public SimpleExtMovement(
            WorldEntity<? extends PhysicContainer> ent) {
        super(ent);
        pcon = ent.getTransformation();
    }

    @Override
    public void refresh(float timeDiff) {
        super.refresh(timeDiff);

        MoveData data = new MoveData(pcon.getPosition(), new Vector3(),
                pcon.getVelocity(), 0);
        EntityEvent event = new EntityEvent(getEntity().getId(),
                WorldEntity.SubEvents.MOVE, data);

        PJUTEventBuffManager.getInstance().sendToAll(event);
    }
}
