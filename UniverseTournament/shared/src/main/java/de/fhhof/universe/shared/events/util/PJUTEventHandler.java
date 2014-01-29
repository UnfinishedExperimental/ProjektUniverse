package de.fhhof.universe.shared.events.util;

import de.fhhof.universe.shared.events.GameEvent;


/**
 * Interface für Handler, die sich bei dem EventReceiver registrieren lassen
 * und dann übergebene Events bearbeiten.
 * Von ihnen wird erwartet, das Type-Casting selbst zu übernehmen, je nachdem
 * welche Event-Klassen sie bearbeiten können.
 * 
 * @author Florian Holzschuher
 *
 */
public interface PJUTEventHandler
{
	/**
	 * Ankommendes Event bearbeiten, wenn es verwertbar ist.
	 * 
	 * @param event ankommendes Event
	 */
	public abstract void handleEvent(GameEvent event);
}
