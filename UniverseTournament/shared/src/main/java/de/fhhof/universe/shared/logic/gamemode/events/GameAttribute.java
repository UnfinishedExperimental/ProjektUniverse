package de.fhhof.universe.shared.logic.gamemode.events;

import de.fhhof.universe.shared.events.SubType;

/**
 * Event-Typen, welche Veränderungen am Spielstatus darstellen.
 * 
 * @author Bernd Marbach
 *
 */
public enum GameAttribute implements SubType
{
	PLAYER_ADD,
	
	PLAYER_REM,
	
	TEAM_JOIN,
	
	TEAM_LEAVE,
	
	SCORE_RED,
	
	SCORE_BLUE,
	
	PLAYER_DIED,
	
	GAME_OVER;
}
