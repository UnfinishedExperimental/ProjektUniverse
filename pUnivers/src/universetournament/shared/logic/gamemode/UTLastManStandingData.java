package universetournament.shared.logic.gamemode;

import java.util.BitSet;

/**
 * Daten für den "Last Man Standing"-Spielmodus.
 * 
 * @author Bernd Marbach
 *
 */
public class UTLastManStandingData extends UTGameModeData
{
	//welche Spieler schon tot sind
	private BitSet dead;
	
	private boolean unlocked;
	
	/**
	 * Erstellt Spielmodusdaten mit der angegebenen Restzeit und dem
	 * angegebenen Punktelimit noch ohne eingetragene tote Spieler.
	 * 
	 * @param id ID der Entity
	 * @param timeLeft verbleibende Zeit
	 * @param scoreLimit Punktegrenze
	 */
	public UTLastManStandingData(short id, double timeLeft, short scoreLimit)
	{
		super(id, timeLeft, scoreLimit);
		dead = new BitSet();
		unlocked = false;
	}
	
	/**
	 * @param id ID des angefragten Spielers
	 * @return ob Spieler schon tot ist
	 */
	public boolean isDead(byte id)
	{
		return dead.get(id);
	}
	
	/**
	 * Setzt den Status des Spielers mit der angegebenen ID.
	 * Akzeptiert auch null als Status - für "nicht vorhanden".
	 * 
	 * @param id ID des Spielers
	 * @param isDead ob er als tot markiert sein soll
	 */
	public void setStatus(byte id, Boolean isDead)
	{
		dead.set(id, isDead);
	}
	
	/**
	 * Gibt das Spiel frei.
	 */
	public void unlock()
	{
		unlocked = true;
	}
	
	/**
	 * Zeigt an ob das Spiel freigegeben ist.
	 * Dies geschieht erst, wenn mindestens 2 Spieler gleichzeitig im Spiel
	 * waren.
	 * 
	 * @return ob das Spiel freigegeben wurde.
	 */
	public boolean isUnlocked()
	{
		return unlocked;
	}
}
