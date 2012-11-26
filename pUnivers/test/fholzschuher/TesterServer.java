package fholzschuher;

import java.util.Map.Entry;

import universetournament.server.communication.PJUTConnectionListener;
import universetournament.server.communication.PJUTServerSocket;
import universetournament.server.communication.events.PJUTReceiverManager;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.events.MainType;
import universetournament.shared.events.GameEvent;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.testing.dheinrich.MoveData;

/**
 * 
 * @author Florian Holzschuher
 *
 */
public class TesterServer implements PJUTEventHandler, PJUTConnectionListener
{
    @Override
    public void handleEvent(GameEvent event)
    {
    	UTEntityManager.getInstance().handleEvent(event);
        PJUTEventBuffManager.getInstance().sendToAll(event);
    }

    @Override
    public void connectionAdded(PJUTServerSocket conn)
    {
        PJUTEventBuffManager.getInstance().createBuffer(conn.getId());
        PJUTReceiverManager.getInstance().createReceiver(conn);
        PJUTReceiverManager.getInstance().setHandlerFor(conn.getId(), this,
            MainType.TYPE_ENTITY);

        PJUTReceiverManager.getInstance().startReceiver(conn.getId());

        //TODO: sendtoallexceptone überprüfen!
        TestEvent te = null;
        
        for(Entry<Short, SquareEntity> sqe :
        	SquareManager.getInstance().getSquares())
        {
	        te = new TestEvent(sqe.getValue().getId(),
	        		Entity.SubEvents.CREATE, null);
	        PJUTEventBuffManager.getInstance().sendToId(conn.getId(), te);
	        
	        te = new TestEvent(sqe.getValue().getId(),
	        		TestEvent.SubEvents.MOVE,
	        		new MoveData(sqe.getValue().getX(),
	        				sqe.getValue().getY()));
	        PJUTEventBuffManager.getInstance().sendToId(conn.getId(), te);
        }

        SquareManager.getInstance().createSquare(conn.getId());
        
        te = new TestEvent(conn.getId(),
        		TestEvent.SubEvents.CONNECTED, null);
        conn.send(te);
    }

    @Override
    public void connectionClosed(byte id)
    {
        PJUTReceiverManager.getInstance().removeReceiver(id);
        PJUTEventBuffManager.getInstance().removeBuffer(id);
        
        SquareManager.getInstance().removeSquare(id);
    }
}
