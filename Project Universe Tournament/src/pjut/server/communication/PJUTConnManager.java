/*
 * 
 */
package pjut.server.communication;

import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Verwaltet die Verbindungen zu Clients über ihre IDs, funktioniert nach dem
 * singleton pattern.
 * Es lässt sich ein Listener definieren, welcher benachrichtigt wird, wenn
 * eine neue Verbindung hinzugefügt wurde.
 * Die IDs sind immer positive Werte, 0 bedeutet "nicht gesetzt".
 * Da die ID nur ein Byte ist und Java kein unsigned kennt, können maximal
 * 127 Verbindungen verwaltet werden.
 * 
 * @author sylence
 *
 */
public class PJUTConnManager implements PJUTServerConnMonitor
{
	/**
	 * Anfängliche Größe der Verbindungs-Map.
	 */
	public static final int DEFAULT_INIT_CAP = 16;
	
	private static PJUTConnManager instance;
	
	private HashMap<Byte, PJUTServerSocket> connections;
	
	//hier wird verwaltet, welche IDs schon vergeben wurden
	private BitSet idSet;
	
	private PJUTConnectionListener listener;
	
	/**
	 * Liefert entweder die vorhandene Instanz der Klasse, oder erzeugt ein
	 * neues Objekt und gibt dieses dann zurück.
	 * Dieses hat noch keinen Listener und keine Verbindungen.
	 * 
	 * @return Instanz der Klasse
	 */
	public static synchronized PJUTConnManager getInstance()
	{
		if(instance == null)
		{
			instance = new PJUTConnManager();
		}
		
		return instance;
	}
	
	private PJUTConnManager()
	{
		connections = new HashMap<Byte, PJUTServerSocket>(
				DEFAULT_INIT_CAP);
		idSet = new BitSet();
		
		listener = null;
	}
	
	/**
	 * Setzt den Verbindungs-Listener.
	 * Akzeptiert auch null, dann werden keine Benachrichtigungen mehr
	 * versandt.
	 * 
	 * @param listener neuer Listener oder null
	 */
	public void setListener(PJUTConnectionListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * @return aktuell eingetragener Verbindungs-Listener.
	 */
	public PJUTConnectionListener getListener()
	{
		return listener;
	}
	
	/**
	 * Fügt den aktiven Verbindungen eine neue Hinzu und ordnet ihr eine ID zu.
	 * Ignoriert null-Referenzen.
	 * Falls vorhanden, wird der Verbindungs-Listener benachrichtigt, bevor die
	 * Verbindung der internen Liste hinzugefügt wird. Dies soll ihm
	 * ermöglichen ein erstes Initialisierungsobjekt zu senden.
	 * Schlägt fehl, wenn schon zu viele Verbindungen verwaltet werden, null
	 * übergeben wird oder ein interner Fehler auftritt.
	 * 
	 * @param conn hinzuzufügendes Verbindungs-Objekt
	 * @return Erfolg
	 */
	public boolean addConnection(PJUTServerSocket conn)
	{
		boolean success = false;
		
		if(conn != null)
		{
			byte newId = 0;
			
			synchronized(connections)
			{
				//nächste freie ID vergeben
				//0 ist für "keine ID vergeben" reserviert
				int next = idSet.nextClearBit(1);
				
				//gegen Überlauf des Bytes
				if(next <= 127)
				{
					newId = (byte)next;
					conn.setId(newId);
					idSet.set(conn.getId());
				}
			}
			
			//zuerst Listener benachrichtigen
			if(newId > 0)
			{
				success = true;
				if(listener != null)
				{
					listener.connectionAdded(conn);
				}
				
				//bei Fehlern benachrichtigen lassen
				conn.setServMonitor(this);
				
				synchronized(connections)
				{
					connections.put(newId, conn);
				}
			}
			
		}
		
		return success;
	}
	
	/**
	 * Entfernt, falls vorhanden, die Verbindung mit der entsprechenden ID
	 * aus der Liste.
	 * Benachrichtigt, falls vorhanden, den entsprechenden Listener.
	 * Existiert die entsprechende Verbindung nicht, tritt kein Fehler auf.
	 * 
	 * @param id ID der zu entferndenden Verbindung
	 */
	public void removeConnection(byte id)
	{
		PJUTServerSocket connection = null;
		
		synchronized(connections)
		{
			connection = connections.get(id);
			connections.remove(id);
		}
		
		if(connection != null && listener != null)
		{
			idSet.clear(id);
			listener.connectionClosed(id);
		}
	}
	
	/**
	 * @return Liste aller Verbindungen
	 */
	public Map<Byte, PJUTServerSocket> getConnections()
	{
		return connections;
	}
	
	/**
	 * Gibt die Verbindung mit der angegebenen ID zurück oder null, wenn diese
	 * nicht existiert.
	 * 
	 * @param id ID der gesuchten Verbindung
	 * @return zur ID passende Verbindung
	 */
	public PJUTServerSocket getConnection(byte id)
	{
		PJUTServerSocket s = null;
		
		synchronized(connections)
		{
			s = connections.get(id);
		}
		
		return s;
	}
	
	/**
	 * Sendet, wenn möglich, über die Verbindung hinter der der Client
	 * mit der angegebenen ID lauscht, das übergebene serialisierbare Objekt.
	 * Sollte nicht mit null aufgerufen werden.
	 * 
	 * @param o zu sendendes Objekt
	 * @param id ID des Ziel-Clients
	 * @return ob passende Verbindung gefunden wurde.
	 */
	public boolean sendToID(Serializable o, byte id)
	{
		boolean found = false;
		
		//Zugriff sperren, damit Indizes nicht wandern
		synchronized(connections)
		{
			PJUTServerSocket s = connections.get(id);
		
			if(s != null)
			{
				s.send(o);
				found = true;
			}
		}
			
		return found;
	}
	
	/**
	 * Serialisierbares Objekt über alle vorhandenen Verbindungen verschicken.
	 * Sollte nicht mit null aufgerufen werden.
	 * 
	 * @param o zu sendendes Objekt
	 */
	public void sendToAll(Serializable o)
	{
		synchronized(connections)
		{
			for(Entry<Byte, PJUTServerSocket> s : connections.entrySet())
			{
				s.getValue().send(o);
			}
		}
	}

	@Override
	public void connectionLost(byte id)
	{
		removeConnection(id);
	}
}
