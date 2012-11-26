/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.logic.entities.ingame.controller;

import universetournament.shared.logic.entities.ingame.*;
import universetournament.shared.logic.entities.ingame.container.*;
import universetournament.shared.util.math.*;

/**
 *
 * @author dheinrich
 */
public class RocketMovment extends SimpleMovement
{
    private TransformationContainer target;
    private PhysicContainer rocket;
    private double maxrot;

    public RocketMovment(RocketEntity ent, WorldEntity target) {
        super(ent);
        if(target != null)
            this.target = target.getTransformation();
        rocket = ent.getTransformation();
        maxrot = ent.getConfiguration().getConstSpeedRot();
    }

    @Override
    public void refresh(float timeDiff) {
        super.refresh(timeDiff);
        if(target == null)
            return;
        Matrix4 mr = rocket.getModelMatrix();

        Vec3 dir = new Vec3(mr.getMinor(3, 3).mult(new Vec3(0,
                                                               0,
                                                               1)));
        Vec3 tdir = target.getPosition().sub(rocket.getPosition());

        dir.normalize(dir);
        tdir.normalize(tdir);
        double cos = Math.abs(dir.dot(tdir));
        double winkel = Math.acos(cos) * 180 / Math.PI;
        double accrot = maxrot * timeDiff / 1000000000f;
        if(accrot < winkel)
           winkel = accrot;

        Vec3 neg = dir.cross(tdir);
        if (neg.getZ() >= 0)
            winkel *= -1;
        rocket.rotateEuler(new Vec3(0, winkel, 0));
        System.out.println(dir);
        System.out.println(tdir);
        System.out.println(winkel);
        System.out.println("---");
        Vec3 vel = rocket.getVelocity();
        Matrix4 rot = new Matrix4();
        rot.loadIdentity();
        rot.rotateEuler(0, winkel, 0);
        Vec3 newvel = new Vec3(rot.mult(vel));
        for (int i = 0; i < 3; ++i)
            vel.getCoords()[i] = newvel.getCoords()[i];
    }

    public static void main(String[] args){
        Vec3 vel = new Vec3(1, 0, 0);
        Vec3 vel2 = new Vec3(-2, 0, -4);
        vel.normalize(vel);
        vel2.normalize(vel2);

        double cos = vel.dot(vel2);

        double winkel = Math.acos(cos) * 180 / Math.PI;
        System.out.println(winkel);
        Vec3 neg = vel.cross(vel2);
        if (neg.getZ() >= 0)
            winkel *= -1;
        Matrix4 rot = new Matrix4();
        rot.loadIdentity();
        rot.rotateEuler(0, winkel, 0);
        Vec3 newvel = new Vec3(rot.mult(vel));
        for (int i = 0; i < 3; ++i)
            vel.getCoords()[i] = newvel.getCoords()[i];
        System.out.println(vel);
    }
}
