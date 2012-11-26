package pjut.server.events.buffers;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import pjut.shared.events.PJUTEvent;

/**
 * Verwaltung für einzelne Event-Puffer im Server, die als vereinfachende
 * Facade dient.
 * Den Globalen Puffer Verwaltet diese Klasse selbst.
 * Null-Werte werden generell von den Puffern nicht angenommen.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author sylence
 *
 */
public class PJUTEventBuffManager
{
	/**
	 * Anfängliche Größe der Buffer-Map.
	 */
	public static final int DEFAULT_INIT_CAP = 16;
	
	private static PJUTEventBuffManager instance;
	
	private PJUTGlobalEventBuffer global;
	private HashMap<Byte, PJUTSingleEventBuffer> buffers;
	
	/**
	 * @return vorhandene oder erstelle Instanz
	 */
	public static synchronized PJUTEventBuffManager getInstance()
	{
		if(instance == null)
		{
			instance = new PJUTEventBuffManager();
		}
		
		return instance;
	}
	
	private PJUTEventBuffManager()
	{
		global = new PJUTGlobalEventBuffer();
		buffers = new HashMap<Byte, PJUTSingleEventBuffer>(
				DEFAULT_INIT_CAP);
	}
	
	/**
	 * Schreibt das Event in den Puffer für alle Verbindungen.
	 * 
	 * @param e zu versendendes Event
	 */
	public void sendToAll(PJUTEvent e)
	{
		global.addEvent(e);
	}
	
	/**
	 * Schreibt das Event in alle Einzel-EventBuffer bis auf den mit der
	 * angegebenen ID.
	 * 
	 * @param id auszulassende ID
	 * @param e zu versendendes Event
	 */
	public void sendToAllExceptOne(byte id, PJUTEvent e)
	{
		Byte temp = 0;
		
		synchronized(buffers)
		{
			for(Entry<Byte, PJUTSingleEventBuffer> entry : buffers.entrySet())
			{
				temp = entry.getKey();
				
				if(temp != id)
				{
					sendToId(id, e);
				}
			}
		}
	}
	
	/**
	 * Schreibt das Event in alle Einzel-EventBuffer, außer denen, die in der
	 * Liste angegeben sind.
	 * 
	 * @param ids Liste der zu ignorierenden IDs
	 * @param e zu versendendes Event
	 */
	public void sendToAllExceptGroup(List<Byte> ids, PJUTEvent e)
	{
		Byte id = 0;
		
		synchronized(buffers)
		{
			for(Entry<Byte, PJUTSingleEventBuffer> entry : buffers.entrySet())
			{
				id = entry.getKey();
				
				if(!ids.contains(id))
				{
					sendToId(id, e);
				}
			}
		}
	}
	
	/**
	 * Schreibt das Event in alle Einzel-EventBuffer, die eine der in der Liste
	 * enthaltenen IDs besitzen.
	 * 
	 * @param ids Liste der Ziel-IDs
	 * @param e zu versendendes Event
	 */
	public void sendToGroup(List<Byte> ids, PJUTEvent e)
	{
		for(Byte id : ids)
		{
			sendToId(id, e);
		}
	}
	
	/**
	 * Schreibt das übergebene Event nur in den Puffer mit der übergebenen ID,
	 * falls vorhanden.
	 * 
	 * @param id ID des Puffers, bzw. seiner Verbindung
	 * @param e zu versendendes Event
	 */
	public void sendToId(byte id, PJUTEvent e)
	{
		PJUTSingleEventBuffer buffer = null;
		synchronized(buffers)
		{
			buffer = buffers.get(id);
		}
		if(buffer != null)
		{
			buffer.addEvent(e);
		}
	}
	
	/**
	 * Lässt alle verfügbaren Puffer leeren und ihren Inhalt versenden.
	 */
	public void flushAll()
	{
		synchronized(buffers)
		{
			for(Entry<Byte, PJUTSingleEventBuffer> e : buffers.entrySet())
			{
				e.getValue().flush();
			}
		}
		global.flush();
	}
	
	/**
	 * Leert alle Puffer, ohne etwas zu versenden.
	 */
	public void clearAll()
	{
		synchronized(buffers)
		{
			for(Entry<Byte, PJUTSingleEventBuffer> e : buffers.entrySet())
			{
				e.getValue().clear();
			}
		}
		global.clear();
	}
	
	/**
	 * Erzeugt einen Puffer für die angegebene ID, falls noch keiner
	 * existiert.
	 * Ingoriert Werte kleiner als 1.
	 * 
	 * @param id ID der Verbindung, für die der Puffer bestimmt ist
	 */
	public void createBuffer(byte id)
	{
		if(id > 0)
		{
			synchronized(buffers)
			{
				if(buffers.get(id) == null)
				{
					buffers.put(id, new PJUTSingleEventBuffer(id));
				}
			}
		}
	}
	
	/**
	 * Entfernt, falls vorhanden, den für die ID angelegten Puffer.
	 * Ingoriert Werte kleiner als 1.
	 * 
	 * @param id ID der Verbindung, für die der Puffer bestimmt war
	 */
	public void removeBuffer(byte id)
	{
		if(id > 0)
		{
			synchronized(buffers)
			{
				buffers.remove(id);
			}
		}
	}
}
