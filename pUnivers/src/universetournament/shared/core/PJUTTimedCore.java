package universetournament.shared.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import universetournament.shared.logic.PJUTTimedRefreshable;

/**
 * Abstrakter Programmkern, der die Logik zum Berechnen der Dauer einer
 * Iteration inklusive Glättungsfunktion besitzt, die in den Hauptschleifen der
 * Teilprogramme gebraucht wird.
 * Die Glättung ist wichtig um den Einfluss von Messfehlern, Ungenauigkeiten
 * und sonstigen Schwankungen auf die gemessene Zeitdifferenz zu minimieren.
 * 
 * @author Florian Holzschuher
 *
 */
public abstract class PJUTTimedCore
{
	//zu wie viel % neue Zeitmessungen in das Ergebnis eingehen
	private static final float TIME_MEASURE_FACT = 0.1f;
	private static final float TIME_OLD_FACT = 1.0f - TIME_MEASURE_FACT;
	
	/**
	 * Liste in der die Refreshables gehalten werden.
	 */
	protected ArrayList<PJUTTimedRefreshable> refreshables;
	
	/**
	 * Liste von Refreshables, die hinzugefügt werden sollen.
	 * Dient zum Asynchronen Hinzufügen von Refreshables.
	 */
	protected LinkedList<PJUTTimedRefreshable> toAdd;
	
	/**
	 * Liste von Refreshables, die entfernt werden sollen.
	 * Dient zur Asynchronen Entfernung von Refreshables.
	 */
	protected LinkedList<PJUTTimedRefreshable> toRemove;
	
	/**
	 * Relative Zeit bei der letzten Ausführung der tick()-Methode
	 * in Nanosekunden.
	 */
	protected long lastNanoTime;
	
	/**
	 * Gemessene, geglättete Länge eines Ticks in Nanosekunden.
	 */
	protected float measuredTickLength;
	
	private long newNanoTime;
	
	/**
	 * Erzeugt einen leeren, unkonfigurierten Programmkern.
	 */
	public PJUTTimedCore()
	{
		refreshables = new ArrayList<PJUTTimedRefreshable>();
		toRemove = new LinkedList<PJUTTimedRefreshable>();
		toAdd = new LinkedList<PJUTTimedRefreshable>();
	}
	
	/**
	 * Initialisiert die Berechnung mit der gewünschten zeitlichen
	 * Schleifenlänge.
	 * Sollte beim ersten Schleifenaufruf aufgerufen werden, um Sprünge am
	 * Anfang der Berechnung zu vermeiden.
	 * 
	 * @param targetTickLength gewünschte Tick-Länge in Nanosekunden
	 */
	protected void initialize(float targetTickLength)
	{
		measuredTickLength = targetTickLength;
		lastNanoTime = System.nanoTime();
	}
	
	private void tick()
	{
		newNanoTime = System.nanoTime();
		
		//alter Messwert 90%, neuer 10% als Standard
		measuredTickLength = measuredTickLength * TIME_OLD_FACT
			+ (newNanoTime - lastNanoTime) * TIME_MEASURE_FACT; 
		
		lastNanoTime = newNanoTime;
	}
	
	/**
	 * Lässt die Ticklänge berechnen, alle registrierten Refreshables
	 * auffrischen und die Liste der Refreshables aktualisieren.
	 */
	protected void handleRefresh()
	{
		//Ticklänge berechnen
		tick();
		
		synchronized(refreshables)
		{
			//Eingehängte Objekte auffrischen
			for(PJUTTimedRefreshable ref : refreshables)
			{
				ref.refresh(measuredTickLength);
			}
			
			//hinzuzufügende einfügen
			synchronized(toAdd)
			{
				for(PJUTTimedRefreshable ref : toAdd)
				{
					refreshables.add(ref);
				}
				toAdd.clear();
			}
			
			//zu entfernende entfernen
			synchronized(toRemove)
			{
				for(PJUTTimedRefreshable ref : toRemove)
				{
					refreshables.remove(ref);
				}
				toRemove.clear();
			}
		}
	}
	
	/**
	 * Lässt bei nächster Gelegenheit das Refreshable hinzufügen, welches bei
	 * jeder Schleifeniteration beanachrichtigt wird.
	 * Wird am Ende der internen Liste eingefügt, also als letztes aufgerufen.
	 * Führt keine Doppelungsprüfung durch.
	 * Dies funktioniert nicht sofort, da sonst der Schleifenablauf gestört
	 * werden könnte.
	 * Ignoriert null.
	 * 
	 * @param ref hinzuzufügendes Refreshable
	 * @param inFront ob das Element an den Anfang der Schleifeniteration soll
	 */
	public void addTimedRefreshable(PJUTTimedRefreshable ref)
	{
		if(ref != null)
		{
			synchronized(toAdd)
			{
				toAdd.add(ref);
			}
		}
	}
	
	/**
	 * Lässt bei nächster Gelegenheit das Refreshable entfernen, sofern
	 * vorhanden.
	 * Dies funktioniert nicht sofort, da sonst der Schleifenablauf gestört
	 * werden könnte.
	 * Ignoriert null.
	 * 
	 * @param ref zu entferndendes Refreshable
	 */
	public void removeTimedRefreshable(PJUTTimedRefreshable ref)
	{
		if(ref != null)
		{
			synchronized(toRemove)
			{
				toRemove.add(ref);
			}
		}
	}
	
	/**
	 * Liefert eine Liste aller eingehängten Refreshables, die aber nicht
	 * manipuliert werden sollte.
	 * 
	 * @return Liste der Refreshables
	 */
	public List<PJUTTimedRefreshable> getRefreshables()
	{
		return refreshables;
	}
}
