/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.shared.logic.entities.ingame.controller;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import java.io.Serializable;

/**
 *
 * @author dheinrich
 */
public class MoveData implements Serializable{
    private final ImmutableVector<Vector3> translation;
    private final ImmutableVector<Vector3> rotation;
    private final ImmutableVector<Vector3> velocity;
    private final long timestamp;
    private boolean override;

    public MoveData(ImmutableVector<Vector3> translation, ImmutableVector<Vector3> rotation, ImmutableVector<Vector3> velocity, long timestamp) {
        this.translation = translation;
        this.rotation = rotation;
        this.velocity = velocity;
        this.timestamp = timestamp;
    }

    public ImmutableVector<Vector3> getRotation() {
        return rotation;
    }

    public ImmutableVector<Vector3> getTranslation() {
        return translation;
    }

    public ImmutableVector<Vector3> getVelocity() {
        return velocity;
    }
    
    public void setOverride(boolean override) {
    	this.override = override;
    }
    
    public boolean isOverride() {
    	return override;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
