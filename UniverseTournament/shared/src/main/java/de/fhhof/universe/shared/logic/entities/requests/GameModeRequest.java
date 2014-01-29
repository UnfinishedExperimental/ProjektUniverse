package de.fhhof.universe.shared.logic.entities.requests;

import java.io.Serializable;

import de.fhhof.universe.shared.logic.gamemode.GameModeType;

/**
 * Request für eine GameModeFactory im Server, welches Spieltyp,
 * Spielzeitbegrenzung und Punktebegrenzung enthält.
 * 
 * @author sylence
 *
 */
public class GameModeRequest implements Serializable
{
	private final GameModeType type;
	private final short scoreLimit;
	private final double timeLimit;
	
	/**
	 * Erstellt einen Spielmodus-Request mit den angegebenen Eigenschaften.
	 * Wirft Exceptions, wenn der Spielmodus-Typ null ist oder die Limits
	 * kleiner als 0 sind.
	 * 
	 * @param type Spielmodus-Typ
	 * @param timeLimit Zeitlimit in Sekunden
	 * @param scoreLimit Punktekimit
	 * @throws Exception wenn Daten unbrauchbar
	 */
	public GameModeRequest(GameModeType type, double timeLimit,
			short scoreLimit) throws Exception
	{
		if(type == null)
		{
			throw new NullPointerException("kein gültiger Spielmodustyp");
		}
		if(scoreLimit <= 0 || timeLimit <= 0.)
		{
			throw new Exception("Limits müssen positiv sein");
		}
		
		this.type = type;
		this.timeLimit = timeLimit;
		this.scoreLimit = scoreLimit;
	}

	/**
	 * @return Spielmodus-Typ
	 */
	public GameModeType getType()
	{
		return type;
	}

	/**
	 * @return Zeitlimit in Sekunden
	 */
	public double getTimeLimit()
	{
		return timeLimit;
	}

	/**
	 * @return Punktelimit
	 */
	public short getScoreLimit()
	{
		return scoreLimit;
	}
}
