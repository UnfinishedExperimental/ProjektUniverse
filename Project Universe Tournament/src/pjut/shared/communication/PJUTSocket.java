/*
 * 
 */
package pjut.shared.communication;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Objekt, welches die Kommunikation mit der verbundenen Gegenseite verwaltet
 * und dazu ein Unter-Thread zum Senden und eins zum Empfangen erzeugt.
 * Auf die Liste der eingehenden Objekte wird ein notify() aufgerufen, wenn
 * ein neues Objekt eintrifft.
 * Kann einen zugeordneten Monitor benachrichtigen, wenn der genutzte
 * Socket nicht mehr brauchbar ist.
 * 
 * @author sylence
 *
 */
public class PJUTSocket implements PJUTSocketMonitor
{
	private Socket socket;
	private LinkedList<Serializable> incoming, outgoing;
	
	private PJUTSocketReceiver receiver;
	private PJUTSocketSender sender;
	private Thread receiverThread, senderThread;
	
	private PJUTSocketMonitor monitor;
	
	private boolean started;
	
	/**
	 * Erzeugt ein Objekt, welches die Verbindung zur Gegenseite verwaltet,
	 * die vom übergebenen Socket repräsentiert wird.
	 * 
	 * @param socket schon zur Gegenseite verbundener Socket
	 * @throws Exception wenn Socket unbrauchbar
	 */
	public PJUTSocket(Socket socket) throws Exception
	{
		if(socket == null)
		{
			throw new Exception("Übergebener Socket ist null");
		}
		else if(!socket.isConnected())
		{
			throw new Exception("Übergebener Socket ist nicht verbunden");
		}
		else
		{
			this.socket = socket;
		}
		
		incoming = new LinkedList<Serializable>();
		outgoing = new LinkedList<Serializable>();
		
		/*
		 * sollte zuerst geöffnet werden, damit der Header für den Receiver
		 * übertragen werden kann (siehe Javadoc ObjectIn-/OutputStream)
		 */
		sender = new PJUTSocketSender(outgoing, socket);
		receiver = new PJUTSocketReceiver(incoming, socket);
		
		sender.setMonitor(this);
		receiver.setMonitor(this);
		
		receiverThread = new Thread(receiver);
		senderThread = new Thread(sender);
		
		started = false;
		
		monitor = null;
	}

	/**
	 * Startet die threads zur Abwicklung von Empfangen und Senden.
	 * Wenn schon geschehen, wird das Kommando ignoriert.
	 */
	public void start()
	{
		if(!started)
		{
			started = true;
			
			receiverThread.start();
			senderThread.start();
		}
	}

	/**
	 * Schließt die Verbindung ohne Anfrage, wenn möglich und Socket offen.
	 * 
	 * @throws IOException wenn Schließen schiefgeht
	 */
	public void forceClose() throws IOException
	{
		if(!socket.isClosed())
		{
			socket.close();
		}
	}
	
	/**
	 * Zeigt an, ob der Socket noch verbunden ist.
	 * 
	 * @return ob verbunden
	 */
	public boolean isConnected()
	{
		return socket.isConnected();
	}
	
	/**
	 * Reiht das übergebene, serialisierbare Objekt in die
	 * Sendewarteschlange ein.
	 * Ignoriert null.
	 * 
	 * @param o zu sendendes Objekt
	 */
	public void send(Serializable o)
	{
		if(o != null)
		{
			synchronized(outgoing)
			{
				outgoing.add(o);
			}
		
			//im Zweifelsfall schlafenden Sender aufwecken
			synchronized(sender)
			{
				sender.notify();
			}
		}
	}
	
	/**
	 * @return Liste eingegangener Objekte
	 */
	public LinkedList<Serializable> getIncoming()
	{
		return incoming;
	}
	
	/**
	 * Enterfernt alle Objekte aus dem Ausgangspuffer.
	 */
	public void clearOutgoing()
	{
		synchronized(outgoing)
		{
			outgoing.clear();
		}
	}
	
	/**
	 * Enterfernt alle Objekte aus dem Eingangspuffer.
	 */
	public void clearIncoming()
	{
		synchronized(incoming)
		{
			incoming.clear();
		}
	}

	@Override
	public void connectionLost()
	{
		receiver.deactivate();
		sender.deactivate();
		
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
	 * @return derzeit registrierter SocketMonitor
	 */
	public PJUTSocketMonitor getMonitor()
	{
		return monitor;
	}
}
