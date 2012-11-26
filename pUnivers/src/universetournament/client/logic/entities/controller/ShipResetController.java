package universetournament.client.logic.entities.controller;

import universetournament.shared.data.proto.ShipConfig;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.util.EntitySubController;

/**
 * Controller, welcher auf ein Event hin die Schiffs-Werte resettet.
 * 
 * @author sylence
 *
 */
public class ShipResetController extends EntitySubController<ShipEntity>
{
	/**
	 * Enum, die lediglich den Reset-Typ enthält.
	 * 
	 * @author sylence
	 *
	 */
	public enum SubEvents implements SubType
	{
		RESET;
	}
	
	/**
	 * Erzeugt einen Reset-Controller, welcher das übergebene Schiff
	 * zurücksetzt.
	 * Wirft eine NullPointerException, wenn das Schiff null ist.
	 * 
	 * @param entity zurückzusetzendes Schiff
	 */
	public ShipResetController(ShipEntity entity)
	{
		super(entity);
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getSub() == SubEvents.RESET)
		{
			ShipConfig conf = entity.getConfiguration();
			entity.setHitPoints(conf.getMaxHitpoints());
			entity.setRocketCount(conf.getMaxRockets());
			entity.setShieldEnergy(conf.getShieldCapacity());
		}
	}

	@Override
	public SubType[] getTypes()
	{
		SubType types[] = {SubEvents.RESET};
		return types;
	}
}
