package pjut.shared.logic;

import java.util.ArrayList;

import pjut.shared.events.PJUTEvent;

/**
 * EventHandler, bei dem ankommende Events direkt in einen Puffer geschrieben
 * werden.
 * Es wird davon ausgegangen, dass der Puffer geleert wird, um Überläufe zu
 * verhindern. Auf ihn sollte immer synchronized zugegriffen werden.
 * 
 * @author sylence
 */
public abstract class PJUTBufferedHandler
	implements PJUTEventHandler
{
	/**
	 * Liste, in der ankommende Events gepuffert werden
	 */
	protected ArrayList<PJUTEvent> buffer;
	
	/**
	 * Erstellt den Handler mit einer ArrayList als Puffer.
	 */
	public PJUTBufferedHandler()
	{
		buffer = new ArrayList<PJUTEvent>();
	}
	
	@Override
	public void handleEvent(PJUTEvent event)
	{
		synchronized(buffer)
		{
			buffer.add(event);
		}
	}
}
