/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.particles;

import universetournament.shared.util.math.Vec3;
import universetournament.testing.dheinrich.collision.Sphere;

/**
 *
 * @author dheinrich
 */
public class Particle implements Sphere{
    private Vec3 position, velocity;
    private float scale, life;
    private double cameradist;

    public Particle(Vec3 position, Vec3 velocity, float scale, float life) {
        this.position = position;
        this.velocity = velocity;
        this.scale = scale;
        this.life = life;
    }

    public void setLife(float life) {
        this.life = life;
    }

    public float getLife() {
        return life;
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public Vec3 getVelocity() {
        return velocity;
    }

    public double getCameradist() {
        return cameradist;
    }

    public void setCameradist(double cameradist) {
        this.cameradist = cameradist;
    }

    public void preAdvance(float delta) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setPosition(Vec3 newpos) {
        position = newpos;
    }

    public void shiftPosition(Vec3 p) {
       position.add(p, position);
    }

    public void setVelocity(Vec3 newvel) {
        velocity = newvel;
    }

    public float getRadius() {
        return 0.1f;
    }

    public float getMass() {
        return 0.001f;
    }

    public void advance(float delta) {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

}
