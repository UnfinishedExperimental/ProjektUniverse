package pjut.client.communication;

import java.net.InetSocketAddress;
import java.net.Socket;

import pjut.shared.communication.PJUTSocket;

/**
 * Utility-Klasse, die Verbindungen zum Server herstellt.
 * 
 * @author sylence
 *
 */
public class PJUTCliConnector
{
	/**
	 * Einsatzbereiten Client Socket erstellen, der auf dem angegebenen Port
	 * zum Server verbindet.
	 * 
	 * @param host Host zu dem verbunden werden soll
	 * @param port anzusprechender Port am Server
	 * @return geöffneter Socket
	 * @throws Exception Wenn Verbindung falsche Parameter hat oder schief geht
	 */
	public static PJUTSocket connectTo(String host, int port) throws Exception
	{
		Socket sock = new Socket();
		
		//Verbindungen halten und auf Latenz optimieren
		sock.setKeepAlive(true);
		sock.setTcpNoDelay(true);
		sock.setPerformancePreferences(0, 5, 1);
		
		sock.connect(new InetSocketAddress(host, port));
		
		return new PJUTSocket(sock);
	}
}