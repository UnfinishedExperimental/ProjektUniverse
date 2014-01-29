/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.client.rendering.particles;

import darwin.util.math.base.vector.Vector3;


/**
 *
 * @author dheinrich
 */
public class Particle implements Sphere{
    private Vector3 position, velocity;
    private float scale, life;
    private double cameradist;

    public Particle(Vector3 position, Vector3 velocity, float scale, float life) {
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

    public Vector3 getPosition() {
        return position;
    }

    public float getScale() {
        return scale;
    }

    public Vector3 getVelocity() {
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

    public void setPosition(Vector3 newpos) {
        position = newpos;
    }

    public void shiftPosition(Vector3 p) {
       position.add(p);
    }

    public void setVelocity(Vector3 newvel) {
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
