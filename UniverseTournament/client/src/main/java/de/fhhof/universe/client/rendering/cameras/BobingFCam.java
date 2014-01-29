/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.cameras;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.shared.logic.entities.ingame.container.TransformationContainer;

/**
 *
 * @author dheinrich
 */
public class BobingFCam extends FollowCam
{
    private Vector3 deltarot, deltatrans;


    public BobingFCam(TransformationContainer trans, float radius, ImmutableVector<Vector3> rotation,
                     ImmutableVector<Vector3> translation) {
        super(trans, radius, rotation, translation);
        deltarot = new Vector3();
        deltatrans = new Vector3();
    }

    public Vector3 getDeltaRot() {
        return deltarot;
    }

    public Vector3 getDeltaTrans() {
        return deltatrans;
    }

    @Override
    public Vector3 getRotation() {
        return super.getRotation().clone().add(deltarot);
    }

    @Override
    public Vector3 getTranslation() {
        return super.getTranslation().clone().add(deltatrans);
    }

    public Vector3 getCamTrans(){
        return super.getTranslation();
    }

    public Vector3 getCamRot(){
        return super.getRotation();
    }
}
