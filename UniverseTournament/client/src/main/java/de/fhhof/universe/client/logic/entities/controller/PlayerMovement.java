/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.logic.entities.controller;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector;
import darwin.util.math.base.vector.Vector3;
import darwin.util.math.composits.ModelMatrix;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.fhhof.universe.client.communication.PJUTCliConnector;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.ingame.controller.MoveData;
import de.fhhof.universe.shared.logic.entities.ingame.controller.MovementController;

/**
 *
 * @author dheinrich
 */
public class PlayerMovement extends MovementController
{
    private static final Logger logger = Logger.getLogger(PlayerMovement.class.getName());
    private Vector3 acceleration, rotation;
    private PhysicContainer pcon;

    public PlayerMovement(ShipEntity ship) {
        super(ship);
        acceleration = new Vector3();
        pcon = getEntity().getTransformation();
        rotation = new Vector3();
    }

    @Override
    public ShipEntity getEntity() {
        return (ShipEntity) super.getEntity();
    }

    public Vector3 getAcceleration() {
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
    
    public void setRotation(ImmutableVector<Vector3> rotation)
    {
    	this.rotation = rotation.clone();
    }

    public void refresh(float timeDiff) {
        timeDiff /= 1000000000f;

        ModelMatrix mmatrix = pcon.getModelMatrix();
        acceleration = acceleration.clone().mul(timeDiff);
        Vector accrel = mmatrix.getMinor(3, 3).mult(acceleration);
        
        Vector3 vel = pcon.getVelocity().clone();
        vel.add(accrel);

        double speed = vel.length();
        double maxspeed = getEntity().getConfiguration().getMaxSpeed();
        if (speed > maxspeed)
            vel.normalize().mul((float) maxspeed);

        pcon.shiftWorldPosition(vel.clone().mul(timeDiff));
        Vector3 newpos = mmatrix.getTranslation();

        MoveData md = new MoveData(newpos, rotation, vel, 0);
        EntityEvent event = new EntityEvent(getEntity().getId(),
        		WorldEntity.SubEvents.MOVE, md);
        
        //Daten versenden
        PJUTCliConnector.getInstance().getBuffer().addEvent(event);
        
        //temporäre Daten verwerfen
        acceleration = new Vector3();
        rotation  = new Vector3();
    }
}
