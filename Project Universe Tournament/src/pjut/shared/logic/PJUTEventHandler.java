package pjut.shared.logic;

import pjut.shared.events.PJUTEvent;

/**
 * Interface für Handler, die sich bei dem EventReceiver registrieren lassen
 * und dann übergebene Events bearbeiten.
 * Von ihnen wird erwartet, das Type-Casting selbst zu übernehmen, je nachdem
 * welche Event-Klassen sie bearbeiten können.
 * 
 * @author sylence
 *
 */
public interface PJUTEventHandler
{
	/**
	 * Ankommendes Event bearbeiten, wenn es verwertbar ist.
	 * 
	 * @param event ankommendes Event
	 */
	public abstract void handleEvent(PJUTEvent event);
}
