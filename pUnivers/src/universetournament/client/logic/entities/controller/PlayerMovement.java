/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.logic.entities.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.controller.MoveData;
import universetournament.shared.logic.entities.ingame.controller.MovementController;
import universetournament.shared.util.math.ModelMatrix;
import universetournament.shared.util.math.Vec3;
import universetournament.shared.util.math.Vector;

/**
 *
 * @author dheinrich
 */
public class PlayerMovement extends MovementController
{
    private static final Logger logger = Logger.getLogger(PlayerMovement.class.getName());
    private Vec3 acceleration, rotation;
    private PhysicContainer pcon;

    public PlayerMovement(ShipEntity ship) {
        super(ship);
        acceleration = new Vec3();
        pcon = getEntity().getTransformation();
        rotation = new Vec3();
    }

    @Override
    public ShipEntity getEntity() {
        return (ShipEntity) super.getEntity();
    }

    public Vec3 getAcceleration() {
        return acceleration;
    }

    @Override
    protected void handleMoveData(MoveData mdata) {
    	if(mdata.isOverride())
    	{
	        logger.log(Level.FINER, "Client handles Movement Event");
	        pcon.setWorldPosition(mdata.getTranslation());
	        pcon.rotateEuler(mdata.getRotation());
	        pcon.setVelocity(mdata.getVelocity());
    	}
    }
    
    public void setRotation(Vec3 rotation)
    {
    	this.rotation = rotation;
    }

    public void refresh(float timeDiff) {
        timeDiff /= 1000000000f;

        ModelMatrix mmatrix = pcon.getModelMatrix();
        acceleration.mult(timeDiff, acceleration);
        Vector accrel = mmatrix.getMinor(3, 3).mult(acceleration);
        
        Vec3 vel = pcon.getVelocity();
        vel.add(accrel, vel);

        double speed = vel.lenght();
        double maxspeed = getEntity().getConfiguration().getMaxSpeed();
        if (speed > maxspeed)
            vel.normalize(vel).mult(maxspeed, vel);

        pcon.shiftWorldPosition(vel.mult(timeDiff));
        Vec3 newpos = mmatrix.getTranslation();

        MoveData md = new MoveData(newpos, rotation, vel);
        EntityEvent event = new EntityEvent(getEntity().getId(),
        		WorldEntity.SubEvents.MOVE, md);
        
        //Daten versenden
        PJUTCliConnector.getInstance().getBuffer().addEvent(event);
        
        //temporäre Daten verwerfen
        acceleration = new Vec3();
        rotation  = new Vec3();
    }
}
