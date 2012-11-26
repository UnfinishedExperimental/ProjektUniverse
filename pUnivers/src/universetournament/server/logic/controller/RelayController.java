package universetournament.server.logic.controller;

import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.util.EntitySubController;

/**
 * Controller, der einfach alle ankommenden Events an alle weiterleitet.
 * 
 * @author Florian Holzschuher
 *
 */
public class RelayController<T extends Entity> extends EntitySubController<T>
{
	private final SubType[] types;
	
	/**
	 * Erstellt einen RelayController, welcher alle Events der angegebenen
	 * Typen an alle Clients weiterleitet.
	 * 
	 * @param entity zugeordnete Entity
	 * @param types weiterzuleitende Typen
	 */
	public RelayController(T entity, SubType[] types)
	{
		super(entity);
		this.types = types;
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		PJUTEventBuffManager.getInstance().sendToAll(event);
	}

	@Override
	public SubType[] getTypes()
	{
		return types;
	}
}

	
