package universetournament.server.logic.gamemode;

import universetournament.shared.logic.gamemode.UTTeamModeData;

/**
 * GameModeController für das Team Deathmatch, in dem zwei Teams gegeneinander
 * im Kampf um die meisten Kills antreten.
 * 
 * @author Bernd Marbach
 *
 */
public class UTTeamDeathMatch extends UTTeamGameMode
{
	public UTTeamDeathMatch(UTTeamModeData teamData)
	{
		super(teamData);
	}
}
