package fholzschuher;

import java.io.Serializable;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.events.SubType;

/**
 * 
 * @author Florian Holzschuher
 *
 */
public class TestEvent extends EntityEvent
{
    public enum SubEvents implements SubType
    {
        CONNECTED, MOVE;
    }

    public TestEvent(short id, SubType sub, Serializable data)
    {
        super(id, sub, data);
    }
}
