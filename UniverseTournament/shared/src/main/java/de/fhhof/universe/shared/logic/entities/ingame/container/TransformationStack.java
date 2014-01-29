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
import java.io.IOException;
import java.util.*;

/**
 *
 * @author dheinrich
 */
public class TransformationStack implements TransformationContainer
{
    private List<ModelMatrix> transform;
    private int listsize = 0;
    private long matrizenhash = 0;
    private ModelMatrix packed, main;

    public TransformationStack() {
        packed = new ModelMatrix();
        packed.loadIdentity();
        transform = new LinkedList<ModelMatrix>();
        main = addTLayer2Beginning();
    }

    public ModelMatrix getMainMatrix() {
        return main;
    }

    public ModelMatrix addTLayer2Beginning() {
        ModelMatrix tmp = new ModelMatrix();
        tmp.loadIdentity();
        transform.add(0, tmp);
        return tmp;
    }

    public ModelMatrix addTLayer2End() {
        ModelMatrix tmp = new ModelMatrix();
        tmp.loadIdentity();
        transform.add(tmp);
        return tmp;
    }

    public ImmutableVector<Vector3> getPosition() {
        return main.getTranslation();
    }

    public void setPosition(ImmutableVector<Vector3> newpos) {
        Vector3 tmp= newpos.clone().sub(getPosition());
        main.worldTranslate(tmp);
    }

    public void shiftWorldPosition(ImmutableVector<Vector3> delta) {
        main.worldTranslate(delta);
    }

    public void setWorldPosition(ImmutableVector<Vector3> pos) {
        main.setWorldTranslate(pos);
    }

    public void shiftRelativePosition(ImmutableVector<Vector3> delta) {
        main.translate(delta);
    }

    public void rotateEuler(ImmutableVector<Vector3> delta) {
        main.rotateEuler(delta);
    }

    public void rotate(Matrix4 rotmat) {
        main.rotateEuler(rotmat.getEularAngles());
    }

    public void rotate(Quaternion rotation) {
        main.rotate(rotation);
    }

    public Quaternion getRotation() {
        return main.getRotation();
    }

    public void setRotation(Quaternion rot) {
        setRotation(rot.getRotationMatrix());
    }

    public void setRotation(Matrix4 rot) {
        main.setRotation(rot);
    }

    public void scale(ImmutableVector<Vector3> delta) {
        main.scale(delta);
    }

    public ModelMatrix getModelMatrix() {
        if (listsize == transform.size()) {
            long hash = 0;
            for (Matrix4 m : transform)
                hash = 23 * hash + m.hashCode();
            if (hash == matrizenhash)
                return packed;
            matrizenhash = hash;
        }
        listsize = transform.size();

        packed.loadIdentity();
        boolean h = true;
        for (ModelMatrix m : transform) {
            packed.mult(m);
            h &= m.isHomogeneous();
        }
        packed.setHomogeneous(h);

        return packed;
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        transform = (List<ModelMatrix>) in.readObject();
        main = (ModelMatrix) in.readObject();
        packed = new ModelMatrix();
        packed.loadIdentity();
        listsize = 0;
        matrizenhash = 0;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.writeObject(transform);
        out.writeObject(main);
    }

    public void reset() {
        main.loadIdentity();
        matrizenhash = 0;
    }
}
