package universetournament.server.logic.entities;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;

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
