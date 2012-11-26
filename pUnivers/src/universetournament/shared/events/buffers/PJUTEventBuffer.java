package universetournament.shared.events.buffers;

import java.util.ArrayList;
import universetournament.shared.events.GameEvent;


/**
 * Abstrakte Oberklasse für Event-Puffer mit einer internen Pufferliste.
 * Über die flush()-Methode sollen in den Unterklassen die Events gebündelt
 * an die jeweiligen Ziele weitergeleitet werden.
 * 
 * @author Florian Holzschuher
 *
 */
public abstract class PJUTEventBuffer
{
	private final ArrayList<GameEvent> buffer;
	
	/**
	 * Erzeugt den Puffer mit der internen, noch leeren, Pufferliste.
	 */
	public PJUTEventBuffer()
	{
		buffer = new ArrayList<GameEvent>();
	}
	
	/**
	 * Fügt dem Puffer ein Event hinzu, ignoriert null.
	 * 
	 * @param event zu versendendes Event
	 */
	public void addEvent(GameEvent event)
	{
		if(event != null)
		{
			synchronized(buffer)
			{
				buffer.add(event);
			}
		}
	}
	
	/**
	 * Leert den internen Puffer, ohne die Evente zu versenden.
	 */
	public void clear()
	{
		synchronized(buffer)
		{
			buffer.clear();
		}
	}
	
	/**
	 * Erzeugt ein Array aus den gepufferten Events und leert den Puffer.
	 * Ist der Puffer leer wird ein Array der Länge 0 erzeugt.
	 * 
	 * @return Array der gepufferten Events
	 */
	protected GameEvent[] toArrayAndClear()
	{
		GameEvent[] events = null;
		
		synchronized(buffer)
		{
			events = new GameEvent[buffer.size()];
			buffer.toArray(events);
			buffer.clear();
		}
		
		return events;
	}
	
	/**
	 * Lässt die gepufferten Events gebündelt weiterleiten und den internen
	 * Puffer leeren.
	 */
	public abstract void flush();
}
