/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.logic.entities.ingame.container;

import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public interface PhysicContainer extends TransformationContainer{
    /**
     * @return
     * velocity vector in world space
     */
    public Vec3 getVelocity();
    
    /**
     * sets 
     * @param newvel
     */
    public void setVelocity(Vec3 newvel);
    
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
