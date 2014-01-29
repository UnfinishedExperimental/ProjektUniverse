package de.fhhof.universe.client.core;

import de.fhhof.universe.client.communication.PJUTCliConnector;
import de.fhhof.universe.shared.core.PJUTTimedCore;
import de.fhhof.universe.shared.events.buffers.PJUTEventBuffer;

/**
 * Programmkern für den Client, welcher durch einen Auffrischungsaufruf vom
 * Renderer nach jedem Frame eine Iteration der Hauptschleife durchläuft.
 * Beinhaltet die Logik um die Iterationen pro Sekunde zu begrenzen,
 * beziehungsweise ein "Frame limit" durchzusetzen.
 * Dies sollte lediglich nötig sein, wenn die vertikale Synchronisazion in
 * der Grafikkonfiguration deaktiviert ist.
 * Bei der Initialisierung des Renderers muss außerdem die initialize-Methode
 * aufgerufen werden, um die Zeitmessung vernünftig beginnen zu können.
 * Kann vor der Initialisierung der Engine als Runnable gestartet werden, um
 * in einem festen Intervall die Refreshables zu aktualisieren.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTClientCore extends PJUTTimedCore implements Runnable
{
	boolean engineRunning;
	private final PJUTCliConnector connector;
	private float tpsLimit, currentTps;
	
	/**
	 * Erstellt einen neuen Client-Programmkern mit einem Frame Limit von
	 * 100 Iterationen pro Sekunde.
	 * Bei der Initialisierung des Renderers muss noch die initialize-Methode
	 * aufgerufen werden, um die Zeitmessung vernünftig beginnen zu können.
	 */
	public PJUTClientCore()
	{
		engineRunning = false;
		connector = PJUTCliConnector.getInstance();
		tpsLimit = 100.f;
		currentTps = tpsLimit;
	}
	
	@Override
	public void initialize(float targetTickLength)
	{
		super.initialize(targetTickLength);
		currentTps = 1 / (targetTickLength / 1000000000.f);
	}
	
	/**
	 * Vom Renderer aufzurufende Iterationsmethode, die die Refreshables
	 * aktualisiert, das Iterationslimit durchsetzt und falls vorhanden
	 * im Puffer befindliche Events verschickt.
	 */
	public void refresh()
	{
		engineRunning = true;
		
		enforceLimit();
		
		handleRefresh();
		
		PJUTEventBuffer buffer = connector.getBuffer();
		if(buffer != null)
		{
			buffer.flush();
		}
	}
	
	private void enforceLimit()
	{
		if(tpsLimit > 0.f)
		{
			currentTps = (1000000000.f / measuredTickLength);
			
			if(currentTps > tpsLimit)
			{
				try
				{
					//Prozentsatz der zu viel ist warten
					Thread.sleep((long)((measuredTickLength / 1000000.f) *
							(currentTps/tpsLimit) - 1.f));
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
					System.err.println("Clientkern unterbrochen (Interrupt)");
				}
			}
		}
	}
	
	/**
	 * Setzt das Limit für die Iterationen pro Sekunde.
	 * Werte zwischen 0 und 20 werden zur Sicherheit ignoriert.
	 * Werte kleiner als 0 bedeuten "deaktiviert".
	 * 
	 * @param tpsLimit neues FPS Limit
	 */
	public void setTpsLimit(float tpsLimit)
	{
		if(tpsLimit < 0.f || tpsLimit > 20.f)
		{
			this.tpsLimit = tpsLimit;
		}
	}
	
	/**
	 * Liefert das Limit für die Iterationen pro Sekunde.
	 * Werte kleiner als 0 bedeuten "deaktiviert".
	 * 
	 * @return aktuelles FPS Limit
	 */
	public float getTpsLimit()
	{
		return tpsLimit;
	}

	@Override
	public void run()
	{
		while(!engineRunning)
		{
			handleRefresh();
			try
			{
				Thread.sleep(16);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				System.out.println("Client Core unterbrochen (Interrupt)");
			}
		}
	}
}
