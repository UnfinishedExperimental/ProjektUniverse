/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.logic.entities.controller;

import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.ingame.controller.MoveData;
import de.fhhof.universe.shared.logic.entities.ingame.controller.MovementController;

/**
 *
 * @author dheinrich
 */
public class LerpMovment extends MovementController {

    private PhysicContainer pcon;
    private MoveData old;
    private Vector3 rotation, rotdelta;

    public LerpMovment(WorldEntity<? extends PhysicContainer> entity) {
        super(entity);
        pcon = entity.getTransformation();
        rotation = new Vector3();
        rotdelta = new Vector3();
    }

    @Override
    protected void handleMoveData(MoveData mdata) {
        if (old != null) {
            pcon.setWorldPosition(old.getTranslation());
            pcon.rotateEuler(rotdelta.add(old.getRotation()));

            Vector3 vel = mdata.getTranslation().clone().sub(old.getTranslation());
            vel.mul(mdata.getTimestamp() - old.getTimestamp() / 1000f);
            pcon.setVelocity(vel);

            rotation = mdata.getRotation().clone().sub(old.getRotation());
            rotation.mul(mdata.getTimestamp() - old.getTimestamp() / 1000f);
        }
        old = mdata;
        rotdelta = new Vector3();
    }

    @Override
    public void refresh(float timeDiff) {
        pcon.shiftWorldPosition(pcon.getVelocity().clone().mul(timeDiff));

        Vector3 newrot = rotation.clone().mul(timeDiff);
        rotdelta.sub(newrot);
        pcon.rotateEuler(newrot);
    }
}
