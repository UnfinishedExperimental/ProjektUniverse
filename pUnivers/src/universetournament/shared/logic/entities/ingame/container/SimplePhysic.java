/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.logic.entities.ingame.container;

import universetournament.shared.data.proto.PhysicConfig;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public class SimplePhysic extends SimpleTransformation implements PhysicContainer{

    private final float radius, mass;
    private Vec3 vel;

    public SimplePhysic(PhysicConfig pconf) {
        this(pconf, new Vec3());
    }

    public SimplePhysic(PhysicConfig pconf, Vec3 v) {
        this(pconf.getRadius(), pconf.getMass(), v);
    }

    public SimplePhysic(float radius, float mass) {
        this(radius, mass, new Vec3());
    }

    public SimplePhysic(float radius, float mass, Vec3 v) {
        this.radius = radius;
        this.mass = mass;
        vel = v;
    }

    public Vec3 getVelocity() {
        return vel;
    }

    public void setVelocity(Vec3 newvel) {
        vel = newvel;
    }

    public float getRadius() {
        return radius;
    }

    public float getMass() {
        return mass;
    }

}
