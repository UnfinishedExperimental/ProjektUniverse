/*
 * 
 */
package pjut.server.communication;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Thread, das von Clients angenommene Verbindungen entgegennimmt und
 * jeweils Bearbeitungs-Threads startet.
 * 
 * @author sylence
 *
 */
public class PJUTServerThread implements Runnable
{
	/**
	 * Minimal einstellbarer Port
	 */
	public static final int MIN_PORT = 0;
	
	/**
	 * Maximal einstellbarer Port
	 */
	public static final int MAX_PORT = 65535;
	
	private boolean listening;
	private int port;
	private ServerSocket socket;
	
	/**
	 * Erzeugt, wenn der Port im gültigen Bereich zwischen MIN und MAX_PORT
	 * liegt, ein lauschbereites Socket-Runnable.
	 * Anderenfalls wird eine Exception geworfen.
	 * 
	 * @param port Port auf dem gelauscht werden soll
	 * @throws Exception wenn Port außerhalb des gültigen Bereichs
	 */
	public PJUTServerThread(int port) throws Exception
	{
		if(port >= MIN_PORT && port <= MAX_PORT)
		{
			this.port = port;
		}
		else
		{
			throw new Exception("ungültiger Port");
		}
		
		listening = true;
		socket = null;
	}
	
	private void handleConnections(ServerSocket socket)
	{
		while(listening)
		{
			try
			{
				Socket current = socket.accept();
				
				//Verbindungen halten und auf Latenz optimieren
				current.setKeepAlive(true);
				current.setTcpNoDelay(true);
				
				//funktioniert eventuell nicht, wird aber ignoriert (->javadoc)
				current.setPerformancePreferences(0, 5, 1);
				
				//Kommunikations-Thread starten und registrieren
				PJUTServerSocket sock = new PJUTServerSocket(current);
				sock.start();
				PJUTConnManager.getInstance().addConnection(sock);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	@Override
	/**
	 * Versucht den Socket zu öffnen, ankommende Verbindungen anzunehmen und
	 * im Zweifelsfall nach beendeter Arbeit den Socket wieder zu schließen.
	 */
	public void run()
	{
		//Socket öffnen
		try
		{
			socket = new ServerSocket();
			socket.setPerformancePreferences(0, 5, 1);
			socket.bind(new InetSocketAddress("0.0.0.0", port));
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		if(socket != null)
		{
			handleConnections(socket);
		}
		
		/*
		 * Falls aufgrund von Problemen noch nicht geschehen, Socket schließen
		 */
		if(socket != null && !socket.isClosed())
		{
			try
			{
				socket.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Lässt die Verbindungs-Annahmeschleife auslaufen und schließt
	 * den Lausch-Socket.
	 */
	public void stop()
	{
		listening = false;
		
		if(socket != null && !socket.isClosed())
		{
			try
			{
				socket.close();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
