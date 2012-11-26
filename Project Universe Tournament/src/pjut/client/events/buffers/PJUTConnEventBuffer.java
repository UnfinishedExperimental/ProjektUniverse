package pjut.client.events.buffers;

import pjut.shared.communication.PJUTSocket;
import pjut.shared.events.PJUTEvent;
import pjut.shared.events.buffers.PJUTEventBuffer;

/**
 * Event-Puffer für eine einzelne Verbindung, die direkt als PJUTSocket
 * zugeordnet wird.
 * 
 * @author sylence
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
		PJUTEvent[] events = this.toArrayAndClear();
		
		if(socket != null && events.length > 0)
		{
			socket.send(events);
		}
	}
}
