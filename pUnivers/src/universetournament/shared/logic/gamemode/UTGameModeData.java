package universetournament.shared.logic.gamemode;

import java.util.HashMap;

import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.PJUTPlayer;

/**
 * Klasse, die die überall bekannten Daten für einen Spielmodus enthält.
 * 
 * @author Bernd Marbach
 *
 */
public class UTGameModeData extends Entity
{
	private boolean running;
	private final HashMap<Byte, PJUTPlayer> players;
	private final short scoreLimit;
	private double timeLeft;
	
	/**
	 * Erstellt Spielmodusdaten mit der angegebenen Restzeit und dem
	 * angegebenen Punktelimit.
	 * 
	 * @param id ID der Entity
	 * @param timeLeft verbleibende Zeit in Sekunden
	 * @param scoreLimit Punktegrenze
	 */
	public UTGameModeData(short id, double timeLeft, short scoreLimit)
	{
		super(id);
		running = true;
		this.timeLeft = timeLeft;
		this.scoreLimit = scoreLimit;
		players = new HashMap<Byte, PJUTPlayer>();
	}
	
	/**
	 * @return Punktelimit für Teams oder Einzelspieler
	 */
	public short getScoreLimit()
	{
		return scoreLimit;
	}
	
	/**
	 * @return Restzeit in Sekunden
	 */
	public double getTimeLeft()
	{
		return timeLeft;
	}
	
	/**
	 * @param seconds Zeit die vergangen ist in Sekunden
	 */
	public void timePassed(double seconds)
	{
		timeLeft -= seconds;
	}
	
	/**
	 * @param timeLeft neue Restzeit in Sekunden
	 */
	public void setTimeLeft(double timeLeft)
	{
		this.timeLeft = timeLeft;
	}

	/**
	 * @return Spieler im Spiel
	 */
	public HashMap<Byte, PJUTPlayer> getPlayers()
	{
		return players;
	}

	/**
	 * @param ob der "Spiel läuft"-Marker gesetzt sein soll
	 */
	public void setRunning(boolean running)
	{
		this.running = running;
	}

	/**
	 * @return ob das Spiel noch läuft
	 */
	public boolean isRunning()
	{
		return running;
	}
}
