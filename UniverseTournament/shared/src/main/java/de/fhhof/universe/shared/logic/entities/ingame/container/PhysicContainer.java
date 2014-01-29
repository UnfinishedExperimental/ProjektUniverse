/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.shared.logic.entities.ingame.container;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;


/**
 *
 * @author dheinrich
 */
public interface PhysicContainer extends TransformationContainer{
    /**
     * @return
     * velocity vector in world space
     */
    public ImmutableVector<Vector3> getVelocity();
    
    /**
     * sets 
     * @param newvel
     */
    public void setVelocity(ImmutableVector<Vector3> newvel);
    
//    public Vec3f getRotVelocity();
//    public void setRotVelocity(Vec3f newvel);

    /**
     * @return
     * physical radius of object
     */
    public float getRadius();

    /**
     * @return
     * weigth/mass of object
     */
    public float getMass();
}
