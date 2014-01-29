/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.logic.entities.ingame.controller;

import darwin.util.math.base.matrix.Matrix4;
import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.shared.logic.entities.ingame.RocketEntity;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.ingame.container.TransformationContainer;

/**
 *
 * @author dheinrich
 */
public class RocketMovment extends SimpleMovement {

    private TransformationContainer target;
    private PhysicContainer rocket;
    private double maxrot;

    public RocketMovment(RocketEntity ent, WorldEntity target) {
        super(ent);
        if (target != null) {
            this.target = target.getTransformation();
        }
        rocket = ent.getTransformation();
        maxrot = ent.getConfiguration().getConstSpeedRot();
    }

    @Override
    public void refresh(float timeDiff) {
        super.refresh(timeDiff);
        if (target == null) {
            return;
        }
        Matrix4 mr = rocket.getModelMatrix();

        float[] v = mr.getMinor(3, 3).mult(new Vector3(0, 0, 1)).getCoords();
        Vector3 dir = new Vector3(v[0], v[1], v[2]).normalize();
        Vector3 tdir = target.getPosition().clone().sub(rocket.getPosition()).normalize();
        
        double cos = Math.abs(dir.dot(tdir));
        double winkel = Math.acos(cos) * 180 / Math.PI;
        double accrot = maxrot * timeDiff / 1000000000f;
        if (accrot < winkel) {
            winkel = accrot;
        }

        Vector3 neg = dir.cross(tdir);
        if (neg.getZ() >= 0) {
            winkel *= -1;
        }
        rocket.rotateEuler(new Vector3(0, (float)winkel, 0));
        System.out.println(dir);
        System.out.println(tdir);
        System.out.println(winkel);
        System.out.println("---");
        ImmutableVector<Vector3> vel = rocket.getVelocity();
        Matrix4 rot = new Matrix4();
        rot.loadIdentity();
        rot.rotateEuler(0, (float) winkel, 0);
        Vector3 newvel = rot.fastMult(vel.toVector3());
        for (int i = 0; i < 3; ++i) {
            vel.getCoords()[i] = newvel.getCoords()[i];
        }
    }
}
