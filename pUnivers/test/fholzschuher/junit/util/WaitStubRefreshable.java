package fholzschuher.junit.util;


/**
 * Refreshable, welches eine Verzögerung durch Last simuliert
 * 
 * @author sylence
 *
 */
public class WaitStubRefreshable extends StubRefreshable
{
	@Override
	public void refresh(float timeDiff)
	{
		super.refresh(timeDiff);
		try
		{
			Thread.sleep(6);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
