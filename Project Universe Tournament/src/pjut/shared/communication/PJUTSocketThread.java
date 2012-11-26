package pjut.shared.communication;

import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Abstraktes Runnable, welches eine Liste zur Aufnahme von Objekten
 * und eine Variable, die den Aktivierungszustand anzeigt hat.
 * Kann einen zugeordneten Monitor benachrichtigen, wenn der genutzte
 * Socket nicht mehr brauchbar ist.
 * 
 * @author sylence
 *
 */
public abstract class PJUTSocketThread implements Runnable
{
	/**
	 * Zeigt an, ob die Thread-Schleife aktiv sein soll.
	 */
	protected boolean active;
	
	/**
	 * Socket, über den kommuniziert wird.
	 */
	protected Socket socket;
	
	/**
	 * Liste, in der Objekte gepuffert werden.
	 */
	protected LinkedList<Serializable> list;
	
	private PJUTSocketMonitor monitor;
	
	/**
	 * Erzeugt ein Socket-Runnable, welches Objekte in der angegebenen
	 * Liste puffert mit gesetztem Aktivitäts-Flag.
	 * Darf nicht mit null aufgerufen werden.
	 * 
	 * @param list Pufferliste
	 * @param socket Socket, über den Objekte ausgetauscht werden
	 */
	public PJUTSocketThread(LinkedList<Serializable> list, Socket socket)
		throws Exception
	{
		active = true;
		
		if(list != null)
		{
			this.list = list;
		}
		else
		{
			throw new Exception("Pufferliste ist null");
		}
		
		if(socket != null)
		{
			this.socket = socket;
		}
		else
		{
			throw new Exception("Socket ist null");
		}
		
		monitor = null;
	}
	
	/**
	 * Setzt die schleifensteuernde Variable auf false, das Thread hält bei
	 * der nächsten Gelegenheit an.
	 */
	public void deactivate()
	{
		active = false;
	}
	
	/**
	 * Wird von Unterklassen aufgerufen, wenn der Socket nichtmehr benutzt
	 * werden kann.
	 */
	protected void connectionError()
	{
		if(monitor != null)
		{
			monitor.connectionLost();
		}
	}

	/**
	 * Setzt den Monitor, der benachrichtigt wird, wenn der benutzte Socket
	 * nichtmehr benutzbar ist.
	 * Wird null übergeben werden keine Meldungen ausgegeben.
	 * 
	 * @param monitor Monitor, der bei Verbindungsproblemen benachrichtigt wird
	 */
	public void setMonitor(PJUTSocketMonitor monitor)
	{
		this.monitor = monitor;
	}

	/**
	 * @return eventuell registrierter SocketMonitor.
	 */
	public PJUTSocketMonitor getMonitor()
	{
		return monitor;
	}
}
