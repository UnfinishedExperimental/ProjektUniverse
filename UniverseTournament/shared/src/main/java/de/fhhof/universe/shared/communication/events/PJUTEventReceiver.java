package de.fhhof.universe.shared.communication.events;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.events.GameEvent;

/**
 * Receiver, der aus einer Liste eingehende Events abgreift und an die
 * entsprechenden registrierten Handler weiterleitet.
 * Arbeitet nur mit dem Basis-Typ der Events.
 * Wartet, wenn Liste leer ist auf ein notify() auf dieser.
 * Die Objekte in der Liste, die keine Events oder Arrays von Events
 * sind werden verworfen, damit der Puffer nicht überläuft.
 * 
 * @author Florian Holzschuher
 * 
 */
public class PJUTEventReceiver extends Thread
{
	/**
	 * Anfängliche Größe der Handler-Map.
	 */
	public static final int DEFAULT_INIT_CAP = 16;
	// Serializable-Liste aus dem Socket
	private LinkedList<Serializable> incoming;
	private final LinkedList<GameEvent> buffer;
	private final HashMap<MainType, PJUTEventHandler> eventHandlers;
	private boolean active;

	/**
	 * Erzeugt einen neuen EventReceiver und setzt das Aktivierungsflag.
	 * Er benötigt noch eine zu überwachende Pufferliste um zu funktionieren.
	 */
	public PJUTEventReceiver()
	{
		incoming = null;
		active = true;
		buffer = new LinkedList<GameEvent>();

		eventHandlers = new HashMap<MainType, PJUTEventHandler>(
				DEFAULT_INIT_CAP);
	}

	@Override
	/**
	 * So lange aktiviert, Events aus der Liste abgreifen und an die Handler
	 * weiterleiten.
	 * Unbekannte Objekte in der überwachten Liste werden dabei verworfen.
	 */
	public void run()
	{
		while (active && incoming != null)
		{
			/*
			 * passende Objekte vor Bearbeitung in Puffer kopieren, da sonst die
			 * Liste zu länge blockiert wäre.
			 */
			copyToBuffer();

			// Events bearbeiten lassen
			distributeEvents();

			// auf Benachrichtigung warten, wenn Liste leer
			if (incoming != null)
			{
				if (incoming.size() > 0)
				{
					continue;
				}
				else
				{
					waitForEvents();
				}
			}
		}
	}

	private void copyToBuffer()
	{
		synchronized (incoming)
		{
			int count = incoming.size();
			while (count-- > 0)
			{
				Serializable temp = incoming.pop();

				// Listen oder Einzelevents erkennen
				if (temp instanceof GameEvent[])
				{
					GameEvent[] arr = (GameEvent[]) temp;
					for (GameEvent event : arr)
					{
						buffer.add(event);
					}
				}
				else if (temp instanceof GameEvent)
				{
					buffer.add((GameEvent) temp);
				}
				else
				{
					System.err.println("EventReceiver verwirft"
							+ " unbekanntes Objekt: " + temp);
				}
			}
		}
	}

	private void waitForEvents()
	{
		synchronized (incoming)
		{
			try
			{
				incoming.wait();
			}
			catch (InterruptedException e)
			{
				System.err.println("EventReceiver wurde unterbrochen.");
				return;
			}
		}
	}

	private void distributeEvents()
	{
		for (GameEvent event : buffer)
		{
			if(event.getMain() != null)
			{
				synchronized (eventHandlers)
				{
					PJUTEventHandler handler = eventHandlers.get(
							event.getMain());
	
					if (handler != null)
					{
						handler.handleEvent(event);
					}
				}
			}
		}

		buffer.clear();
	}

	/**
	 * Setzt den Aktivierungs-Flag der Thread-Schleife.
	 * Wird die Schleife deaktiviert, wird ein notify() auf die Liste
	 * ausgeführt (falls vorhanden), damit Rest-Events abgebaut und die
	 * Schleife aktiviert werden
	 * (nur falls die Schleife vorher aktiv war).
	 * 
	 * @param active ob Schleife aktiv sein soll oder nicht.
	 */
	public void setActive(boolean active)
	{
		if (this.active && !active && incoming != null)
		{
			synchronized (incoming)
			{
				incoming.notify();
			}
		}

		this.active = active;
	}

	/**
	 * @return ob Aktivierungs-Flag gesetzt.
	 */
	public boolean isActive()
	{
		return active;
	}

	/**
	 * Setzt, welche Liste überwacht werden soll und führt bei der Ersetzung
	 * noch ein notify() auf die alte Liste aus (falls vorhanden), um die
	 * Schleife in Gang zu bringen.
	 * Wird null übergeben, wird die Schleife deaktiviert.
	 * 
	 * @param list neue zu überwachende Liste.
	 */
	public void setList(LinkedList<Serializable> list)
	{
		List<Serializable> old = incoming;

		incoming = list;

		if (list == null)
		{
			active = false;
		}

		if (old != null)
		{
			synchronized (old)
			{
				old.notify();
			}
		}
	}

	/**
	 * @return aktuell überwachte Liste an Objekten.
	 */
	public List<Serializable> getList()
	{
		return incoming;
	}

	/**
	 * Setzt für einen angegebenen Basis-Typ einen Handler.
	 * Wird null als Handler übergeben, wird der für den Typ registrierte
	 * Handler, falls vorhanden, entfernt.
	 * Null-Typen werden ignoriert.
	 * 
	 * @param type typ, für den der Handler registriert wird
	 * @param handler handler, an den die Events geschickt werden sollen
	 */
	public void setHandler(MainType type, PJUTEventHandler handler)
	{
		if(type != null)
		{
			synchronized (eventHandlers)
			{
				if (handler == null)
				{
					eventHandlers.remove(type);
				}
				else
				{
					eventHandlers.put(type, handler);
				}
			}
		}
	}

	/**
	 * Entfernt alle registrierten Handler.
	 */
	public void clearHandlers()
	{
		synchronized (eventHandlers)
		{
			eventHandlers.clear();
		}
	}

	/**
	 * @return Map mit allen registrierten Handlern
	 */
	public Map<MainType, PJUTEventHandler> getHandlers()
	{
		return eventHandlers;
	}
}
