package universetournament.shared.logic.entities.controller;

import universetournament.shared.events.SubType;

/**
 * Kollisions-Event-Typen.
 * 
 * @author sylence
 *
 */
public enum CollisionType implements SubType
{
	NOTIFY,
	
	DAMAGE,
	
	MOVEMENT;
}
