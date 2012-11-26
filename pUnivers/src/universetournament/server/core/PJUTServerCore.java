package universetournament.server.core;

import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.core.PJUTTimedCore;

/**
 * Beinhaltet die Hauptschleife des Servers, die Zeitgesteuert Aufgaben
 * delegiert und Events verschicken lässt.
 * Die Hauptschleife läuft mit einer definierten Tickrate.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTServerCore extends PJUTTimedCore implements Runnable
{
	/**
	 * Standard-Tick-Länge in Nanosekunden.
	 * Entspricht ungefähr 60 Ticks pro Sekunde.
	 */
	public static final float DEFAULT_TICK_LENGTH = 10000000.0f / .6f;
	
	//wie viel die Zeitdifferenz zum Idealwert in die Schlafzeit eingeht
	private static final float SLEEP_DIFF_INFL = 0.5f;
	private static final float SLEEP_DIFF_OLD_INFL = 1f - SLEEP_DIFF_INFL;
	
	private float tickLength, diff, oldDiff;
	private long sleepMs;
	private int sleepNs;
	
	private boolean active;
	
	private PJUTEventBuffManager eventManager;
	
	/**
	 * Erzeugt einen Server-Kern mit der Standard-Tickrate, der noch nicht
	 * auf "aktiv" geschalten ist.
	 */
	public PJUTServerCore()
	{
		tickLength = DEFAULT_TICK_LENGTH;
		active = false;
		
		eventManager = PJUTEventBuffManager.getInstance();
		
		diff = 0;
		oldDiff = 0;
	}
	
	@Override
	/**
	 * Versucht in möglichst gleichmäßigen Zeitintervallen der vorgegebenen
	 * Länge "Ticks" auszuführen und leert immer am Ende den Eventpuffer.
	 */
	public void run()
	{
		//Schlafzyklen mit gewünschter Länge initialisieren
		sleepInit();
		
		//Zeitmessung initialisieren
		initialize(tickLength);
		
		//damit Thread sich ohne Startschwierigikeiten einpendeln kann.
		tickSleep();
		
		while(active)
		{
			//Ticklänge berechnen und Refreshables bearbeiten
			handleRefresh();
			
			//Events verschicken
			eventManager.flushAll();
			
			tickSleep();
		}
	}
	
	private void sleepInit()
	{
		sleepMs = (long)(tickLength / 1000000);
		
		//zuerst nur auf Long casten, um mögliche Überläufe zu verhindern
		sleepNs = (int)((long)(tickLength) % 1000000);
	}
	
	private void tickSleep()
	{
		//Unterschied zur gewünschten Dauer messen und glätten
		diff = tickLength - measuredTickLength;
		oldDiff = diff * SLEEP_DIFF_INFL + oldDiff * SLEEP_DIFF_OLD_INFL;
		
		//Schlafzeit neu berechnen (% kennt kein negativ)
		sleepMs += (long)(oldDiff / 1000000);
		if(oldDiff > 0)
		{
			sleepNs += (int)((long)(oldDiff) % 1000000);
		}
		else
		{
			sleepNs -= (int)((long)(oldDiff) % 1000000);
		}
		
		//Überläufe abfangen
		if(sleepNs < 0)
		{
			sleepNs += 999999;
		}
		else if(sleepNs > 999999)
		{
			sleepNs -= 999999;
		}
		
		//negative Werte (bei "Hängern") abfangen
		if(sleepMs < 0)
		{
			sleepMs = 0;
		}
		if(sleepNs < 0)
		{
			sleepNs = 0;
		}
		
		try
		{
			Thread.sleep(sleepMs, sleepNs);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			System.err.println("Serverkern unterbrochen (Interrupt)");
			active = false;
		}
	}
	
	/**
	 * @return derzeitige Tick-Länge in Millisekunden
	 */
	public float getTickLength()
	{
		return tickLength;
	}
	
	/**
	 * Setzt die Tick-Dauer auf einen Wert, der möglichst die angegebene
	 * Anzahl an Ticks pro Sekunde erzeugt.
	 * Ignoriert Werte kleiner als 1 und größer als 1000 um Probleme zu
	 * vermeiden.
	 * 
	 * @param tps wie viele Ticks pro Sekunde gewünscht werden
	 */
	public void setTicksPerSecond(float tps)
	{
		if(tps >= 1 || tps <= 1000)
		{
			tickLength = 1000000000f / tps;
		}
	}

	/**
	 * Setzt das Aktivitäts-Flag der Hauptschleife.
	 * Die Schleife wird beim Deaktivieren beim nächsten Durchlauf beendet.
	 * Wird das Thread gestartet während das Flag nicht gesetzt ist, wird
	 * nichts getan und das Thread beendet sich.
	 * 
	 * @param active ob das Aktivitäts-Flag gesetzt werden soll
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	/**
	 * @return ob Kern auf aktiv geschaltet ist
	 */
	public boolean isActive()
	{
		return active;
	}
}
