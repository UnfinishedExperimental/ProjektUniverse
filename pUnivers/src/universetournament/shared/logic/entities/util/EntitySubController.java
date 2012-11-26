package universetournament.shared.logic.entities.util;

import universetournament.shared.events.SubType;
import universetournament.shared.events.util.PJUTEventHandler;

/**
 * Ein EntitySubController dient zur konkreten Abarbeitung eines oder mehrerer
 * Sub-Event-Typen und erhält eine Referenz auf ein Objekt mit dem benötigten
 * Inteface, welches bearbeitet werden soll.
 * Über die Generics wird sichergestellt, dass der Controller
 * hier konkrete Interfaces erhält, die er benötigt, wie ein Objekt, für
 * welches Kollisionen berechnet werden sollen.
 * 
 * @author sylence
 *
 * @param <T> Interface oder Klasse, die vom Controller benötigt wird
 */
public abstract class EntitySubController<T> implements PJUTEventHandler
{
	/**
	 * Entity-Controller, dem der Unter-Controller zugeordnet ist.
	 */
	protected final T entity;
	
	/**
	 * Erstellt einen SubController, der Zugriff auf das übergebene Objekt hat.
	 * Erzeugt eine NullPointerException, wenn das übergebene Objekt null ist.
	 * 
	 * @param entity Enitity, zu der der Controller gehört.
	 */
	public EntitySubController(T entity)
	{
		if(entity != null)
		{
			this.entity = entity;
		}
		else
		{
			throw new NullPointerException("Null als Entity ist ungültig");
		}
	}
	
	/**
	 * @return Untertypen, die der Controller bearbeiten kann oder soll.
	 */
	public abstract SubType[] getTypes();
}
