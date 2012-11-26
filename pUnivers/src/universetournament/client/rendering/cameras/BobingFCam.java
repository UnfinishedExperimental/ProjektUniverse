/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.cameras;

import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public class BobingFCam extends FollowCam
{
    private Vec3 deltarot, deltatrans;


    public BobingFCam(TransformationContainer trans, float radius, Vec3 rotation,
                     Vec3 translation) {
        super(trans, radius, rotation, translation);
        deltarot = new Vec3();
        deltatrans = new Vec3();
    }

    public Vec3 getDeltaRot() {
        return deltarot;
    }

    public Vec3 getDeltaTrans() {
        return deltatrans;
    }

    @Override
    public Vec3 getRotation() {
        return (Vec3) super.getRotation().add(deltarot);
    }

    @Override
    public Vec3 getTranslation() {
        return (Vec3) super.getTranslation().add(deltatrans);
    }

    public Vec3 getCamTrans(){
        return super.getTranslation();
    }

    public Vec3 getCamRot(){
        return super.getRotation();
    }
}
