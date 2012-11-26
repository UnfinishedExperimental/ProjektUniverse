/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.testing.dheinrich.collision;

import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public interface Sphere {
    public void preAdvance(float delta);
    
    public Vec3 getPosition();
    public void setPosition(Vec3 newpos);
    public void shiftPosition(Vec3 p);

    public Vec3 getVelocity();
    public void setVelocity(Vec3 newvel);

    public float getRadius();
    public float getMass();
}
