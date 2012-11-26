/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.logic.entities.ingame.container;

import java.io.Serializable;
import universetournament.shared.util.math.*;

/**
 *
 * @author dheinrich
 */
public interface TransformationContainer extends Serializable{
    /**
     * @return
     * position-vector in world coordinates
     */
    public Vec3 getPosition();

    /**
     * sets world-position
     * @param newpos
     * new position-vector
     */
    public void setPosition(Vec3 newpos);

    /**
     * shifts position in world space
     * @param p
     * shift vector
     */
    public void shiftWorldPosition(Vec3 delta);
    
    /**
     * sets the position in world space
     * @param pos
     */
    public void setWorldPosition(Vec3 pos);

    /**
     * shifts position in object space
     * @param p
     * shift vector
     */
    public void shiftRelativePosition(Vec3 delta);

    /**
     * rotate Object
     * @param delta
     * vector which discribes rotation for each axis
     */
    public void rotateEuler(Vec3 delta);

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
    public void scale(Vec3 delta);

    /**
     * @return ModelMatrix of Object.
     * altering this matrix don't effect the object transformation
     */
    public ModelMatrix getModelMatrix();

    public void reset();
}
