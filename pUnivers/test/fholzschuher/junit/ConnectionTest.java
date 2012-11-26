package fholzschuher.junit;

import universetournament.client.communication.PJUTCliConnector;
import junit.framework.TestCase;

/**
 * Testet das Verbindungssystem.
 * 
 * @author sylence
 *
 */
public class ConnectionTest extends TestCase
{
	private PJUTCliConnector connector;
	
	@Override
	protected void setUp()
	{
		connector = PJUTCliConnector.getInstance();
	}
	
	@Override
	protected void tearDown()
	{
		connector = null;
	}
}
