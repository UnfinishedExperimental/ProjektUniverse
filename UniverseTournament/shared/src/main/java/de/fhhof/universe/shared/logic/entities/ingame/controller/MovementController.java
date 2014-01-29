/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.shared.logic.entities.ingame.controller;

import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.SubType;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.util.EntitySubController;

/**
 *
 * @author dheinrich
 */
public abstract class MovementController extends 
	EntitySubController<WorldEntity<?>> implements PJUTTimedRefreshable,
	PJUTEventHandler{
    private WorldEntity<?> ent;

    public MovementController(WorldEntity<?> ent) {
    	super(ent);
        this.ent = ent;
    }

    public void handleEvent(GameEvent event) {
        if(event.getSub() != WorldEntity.SubEvents.MOVE)
            return;
        handleMoveData((MoveData) event.getData());  
    }

    public WorldEntity<?> getEntity() {
        return ent;
    }

    protected abstract void handleMoveData(MoveData mdata);
    
	@Override
	public SubType[] getTypes()
	{
		SubType st[] = {WorldEntity.SubEvents.MOVE};
		return st;
	}
}
