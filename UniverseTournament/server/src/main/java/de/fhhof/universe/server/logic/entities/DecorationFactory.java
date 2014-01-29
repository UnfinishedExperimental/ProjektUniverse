package de.fhhof.universe.server.logic.entities;

import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;

/**
 * Factory, die Dekorationsobjekte erstellt, sie an den entsprechenden Stellen
 * registriert und sie an die Clients verteilt.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author Florian Holzschuher
 *
 */
public class DecorationFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		//TODO: nichtmehr implementiert
	}
}
