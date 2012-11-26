package universetournament.server.events.buffers;

import universetournament.server.communication.PJUTConnManager;
import universetournament.shared.events.buffers.PJUTEventBuffer;
import universetournament.shared.events.GameEvent;

/**
 * Event-Puffer, der Events puffert, die an alle mit dem Server verbundenen
 * Clients verschickt werden sollen.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTGlobalEventBuffer extends PJUTEventBuffer
{	
	@Override
	public void flush()
	{
		GameEvent[] events = toArrayAndClear();
		if(events.length > 0)
		{
			PJUTConnManager.getInstance().sendToAll(events);
		}
	}
}
