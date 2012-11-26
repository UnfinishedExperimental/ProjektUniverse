package universetournament.shared.logic.entities.ingame.controller;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.controller.CollisionType;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.container.DamageContainer;
import universetournament.shared.logic.entities.util.EntitySubController;

/**
 * Nimmt den Schaden auf, der über eine Kollision verursacht wird.
 * 
 * @author sylence
 *
 */
public class ShipDamageController extends EntitySubController<ShipEntity>
{
	/**
	 * Erzeugt einen Schadens-Controller für die angegebene Entity.
	 * Wirft eine NullPointerException, wenn diese null ist.
	 * 
	 * @param entity zu manipulierende Entity
	 */
	public ShipDamageController(ShipEntity entity)
	{
		super(entity);
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getData() instanceof DamageContainer)
		{
			DamageContainer damage = (DamageContainer)event.getData();
			
			entity.modShieldEnergy(damage.getShieldDamage());
			if(entity.getShieldEnergy() <= 0)
			{
				entity.modHitPoints(damage.getHullDamage());
			}
		}
	}

	@Override
	public SubType[] getTypes()
	{
		SubType types[] = {CollisionType.DAMAGE};
		return types;
	}
}
