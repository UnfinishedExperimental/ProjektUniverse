package de.fhhof.universe.shared.events.util;

import java.util.ArrayList;

import de.fhhof.universe.shared.events.GameEvent;


/**
 * EventHandler, bei dem ankommende Events direkt in einen Puffer geschrieben
 * werden.
 * Es wird davon ausgegangen, dass der Puffer geleert wird, um Überläufe zu
 * verhindern. Auf ihn sollte immer synchronized zugegriffen werden.
 * 
 * @author Florian Holzschuher
 */
public abstract class PJUTBufferedHandler implements PJUTEventHandler
{
	/**
	 * Liste, in der ankommende Events gepuffert werden
	 */
	protected final ArrayList<GameEvent> buffer;
	
	/**
	 * Erstellt den Handler mit einer ArrayList als Puffer.
	 */
	public PJUTBufferedHandler()
	{
		buffer = new ArrayList<GameEvent>();
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		synchronized(buffer)
		{
			buffer.add(event);
		}
	}
}
