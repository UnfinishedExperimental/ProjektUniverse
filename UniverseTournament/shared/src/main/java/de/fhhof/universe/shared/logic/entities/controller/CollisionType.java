package de.fhhof.universe.shared.logic.entities.controller;

import de.fhhof.universe.shared.events.SubType;

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
