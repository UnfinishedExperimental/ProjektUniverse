package pjut.testing;

import pjut.server.communication.PJUTConnectionListener;
import pjut.server.communication.PJUTServerSocket;
import pjut.server.communication.events.PJUTReceiverManager;
import pjut.server.events.buffers.PJUTEventBuffManager;
import pjut.shared.events.PJUTEvent;
import pjut.shared.logic.PJUTEventHandler;

public class TesterServer implements PJUTEventHandler, PJUTConnectionListener
{
	@Override
	public void handleEvent(PJUTEvent event)
	{
		PJUTEventBuffManager.getInstance().sendToAll(event);
	}

	@Override
	public void connectionAdded(PJUTServerSocket conn)
	{
		PJUTEventBuffManager.getInstance().createBuffer(conn.getId());
		PJUTReceiverManager.getInstance().createReceiver(conn);
		PJUTReceiverManager.getInstance().setHandlerFor(conn.getId(), this, (byte)1);
		PJUTReceiverManager.getInstance().startReceiver(conn.getId());
		
		TesterEvent te = new TesterEvent(conn.getId(), (byte)0, (byte)0);
		te.setSubType((byte)1);
		conn.send(te);
	}

	@Override
	public void connectionClosed(byte id)
	{
		PJUTReceiverManager.getInstance().removeReceiver(id);
		PJUTEventBuffManager.getInstance().removeBuffer(id);
	}

}
