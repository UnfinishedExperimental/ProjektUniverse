/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.cameras;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import darwin.util.math.composits.ViewMatrix;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.TransformationContainer;

/**
 *
 * @author dheinrich
 */
public class FollowCam implements Camera
{
    private TransformationContainer follow;
    private float radius;
    private Vector3 rotation, translation;

    public FollowCam(WorldEntity follow, float radius, ImmutableVector<Vector3> rotation,
                     ImmutableVector<Vector3> translation) {
        this(follow.getTransformation(), radius, rotation, translation);
    }

    public FollowCam(TransformationContainer follow, float radius,
                     ImmutableVector<Vector3> rotation, ImmutableVector<Vector3> translation) {
        this.follow = follow;
        this.radius = radius;
        this.rotation = rotation.clone();
        this.translation = translation.clone();
    }

    public ViewMatrix getViewMatrix() {
        ViewMatrix m = new ViewMatrix();
        m.setMat(follow.getModelMatrix().getArray());

        m.translate(getTranslation());
        m.rotateEuler(getRotation());
        m.translate(0f, 0f, -radius);
        m.rotateEuler(0, 180, 0);
        return m.inverse();
    }

    public void follow(TransformationContainer t){
        follow = t;
    }

    public void follow(WorldEntity ent){
        follow(ent.getTransformation());
    }

    public float getRadius() {
        return radius;
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public Vector3 getTranslation() {
        return translation;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
