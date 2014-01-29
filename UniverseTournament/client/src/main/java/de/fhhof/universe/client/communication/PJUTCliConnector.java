package de.fhhof.universe.client.communication;

import java.net.InetSocketAddress;
import java.net.Socket;

import de.fhhof.universe.client.events.buffers.PJUTConnEventBuffer;
import de.fhhof.universe.shared.communication.PJUTSocket;
import de.fhhof.universe.shared.communication.events.PJUTEventReceiver;

/**
 * Utility-Klasse, die Verbindungen zum Server herstellt, Receiver startet,
 * Puffer erstellt und diese für andere Komponenten bereitstellt.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTCliConnector
{
	private static final PJUTCliConnector instance = new PJUTCliConnector();
	
	private PJUTSocket socket;
	private PJUTEventReceiver receiver;
	private PJUTConnEventBuffer buffer;
	
	/**
	 * @return Instanz der Klasse.
	 */
	public static synchronized PJUTCliConnector getInstance()
	{
		return instance;
	}
	
	private PJUTCliConnector()
	{
		socket = null;
		receiver = null;
		buffer = null;
	}
	
	/**
	 * Liefert den eingetragenen Socket oder null zurück.
	 * Keine Garantie, dass eingetragene Sockets verbunden sind.
	 * 
	 * @return eingetragener Socket
	 */
	public PJUTSocket getSocket()
	{
		return socket;
	}
	
	/**
	 * Liefert den eingetragenen Receiver oder null zurück.
	 * Keine Garantie, dass eingetragene Receiver an einen funktionierenden
	 * Socket gebunden sind.
	 * 
	 * @return eingetragener Receiver
	 */
	public PJUTEventReceiver getReceiver()
	{
		return receiver;
	}
	
	/**
	 * Liefert den eingetragenen Puffer oder null zurück.
	 * Keine Garantie, dass der eingetragene Puffer an einen funktionierenden
	 * Socket gebunden ist.
	 * 
	 * @return eingetragener Puffer
	 */
	public PJUTConnEventBuffer getBuffer()
	{
		return buffer;
	}
	
	/**
	 * Falls vorhanden alte Verbindung trennen, neue Verbindung mit den
	 * übergebenen Parametern herstellen und für diese einen PJUTSocket
	 * erzeugen.
	 * Registriert einen Notifier, welcher bei verlorener Verbindung einen
	 * Dialog mit einer Meldung anzeigt.
	 * Der Socket wird sowohl intern gehalten, als auch zurückggegeben.
	 * beim Trennen der alten Verbindung können Exeptions in der Konsole
	 * angezeigt werden, was aber normal ist.
	 * Ingoriert Aufrufe, bei denen der Host null ist oder keine Zeichen
	 * enthält oder der Port ungültig ist und gibt dann den bisher
	 * eingetragenen Socket zurück, oder null, falls keiner vorhanden ist.
	 * 
	 * @param host Host zu dem verbunden werden soll
	 * @param port anzusprechender Port am Server
	 * @return geöffneter Socket
	 * @throws Exception Wenn Verbindung falsche Parameter hat oder schief geht
	 */
	public PJUTSocket connectTo(String host, int port) throws Exception
	{
		if(host != null && host.length() > 0 && port >= 0 && port < 65535)
		{
			if(socket != null && !socket.isConnected())
			{
				socket.forceClose();
				socket = null;
			}
		
			Socket sock = new Socket();
			
			//Verbindungen halten und auf Latenz optimieren
			sock.setKeepAlive(true);
			sock.setTcpNoDelay(true);
			sock.setPerformancePreferences(0, 5, 1);
			
			sock.connect(new InetSocketAddress(host, port));
			
			socket = new PJUTSocket(sock);
			socket.setMonitor(new ConnectionNotifier());
		}
		
		return socket;
	}
	
	/**
	 * Erzeugt einen Recceiver, der an den eingehenden Puffer des übergebenen
	 * Sockets gebunden und noch nicht gestartet ist.
	 * Bei Aufrufen mit null wird nur der bisher eingetragene Receiver
	 * zurückgegeben, welcher auch null sein kann.
	 * 
	 * @param socket zu benutzender Socket
	 * @return neu erstellter Receiver
	 */
	public PJUTEventReceiver createReceiver(PJUTSocket socket)
	{
		if(socket != null)
		{
			if(receiver != null)
			{
				receiver.setActive(false);
				receiver = null;
			}
			
			receiver = new PJUTEventReceiver();
			receiver.setActive(true);
			receiver.setList(socket.getIncoming());
		}
		
		return receiver;
	}
	
	/**
	 * Erzeugt einen ausgehenden Event-Puffer für den übergebenen Socket,
	 * legt diesen intern ab und gibt ihn Zurück.
	 * Bei Aufrufen mit null wird nur der bisher eingetragene Puffer
	 * zurückgegeben, welcher auch null sein kann.
	 * 
	 * @param socket zu benutzender Socket
	 * @return neu erstellter Puffer
	 */
	public PJUTConnEventBuffer createBuffer(PJUTSocket socket)
	{
		if(socket != null)
		{
			if(buffer != null)
			{
				buffer.flush();
				buffer = null;
			}
		
			buffer = new PJUTConnEventBuffer(socket);
		}
		
		return buffer;
	}
}