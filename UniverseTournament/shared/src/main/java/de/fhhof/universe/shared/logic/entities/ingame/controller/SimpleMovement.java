/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.logic.entities.ingame.controller;

import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.ingame.container.TransformationContainer;

/**
 *
 * @author dheinrich
 */
public class SimpleMovement extends MovementController
{
    private TransformationContainer tcon;

    public SimpleMovement(WorldEntity ent) {
        super(ent);
        tcon = ent.getTransformation();
    }

    @Override
    protected void handleMoveData(MoveData mdata) {
        tcon.setPosition(mdata.getTranslation());
        tcon.rotateEuler(mdata.getRotation());
        if (tcon instanceof PhysicContainer)
            ((PhysicContainer) tcon).setVelocity(mdata.getVelocity());
    }

    public void refresh(float timeDiff) {
        timeDiff /= 1000000000f;
        if (tcon instanceof PhysicContainer)
            tcon.shiftWorldPosition(
                    ((PhysicContainer) tcon).getVelocity().clone().mul(timeDiff));
    }
}
