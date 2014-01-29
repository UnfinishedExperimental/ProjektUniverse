/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.shared.logic.entities.ingame.container;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.shared.data.proto.PhysicConfig;

/**
 *
 * @author dheinrich
 */
public class SimplePhysic extends SimpleTransformation implements PhysicContainer{

    private final float radius, mass;
    private ImmutableVector<Vector3> vel;

    public SimplePhysic(PhysicConfig pconf) {
        this(pconf, new Vector3());
    }

    public SimplePhysic(PhysicConfig pconf, ImmutableVector<Vector3> v) {
        this(pconf.getRadius(), pconf.getMass(), v);
    }

    public SimplePhysic(float radius, float mass) {
        this(radius, mass, new Vector3());
    }

    public SimplePhysic(float radius, float mass, ImmutableVector<Vector3> v) {
        this.radius = radius;
        this.mass = mass;
        vel = v;
    }

    public ImmutableVector<Vector3> getVelocity() {
        return vel;
    }

    public void setVelocity(ImmutableVector<Vector3> newvel) {
        vel = newvel;
    }

    public float getRadius() {
        return radius;
    }

    public float getMass() {
        return mass;
    }

}
