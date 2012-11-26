package pjut.server.core;

import pjut.server.events.buffers.PJUTEventBuffManager;
import pjut.shared.core.PJUTTimedCore;
import pjut.shared.logic.PJUTTimedRefreshable;

/**
 * Beinhaltet die Hauptschleife des Servers, die Zeitgesteuert Aufgaben
 * delegiert und Events verschicken lässt.
 * Die Hauptschleife läuft mit einer definierten Tickrate.
 * 
 * @author sylence
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
	private static final float SLEEP_DIFF_INFL = 0.1f;
	
	private float tickLength, diff;
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
	}
	
	@Override
	/**
	 * Versucht in möglichst gleichmäßigen Zeitintervallen der vorgegebenen
	 * Länge "Ticks" auszuführen.
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
		diff = measuredTickLength - tickLength;
		
		//TODO: Differenz einrechnen
		
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
		//TODO
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
