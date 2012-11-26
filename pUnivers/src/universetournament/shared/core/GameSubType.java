package universetournament.shared.core;

import universetournament.shared.events.SubType;

/**
 * Enum in der alle Subtypen für die Initialisierung des Spiels liegen.
 * 
 * @author Florian Holzschuher
 *
 */
public enum GameSubType implements SubType
{
	AUTH_REQUEST,
	
	PASSWORD,
	
	PWD_CONFIRM,
	
	PWD_REJECT,
	
	REQUEST_GAME,
	
	START_GAME,
	
	KICK;
}
