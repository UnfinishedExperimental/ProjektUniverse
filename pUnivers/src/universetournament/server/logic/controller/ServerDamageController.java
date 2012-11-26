package universetournament.server.logic.controller;

import universetournament.client.logic.entities.controller.RechargeController;
import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.controller.CollisionType;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.container.DamageContainer;
import universetournament.shared.logic.entities.ingame.controller.ShipDamageController;
import universetournament.shared.logic.gamemode.events.GameModeSubType;

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
