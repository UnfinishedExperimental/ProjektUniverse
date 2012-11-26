/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.logic.entities.ingame.container;

import universetournament.shared.util.math.*;

/**
 *
 * @author dheinrich
 */
public class SimpleTransformation implements TransformationContainer
{
    private ModelMatrix matrix;

    public SimpleTransformation() {
        matrix = new ModelMatrix();
        matrix.loadIdentity();
    }

    protected SimpleTransformation(ModelMatrix m) {
        matrix = m;
    }

    public Vec3 getPosition() {
        return matrix.getTranslation();
    }

    public void setPosition(Vec3 newpos) {
        newpos.sub(getPosition(), newpos);
        matrix.worldTranslate(newpos);
    }

    public void shiftWorldPosition(Vec3 delta) {
        matrix.worldTranslate(delta);
    }

    public void shiftRelativePosition(Vec3 delta) {
        matrix.translate(delta);
    }

    public void rotateEuler(Vec3 delta) {
        matrix.rotateEuler(delta);
    }

    public void rotate(Matrix4 rotmat) {
        matrix.rotateEuler(rotmat.getEularAngles());
    }

    public void scale(Vec3 delta) {
        matrix.scale(delta);
    }

    public ModelMatrix getModelMatrix() {
        return matrix.clone();
    }

    public void setWorldPosition(Vec3 pos) {
        matrix.setWorldTranslate(pos);
    }

    protected ModelMatrix getMatrix() {
        return matrix;
    }

    public void rotate(Quaternion rotation) {
        matrix.rotate(rotation);
    }

    public Quaternion getRotation() {
        return matrix.getRotation();
    }

    public void setRotation(Quaternion rot) {
        setRotation(rot.getRotationMatrix());
    }

    public void setRotation(Matrix4 rot) {
        matrix.setRotation(rot);
    }

    public void reset() {
        matrix.loadIdentity();
    }
}
