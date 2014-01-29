package de.fhhof.universe.server.communication;

import de.fhhof.universe.shared.communication.PJUTSocket;
import java.net.Socket;


/**
 * Thread, welches eine Verbindung mit einem Client verwaltet.
 * Ihm kann eine ID zugeordnet werden, welche eine positive Zahl
 * sein sollte, 0 bedeutet "nicht gesetzt".
 * Kann einen zugeordneten Monitor benachrichtigen, wenn der genutzte
 * Socket nicht mehr brauchbar ist und dabei seine ID angeben.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTServerSocket extends PJUTSocket
{
	private byte id;
	
	private PJUTServerConnMonitor servMonitor;

	/**
	 * Erzeugt ein Runnable, welches die Verbindung zur Gegenseite verwaltet,
	 * die vom übergebenen Socket repräsentiert wird.
	 * 
	 * @param socket schon zum Client verbundener Socket
	 * @throws Exception wenn Socket unbrauchbar
	 */
	public PJUTServerSocket(Socket socket) throws Exception
	{
		super(socket);
		
		id = 0;
	}

	/**
	 * Setzt die ID des Clients hinter der Verbindung.
	 * ID muss positiv sein, nur 0 bedeutet standardmäßig "keine ID vergeben".
	 * 
	 * @param id neue ID des Clients
	 */
	public void setId(byte id)
	{
		if(id >= 0)
		{
			this.id = id;
		}
	}
	
	/**
	 * Liefert die ID des Clients hinter der Verbindung.
	 * 0 bedeutet standardmäßig "keine ID vergeben".
	 * 
	 * @return id des verbundenen Clients
	 */
	public byte getId()
	{
		return id;
	}
	
	@Override
	public void connectionLost()
	{
		super.connectionLost();
		
		if(servMonitor != null)
		{
			servMonitor.connectionLost(id);
		}
	}

	/**
	 * Setzt den Monitor, der benachrichtigt wird, wenn der benutzte Socket
	 * nichtmehr benutzbar ist.
	 * Wird null übergeben werden keine Meldungen ausgegeben.
	 * 
	 * @param servMonitor neuer ServerSocketMonitor
	 */
	public void setServMonitor(PJUTServerConnMonitor servMonitor)
	{
		this.servMonitor = servMonitor;
	}

	/**
	 * @return derzeit registrierter ServerSocketMonitor
	 */
	public PJUTServerConnMonitor getServMonitor()
	{
		return servMonitor;
	}
}
