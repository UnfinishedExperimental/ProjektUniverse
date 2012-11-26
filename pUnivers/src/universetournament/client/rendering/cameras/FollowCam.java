/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.cameras;

import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.*;

/**
 *
 * @author dheinrich
 */
public class FollowCam implements Camera
{
    private TransformationContainer follow;
    private float radius;
    private Vec3 rotation, translation;

    public FollowCam(WorldEntity follow, float radius, Vec3 rotation,
                     Vec3 translation) {
        this(follow.getTransformation(), radius, rotation, translation);
    }

    public FollowCam(TransformationContainer follow, float radius,
                     Vec3 rotation, Vec3 translation) {
        this.follow = follow;
        this.radius = radius;
        this.rotation = rotation;
        this.translation = translation;
    }

    public ViewMatrix getViewMatrix() {
        ViewMatrix m = new ViewMatrix();
        m.setMat(follow.getModelMatrix().getMat());

        m.translate(getTranslation());
        m.rotateEuler(getRotation());
        m.translate(0f, 0f, -radius);
        m.rotateEuler(0, 180, 0);
        return (ViewMatrix) m.inverse(m);
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

    public Vec3 getRotation() {
        return rotation;
    }

    public Vec3 getTranslation() {
        return translation;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
