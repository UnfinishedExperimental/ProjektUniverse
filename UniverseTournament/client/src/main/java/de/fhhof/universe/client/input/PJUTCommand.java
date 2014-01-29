package de.fhhof.universe.client.input;

/**
 * Interface für ein ausführbares Kommando, welches beispielsweise auf eine
 * Taste gebunden werden kann.
 * Hinter der Ausführungsmethode können sich beliebige Vorgänge verbergen,
 * wobei durch die Übergabe der Tick-Länge zeitabhängige Aktionen auch
 * angesprochen werden können.
 * (Beispiel Triebwerke aktivieren -> beschleunigen; v += a*t)
 * 
 * @author Florian Holzschuher
 *
 */
public interface PJUTCommand
{
	/**
	 * Veranlasst das Ausführen des jeweilig definierten Kommandos.
	 * Übergeben wird die gemessene Ticklänge, die für manche Berechnungen
	 * wichtig ist. Sie kann, muss aber nicht verwendet werden.
	 * 
	 * @param timeDiff zeitliche Länge des Frames
	 */
	public void execute(float timeDiff);
}
