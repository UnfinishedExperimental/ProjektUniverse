/*
 * 
 */
package fholzschuher;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.shared.communication.PJUTSocket;
import universetournament.shared.communication.events.PJUTEventReceiver;
import universetournament.shared.events.util.PJUTEventBus;
import universetournament.shared.events.MainType;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * Klasse, die eine Client-Instanz darstellt und zu dessen Start dient.
 * 
 * @author Florian Holzschuher
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
			socket = PJUTCliConnector.getInstance().connectTo(host, 6667);
			
			PJUTEventReceiver receiver =
				PJUTCliConnector.getInstance().createReceiver(socket);
			
			PJUTEventBus bus = PJUTEventBus.getInstance();

			TesterWindow window = new TesterWindow();
			bus.register(new TesterCommandCreator(
					PJUTCliConnector.getInstance().createBuffer(socket)),
					TestEvent.SubEvents.CONNECTED);
			
			bus.register(UTEntityManager.getInstance(), MainType.TYPE_ENTITY);
			
			receiver.setHandler(MainType.TYPE_ENTITY, bus);
			receiver.setList(socket.getIncoming());
			receiver.setActive(true);
			
			new Thread(window).start();
			receiver.start();
			socket.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
