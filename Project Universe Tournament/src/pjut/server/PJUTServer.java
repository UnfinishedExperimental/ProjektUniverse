/*
 * 
 */
package pjut.server;

import pjut.server.communication.PJUTConnManager;
import pjut.server.communication.PJUTServerThread;
import pjut.server.core.PJUTServerCore;
import pjut.testing.TesterServer;

/**
 * Klasse die eine Server-Instanz darstellt und zu dessen Start dient.
 * 
 * @author sylence
 *
 */
public class PJUTServer
{
	public static void main(String[] args)
	{
		TesterServer server = new TesterServer();
		PJUTConnManager.getInstance().setListener(server);
		
		PJUTServerCore core = new PJUTServerCore();
		core.setActive(true);
		
		try
		{
			new Thread(new PJUTServerThread(6667)).start();
			new Thread(core).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
