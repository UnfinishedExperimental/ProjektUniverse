/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.logic.entities.ingame.controller;

import java.io.Serializable;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public class MoveData implements Serializable{
    private Vec3 translation;
    private Vec3 rotation;
    private Vec3 velocity;
    private long timestamp;
    private boolean override;

    public MoveData(Vec3 translation, Vec3 rotation, Vec3 velocity) {
        this.translation = translation;
        this.rotation = rotation;
        this.velocity = velocity;
        override = false;
    }

    public Vec3 getRotation() {
        return rotation;
    }

    public Vec3 getTranslation() {
        return translation;
    }

    public Vec3 getVelocity() {
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

    //optimized serialization specific for Univers Tournament requirements
//    private void writeObject(java.io.ObjectOutputStream out)
//            throws IOException {
//        float[] coords = translation.getCoords();
//        out.writeFloat(coords[0]);
//        out.writeFloat(coords[2]);
//
//        coords = velocity.getCoords();
//        out.writeFloat(coords[0]);
//        out.writeFloat(coords[2]);
//
//        out.writeFloat(rotation.getCoords()[1]);
//
//        out.writeLong(timestamp);
//    }
//
//    private void readObject(java.io.ObjectInputStream in)
//            throws IOException, ClassNotFoundException {
//        translation = new Vec3f(in.readFloat(), 0, in.readFloat());
//        velocity = new Vec3f(in.readFloat(), 0, in.readFloat());
//        rotation = new Vec3f(0, in.readFloat(), 0);
//        timestamp = in.readLong();
//    }

}
