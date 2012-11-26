package fholzschuher.junit.util;

import java.util.LinkedList;
import java.util.List;

import universetournament.shared.logic.PJUTTimedRefreshable;

/**
 * Refreshable, welches übergebene Differenzwerte (in Millisekunden) und den
 * letzten Aufrufzeitpunkt speichert.
 * 
 * @author sylence
 *
 */
public class StubRefreshable implements PJUTTimedRefreshable
{
	private List<Float> values;
	private long lastTime;
	
	public StubRefreshable()
	{
		values = new LinkedList<Float>();
		lastTime = 0;
	}
	
	@Override
	public void refresh(float timeDiff)
	{
		values.add(timeDiff / 1000000.f);
		lastTime = System.currentTimeMillis();
	}
	
	public List<Float> getTimes()
	{
		return values;
	}
	
	public long getLastTime()
	{
		return lastTime;
	}
}
