/*
 * 
 */
package fholzschuher;

import universetournament.server.communication.PJUTConnManager;
import universetournament.server.communication.PJUTServerThread;
import universetournament.server.core.PJUTServerCore;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * Klasse die eine Server-Instanz darstellt und zu dessen Start dient.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTServer
{
	public static void main(String[] args)
	{
		TesterServer server = new TesterServer();
		PJUTConnManager.getInstance().setListener(server);
		
		PJUTServerCore core = new PJUTServerCore();
		core.addTimedRefreshable(UTEntityManager.getInstance());
		core.setActive(true);
		
		try
		{
			new Thread(core).start();
			new Thread(new PJUTServerThread(6667)).start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
