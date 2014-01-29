package de.fhhof.universe.shared.logic.entities.controller;

import de.fhhof.universe.shared.events.SubType;

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
