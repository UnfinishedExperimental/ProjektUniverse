package pjut.server.events.buffers;

import pjut.server.communication.PJUTConnManager;
import pjut.shared.events.PJUTEvent;
import pjut.shared.events.buffers.PJUTEventBuffer;

/**
 * Event-Puffer, der Events puffert, die an alle mit dem Server verbundenen
 * Clients verschickt werden sollen.
 * 
 * @author sylence
 *
 */
public class PJUTGlobalEventBuffer extends PJUTEventBuffer
{	
	@Override
	public void flush()
	{
		PJUTEvent[] events = toArrayAndClear();
		if(events.length > 0)
		{
			PJUTConnManager.getInstance().sendToAll(events);
		}
	}
}
