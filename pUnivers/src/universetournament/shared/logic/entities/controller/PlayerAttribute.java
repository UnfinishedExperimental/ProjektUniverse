package universetournament.shared.logic.entities.controller;

import universetournament.shared.events.SubType;

/**
 * Enum, deren Elemente Attribute eines Spielers identifizieren.
 * 
 * @author sylence
 *
 */
public enum PlayerAttribute implements SubType
{
	COLOR,
	
	SCORE,
	
	KILLS,
	
	DEATHS;
}
