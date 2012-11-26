package fholzschuher.junit;

import universetournament.client.input.PJUTInputBuffer;
import universetournament.client.input.PJUTInputDistributor;
import junit.framework.TestCase;

/**
 * Testet das Input-System.
 * 
 * @author sylence
 *
 */
public class InputTest extends TestCase
{
	private PJUTInputBuffer buffer;
	private PJUTInputDistributor distributor;
	
	@Override
	protected void setUp()
	{
		buffer = PJUTInputBuffer.getInstance();
		distributor = PJUTInputDistributor.getInstance();
	}
	
	@Override
	protected void tearDown()
	{
		buffer = null;
		distributor = null;
	}
}
