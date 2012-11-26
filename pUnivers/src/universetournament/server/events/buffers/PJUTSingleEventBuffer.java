package universetournament.server.events.buffers;

import universetournament.server.communication.PJUTConnManager;
import universetournament.shared.events.buffers.PJUTEventBuffer;
import universetournament.shared.events.GameEvent;

/**
 * Event-Puffer für eine einzelne Client-Verbindung, definiert über deren ID.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTSingleEventBuffer extends PJUTEventBuffer
{
	private byte id;
	
	/**
	 * Erzeugt einen Puffer, mit der Ziel-ID 0.
	 */
	public PJUTSingleEventBuffer()
	{
		this((byte)0);
	}
	
	/**
	 * Erzeugt einen Puffer, mit der angegebenen Ziel-ID.
	 * Akzeptiert nur positive Werte, negative Werte werden zu 0
	 * umgesetzt.
	 * Der Wert 0 gilt immer als "deaktiviert".
	 * 
	 * @param id ID der Ziel-Verbindung
	 */
	public PJUTSingleEventBuffer(byte id)
	{
		setId(id);
	}

	/**
	 * Setzt die ID der Ziel-Verbindung des Puffers, der beim flush()
	 * die Events übergeben werden.
	 * Werden negative Werte übergeben, werden diese auf 0 umgesetzt.
	 * 
	 * @param id id ID der Ziel-Verbindung
	 */
	public void setId(byte id)
	{
		if(id > 0)
		{
			this.id = id;
		}
		else
		{
			this.id = 0;
		}
	}

	/**
	 * @return ID der Ziel-Verbindung
	 */
	public byte getId()
	{
		return id;
	}
	
	@Override
	public void flush()
	{
		GameEvent[] events = toArrayAndClear();
		if(events.length > 0)
		{
			PJUTConnManager.getInstance().sendToID(events, id);
		}
	}
}
