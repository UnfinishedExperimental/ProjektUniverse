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
import java.io.Serializable;

/**
 *
 * @author dheinrich
 */
public interface TransformationContainer extends Serializable{
    /**
     * @return
     * position-vector in world coordinates
     */
    public ImmutableVector<Vector3> getPosition();

    /**
     * sets world-position
     * @param newpos
     * new position-vector
     */
    public void setPosition(ImmutableVector<Vector3> newpos);

    /**
     * shifts position in world space
     * @param p
     * shift vector
     */
    public void shiftWorldPosition(ImmutableVector<Vector3> delta);
    
    /**
     * sets the position in world space
     * @param pos
     */
    public void setWorldPosition(ImmutableVector<Vector3> pos);

    /**
     * shifts position in object space
     * @param p
     * shift vector
     */
    public void shiftRelativePosition(ImmutableVector<Vector3> delta);

    /**
     * rotate Object
     * @param delta
     * vector which discribes rotation for each axis
     */
    public void rotateEuler(ImmutableVector<Vector3> delta);

    /**
     * rotate Object
     * @param delta
     * rotationmatrix which will be applyed;
     */
    public void rotate(Matrix4 rotmat);
    
    public void rotate(Quaternion rotation);

    public Quaternion getRotation();

    public void setRotation(Quaternion rot);

    public void setRotation(Matrix4 rot);

    /**
     * rotate Object
     * @param delta
     * vector which discribes scale factor for each axis
     */
    public void scale(ImmutableVector<Vector3> delta);

    /**
     * @return ModelMatrix of Object.
     * altering this matrix don't effect the object transformation
     */
    public ModelMatrix getModelMatrix();

    public void reset();
}
