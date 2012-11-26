package fholzschuher.junit;

import java.util.List;

import universetournament.client.core.PJUTClientCore;
import universetournament.server.core.PJUTServerCore;
import fholzschuher.junit.util.StubRefreshable;
import fholzschuher.junit.util.WaitStubRefreshable;
import junit.framework.TestCase;

/**
 * Testet die Kern-/Timing-Systeme.
 * Beim Client-Core kann nur die autonome Funktionalität getestet werden,
 * da das Fremd-Timing von der Engine kommt, die hier nicht getestet werden
 * soll.
 * Die Grundfunktionalität ist bei beiden sowieso gleich, weil ererbt.
 * 
 * @author sylence
 *
 */
public class CoreTest extends TestCase
{
	//man kann kein exaktes Timing erwarten -> Toleranz.
	private static final float tolerance = 0.05f;
	private PJUTClientCore clientCore;
	private PJUTServerCore serverCore;
	
	@Override
	protected void setUp()
	{
		clientCore = new PJUTClientCore();
		serverCore = new PJUTServerCore();
	}
	
	@Override
	protected void tearDown()
	{
		clientCore = null;
		serverCore.setActive(false);
		serverCore = null;
	}
	
	/**
	 * Testet das Starten und Stoppen des Kerns.
	 * Zwangsweise wird auch das Hinzufügen und Entfernen von Refreshables
	 * getestet, da sonst keine Informationen aus dem Core entnommen werden
	 * können.
	 */
	public void testAddRemoveStartStop()
	{
		StubRefreshable ref = new StubRefreshable();
		
		serverCore.setActive(true);
		serverCore.addTimedRefreshable(ref);
		new Thread(serverCore).start();
		
		//etwas warten, damit der Core das Refreshable einhängen kann etc.
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		//Zeit eingetragen?
		TestCase.assertTrue(ref.getLastTime() != 0);
		
		serverCore.setActive(false);
		
		//etwas warten, damit der Core sicher anhalten kann
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		long oldTime = ref.getLastTime();
		
		/*
		 * warten, dass falls Core noch laufen würde, neue Zeiten eingetragen
		 * würden
		 */
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		TestCase.assertEquals(oldTime, ref.getLastTime());
	}
	
	/**
	 * Komplizierteres Add/Remove testen.
	 */
	public void testAddRemove()
	{
		StubRefreshable ref1 = new StubRefreshable();
		StubRefreshable ref2 = new StubRefreshable();
		StubRefreshable ref3 = new StubRefreshable();
		
		serverCore.setActive(true);
		new Thread(serverCore).start();
		
		serverCore.addTimedRefreshable(ref1);
		serverCore.addTimedRefreshable(ref2);
		
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		TestCase.assertTrue(ref1.getLastTime() != 0);
		TestCase.assertTrue(ref2.getLastTime() != 0);
		
		//Entfernen testen, auch nicht vorhandenes sollte gut gehen
		serverCore.removeTimedRefreshable(ref2);
		serverCore.removeTimedRefreshable(ref3);
		
		long oldTime1 = ref1.getLastTime();
		
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		TestCase.assertTrue(oldTime1 != ref1.getLastTime());
		TestCase.assertEquals(0, ref3.getLastTime());
		long oldTime2 = ref2.getLastTime();
		
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		TestCase.assertEquals(oldTime2, ref2.getLastTime());
		
		serverCore.removeTimedRefreshable(ref1);
		
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		oldTime1 = ref1.getLastTime();
		
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		TestCase.assertEquals(oldTime1, ref1.getLastTime());
	}
	
	/**
	 * Testen, ob die automatische Regulierung der Ticklänge funktioniert.
	 */
	public void testIPS()
	{
		StubRefreshable ref = new StubRefreshable();
		float ips = 75.f;

		serverCore.setTicksPerSecond(ips);
		serverCore.setActive(true);
		new Thread(serverCore).start();
		
		serverCore.addTimedRefreshable(ref);
		
		//5 Sekunden Werte sammeln
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		//Anhalten, auf Beendinung warten
		serverCore.setActive(false);
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		List<Float> values = ref.getTimes();
		float total = 0;
		for(Float f : values)
		{
			total += f;
		}
		total /= values.size();
		
		//Toleranzgrenzen
		float upper = 1000.f/ips;
		upper += upper * tolerance;
		float lower = 1000.f/ips;
		lower -= lower * tolerance;
		
		TestCase.assertTrue(total >= lower && total <= upper);
	}
	
	/**
	 * Testen, ob die automatische Regulierung der Ticklänge unter Pseudo-Last
	 * funktioniert.
	 */
	public void testLoadIPS()
	{
		WaitStubRefreshable ref = new WaitStubRefreshable();
		float ips = 60.f;

		serverCore.setTicksPerSecond(ips);
		serverCore.setActive(true);
		new Thread(serverCore).start();
		
		serverCore.addTimedRefreshable(ref);
		
		//5 Sekunden Werte sammeln
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		//Anhalten, auf Beendinung warten
		serverCore.setActive(false);
		try
		{
			Thread.sleep(180);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		List<Float> values = ref.getTimes();
		float total = 0;
		for(Float f : values)
		{
			total += f;
		}
		total /= values.size();
		
		//Toleranzgrenzen
		float upper = 1000.f/ips;
		upper += upper * tolerance;
		float lower = 1000.f/ips;
		lower -= lower * tolerance;
		
		TestCase.assertTrue(total >= lower && total <= upper);
	}
	
	/**
	 * Iterations-Limit des Clients testen.
	 */
	public void testLimiter()
	{
		StubRefreshable ref = new StubRefreshable();
		final float limit = 100.f;
		
		//Client-Core mit simulierter Fremdsteuerung ohne Eigenlast
		clientCore.setTpsLimit(limit);
		clientCore.addTimedRefreshable(ref);
		
		clientCore.initialize(1000000.f / limit);
		clientCore.refresh();
		try
		{
			Thread.sleep(5);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		int num = 500;
		//Werte sammeln
		while(--num > 0)
		{
			clientCore.refresh();
			try
			{
				//ca. 200 TPS
				Thread.sleep(5);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
		
		List<Float> values = ref.getTimes();
		float total = 0;
		for(Float f : values)
		{
			total += f;
		}
		total /= values.size();
		
		//Toleranzgrenzen
		float upper = 1000.f/limit;
		upper += upper * tolerance;
		float lower = 1000.f/limit;
		lower -= lower * tolerance;
		
		System.out.println(total + " " + lower + " " + upper);
		
		TestCase.assertTrue(total >= lower && total <= upper);
	}
}
