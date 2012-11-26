package pjut.server.communication.events;

import pjut.shared.communication.events.PJUTEventReceiver;

/**
 * Ein PJUTEventReceiver für den Server, lediglich erweitert um die ID
 * der zugehörigen Verbindung, beziehungsweise des Clients.
 * 
 * @author sylence
 *
 */
public class PJUTServerReceiver extends PJUTEventReceiver
{
	private byte id;
	private boolean started;
	
	/**
	 * Erzeugt einen inaktiven Receiver ohne überwachte Liste mit der ID 0.
	 */
	public PJUTServerReceiver()
	{
		id = 0;
		started = false;
	}

	/**
	 * Setzt die ID des Sockets, der hiermit indirekt verbunden ist.
	 * ID muss positiv sein, nur 0 bedeutet standardmäßig "keine ID vergeben".
	 * 
	 * @param id neue ID des Clients
	 */
	public void setClientId(byte id)
	{
		this.id = id;
	}
	
	/**
	 * Liefert die ID des Sockets, der hiermit indirekt verbunden ist.
	 * 0 bedeutet standardmäßig "keine ID vergeben".
	 * 
	 * @return id des verbundenen Clients
	 */
	public byte getClientId()
	{
		return id;
	}

	/**
	 * @param started ob "gestartet"-Flag gesetzt sein soll
	 */
	public void setStarted(boolean started)
	{
		this.started = started;
	}

	/**
	 * @return ob "gestartet"-Flag gesetzt ist
	 */
	public boolean isStarted()
	{
		return started;
	}
}
