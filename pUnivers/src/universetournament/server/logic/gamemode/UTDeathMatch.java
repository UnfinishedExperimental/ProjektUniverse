package universetournament.server.logic.gamemode;

import universetournament.shared.logic.gamemode.UTGameModeData;

/**
 * GameModeController für das Deathmatch, in dem alle Spieler gegeneinander
 * im Kampf um die meisten Kills antreten.
 * 
 * @author Bernd Marbach
 *
 */
public class UTDeathMatch extends UTGameModeController
{
	public UTDeathMatch(UTGameModeData gameData)
	{
		super(gameData);
	}
}
