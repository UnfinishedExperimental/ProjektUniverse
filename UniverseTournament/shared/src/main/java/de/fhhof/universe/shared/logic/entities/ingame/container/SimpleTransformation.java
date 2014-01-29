/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.logic.entities.ingame.container;

import darwin.util.math.base.Quaternion;
import darwin.util.math.base.matrix.Matrix4;
import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import darwin.util.math.composits.ModelMatrix;


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

    public ImmutableVector<Vector3> getPosition() {
        return matrix.getTranslation();
    }

    public void setPosition(ImmutableVector<Vector3> newpos) {
        Vector3 tmp = newpos.clone().sub(getPosition());
        matrix.worldTranslate(tmp);
    }

    public void shiftWorldPosition(ImmutableVector<Vector3> delta) {
        matrix.worldTranslate(delta);
    }

    public void shiftRelativePosition(ImmutableVector<Vector3> delta) {
        matrix.translate(delta);
    }

    public void rotateEuler(ImmutableVector<Vector3> delta) {
        matrix.rotateEuler(delta);
    }

    public void rotate(Matrix4 rotmat) {
        matrix.rotateEuler(rotmat.getEularAngles());
    }

    public void scale(ImmutableVector<Vector3> delta) {
        matrix.scale(delta);
    }

    public ModelMatrix getModelMatrix() {
        ModelMatrix m = new ModelMatrix();
        m.setMat(matrix.getArray().clone());
        return m;
    }

    public void setWorldPosition(ImmutableVector<Vector3> pos) {
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
