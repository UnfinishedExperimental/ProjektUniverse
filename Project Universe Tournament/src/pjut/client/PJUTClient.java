/*
 * 
 */
package pjut.client;

import pjut.client.communication.PJUTCliConnector;
import pjut.client.events.buffers.PJUTConnEventBuffer;
import pjut.shared.communication.PJUTSocket;
import pjut.shared.communication.events.PJUTEventReceiver;
import pjut.testing.TesterWindow;

/**
 * Klasse, die eine Client-Instanz darstellt und zu dessen Start dient.
 * 
 * @author sylence
 *
 */
public class PJUTClient
{
	public static void main(String[] args)
	{
		PJUTSocket socket = null;
		
		String host = "127.0.0.1";
		
		if(args.length > 0)
		{
			host = args[0];
		}
		
		try
		{
			socket = PJUTCliConnector.connectTo(host, 6667);
			socket.start();
			
			TesterWindow window = new TesterWindow(
					new PJUTConnEventBuffer(socket));
			PJUTEventReceiver receiver = new PJUTEventReceiver();
			
			receiver.setHandler((byte)1, window);
			receiver.setList(socket.getIncoming());
			receiver.setActive(true);
			
			receiver.start();
			new Thread(window).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
