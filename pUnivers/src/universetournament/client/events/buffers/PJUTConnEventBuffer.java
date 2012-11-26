package universetournament.client.events.buffers;

import universetournament.shared.communication.PJUTSocket;
import universetournament.shared.events.buffers.PJUTEventBuffer;
import universetournament.shared.events.GameEvent;

/**
 * Event-Puffer für eine einzelne Verbindung, die direkt als PJUTSocket
 * zugeordnet wird.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTConnEventBuffer extends PJUTEventBuffer
{
	private PJUTSocket socket;
	
	/**
	 * Erzeugt einen Puffer ohne Ziel-Socket.
	 */
	public PJUTConnEventBuffer()
	{
		this(null);
	}
	
	/**
	 * Erzeugt einen Puffer mit dem angegeben Ziel-Socket.
	 * Ist dieser null, wird der Puffer beim flush() geleert, aber nichts
	 * verschickt.
	 * 
	 * @param socket Socket, über den Events verschickt werden sollen
	 */
	public PJUTConnEventBuffer(PJUTSocket socket)
	{
		this.socket = socket;
	}

	/**
	 * Setzt den Ziel-Socket für Event-Pakete.
	 * Ist dieser null, wird der Puffer beim flush() geleert, aber nichts
	 * verschickt.
	 * 
	 * @param socket Socket, über den Events verschickt werden sollen
	 */
	public void setSocket(PJUTSocket socket)
	{
		this.socket = socket;
	}

	/**
	 * @return Socket, über den Events verschickt werden
	 */
	public PJUTSocket getSocket()
	{
		return socket;
	}
	
	@Override
	public void flush()
	{
		GameEvent[] events = this.toArrayAndClear();
		
		if(socket != null && events.length > 0)
		{
			socket.send(events);
		}
	}
}
