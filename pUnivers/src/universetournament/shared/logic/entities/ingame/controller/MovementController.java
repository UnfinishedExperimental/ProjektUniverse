/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.logic.entities.ingame.controller;

import universetournament.shared.events.*;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.util.EntitySubController;

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
