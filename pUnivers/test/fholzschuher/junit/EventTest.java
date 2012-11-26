package fholzschuher.junit;

import universetournament.shared.communication.events.PJUTEventReceiver;
import universetournament.shared.events.util.PJUTEventBus;
import junit.framework.TestCase;

/**
 * Testet das Event-Basissystem.
 * 
 * @author Florian Holzschuher
 *
 */
public class EventTest extends TestCase
{
	private PJUTEventBus eventBus;
	private PJUTEventReceiver receiver;
	
	@Override
	protected void setUp()
	{
		eventBus = PJUTEventBus.getInstance();
		receiver = new PJUTEventReceiver();
	}
	
	@Override
	protected void tearDown()
	{
		eventBus = null;
		receiver = null;
	}
}
