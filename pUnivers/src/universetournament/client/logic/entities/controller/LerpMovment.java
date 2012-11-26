/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.logic.entities.controller;

import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.controller.MoveData;
import universetournament.shared.logic.entities.ingame.controller.MovementController;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public class LerpMovment extends MovementController
{
    private PhysicContainer pcon;
    private MoveData old;
    private Vec3 rotation, rotdelta;

    public LerpMovment(WorldEntity<? extends PhysicContainer> entity) {
        super(entity);
        pcon = entity.getTransformation();
        rotation = new Vec3();
        rotdelta = new Vec3();
    }

    @Override
    protected void handleMoveData(MoveData mdata) {
        if (old != null) {
            pcon.setWorldPosition(old.getTranslation());
            pcon.rotateEuler(rotdelta.add(old.getRotation()));

            Vec3 vel = mdata.getTranslation().sub(old.getTranslation());
            vel.mult(mdata.getTimestamp() - old.getTimestamp() / 1000f, vel);
            pcon.setVelocity(vel);

            mdata.getRotation().sub(old.getRotation(), rotation);
            rotation.mult(mdata.getTimestamp() - old.getTimestamp() / 1000f,
                         rotation);
        }
        old = mdata;
        rotdelta = new Vec3();
    }

    public void refresh(float timeDiff) {
        pcon.shiftWorldPosition(pcon.getVelocity().mult(timeDiff));

        Vec3 newrot = rotation.mult(timeDiff);
        rotdelta.sub(newrot, rotdelta);
        pcon.rotateEuler(newrot);
    }
}
