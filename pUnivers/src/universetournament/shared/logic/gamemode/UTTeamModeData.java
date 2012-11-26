package universetournament.shared.logic.gamemode;

import java.util.HashMap;

/**
 * Klasse, die die überall bekannten Daten für einen Team-basierten Spielmodus
 * enthält.
 * 
 * @author Bernd Marbach
 *
 */
public class UTTeamModeData extends UTGameModeData
{
	private HashMap<Byte, Boolean> teamDist;
	private short teamScoreRed, teamScoreBlue;
	
	/**
	 * Erstellt Spielmodusdaten mit der angegebenen Restzeit und dem
	 * angegebenen Punktelimit noch ohne Teamverteilung.
	 * 
	 * @param id ID der Entity
	 * @param timeLeft verbleibende Zeit
	 * @param scoreLimit Punktegrenze
	 */
	public UTTeamModeData(short id, double timeLeft, short scoreLimit)
	{
		super(id, timeLeft, scoreLimit);
		
		teamDist = new HashMap<Byte, Boolean>();
		
		teamScoreRed = 0;
		teamScoreBlue = 0;
	}
	
	/**
	 * @param id ID des Spielers
	 * @param blue ob er im blauen Team sein soll
	 */
	public void setTeam(byte id, boolean blue)
	{
		teamDist.put(id, blue);
	}
	
	/**
	 * @param id ID des Spielers
	 * @return ob Spieler im blauen Team ist oder null
	 */
	public Boolean getTeam(byte id)
	{
		return teamDist.get(id);
	}
	
	/**
	 * @param id ID des Spielers, dessen Team gelöscht werden soll
	 */
	public void unsetTeam(byte id)
	{
		teamDist.remove(id);
	}

	/**
	 * @param teamScoreRed neue Punktzahl für rotes Team
	 */
	public void setTeamScoreRed(short teamScoreRed)
	{
		this.teamScoreRed = teamScoreRed;
	}
	
	/**
	 * @param diff Änderung des roten Punktestands
	 */
	public void changeTeamScoreRed(short diff)
	{
		teamScoreRed += diff;
	}

	/**
	 * @return aktuelle rote Punktzahl
	 */
	public short getTeamScoreRed()
	{
		return teamScoreRed;
	}

	/**
	 * @param teamScoreBlue neue Punktzahl des blauen Teams
	 */
	public void setTeamScoreBlue(short teamScoreBlue)
	{
		this.teamScoreBlue = teamScoreBlue;
	}
	
	/**
	 * @param diff Änderung des blauen Punktestands
	 */
	public void changeTeamScoreBlue(short diff)
	{
		teamScoreBlue += diff;
	}

	/**
	 * @return aktuelle blaue Punktzahl
	 */
	public short getTeamScoreBlue()
	{
		return teamScoreBlue;
	}
}
