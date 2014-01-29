package de.fhhof.universe.shared.logic.gamemode.events;

import de.fhhof.universe.shared.events.SubType;

/**
 * 
 * @author Bernd Marbach
 *
 */
public enum GameModeSubType implements SubType
{
	SET_TIME,
	
	PLAYER_JOINED,
	PLAYER_LEFT,
	
	PLAYER_SCORE_CHANGED,
	TEAM_SCORE_CHANGED,
	
	PLAYER_KILLED,
	PLAYER_DIED;
}
