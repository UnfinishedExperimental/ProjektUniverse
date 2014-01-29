package de.fhhof.universe.server.logic.controller;

import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.controller.CollisionType;
import de.fhhof.universe.shared.logic.entities.controller.RechargeController;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.DamageContainer;
import de.fhhof.universe.shared.logic.entities.ingame.controller.ShipDamageController;
import de.fhhof.universe.shared.logic.gamemode.events.GameModeSubType;

/**
 * Schiffs-Schadenscontroller, welcher zusätzlich den Schaden an den
 * Eigentümer-Client des Schiffs übermittelt und Schiffe zerstört.
 * Dient auch, um Schild-Auflade-Events entgegenzunehmen.
 * 
 * @author Florian Holzschuher
 *
 */
public class ServerDamageController extends ShipDamageController
{
	/**
	 * Erzeugt einen Schadens-Controller für die angegebene Entity.
	 * Wirft eine NullPointerException, wenn diese null ist.
	 * 
	 * @param entity zu manipulierende Entity
	 */
	public ServerDamageController(ShipEntity entity)
	{
		super(entity);
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getSub() == RechargeController.SubTypes.RECHARGE
				&& event.getData() instanceof Short)
		{
			entity.modShieldEnergy((Short)event.getData());
		}
		else if(event.getData() instanceof DamageContainer)
		{
			DamageContainer damage = (DamageContainer)event.getData();
			
			entity.modShieldEnergy(damage.getShieldDamage());
			if(entity.getShieldEnergy() <= 0)
			{
				entity.modHitPoints(damage.getHullDamage());
				if(entity.getHitPoints() <= 0)
				{
					//getöteter Spieler
					GameEvent ge = new GameEvent(MainType.TYPE_GAMEMODE,
							GameModeSubType.PLAYER_DIED, entity.getPilotId());
					ServerMainController.getInstance().getGameModeController().
						handleEvent(ge);
					
					//tötender Spieler
					ge = new GameEvent(MainType.TYPE_GAMEMODE,
							GameModeSubType.PLAYER_KILLED, damage.getCause());
					ServerMainController.getInstance().getGameModeController().
						handleEvent(ge);
				}
			}
			
			//Schaden an Client übermitteln
			EntityEvent ee = new EntityEvent(entity.getId(),
					CollisionType.DAMAGE, damage);
			PJUTEventBuffManager.getInstance().sendToId(entity.getPilotId(),
					ee);
		}
	}
}
