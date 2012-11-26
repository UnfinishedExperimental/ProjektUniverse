package universetournament.server.logic.entities;

import java.util.List;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.server.logic.controller.RelayMovement;
import universetournament.server.logic.controller.ServerDamageController;
import universetournament.shared.data.proto.BlasterConfig;
import universetournament.shared.data.proto.RLauncherConfig;
import universetournament.shared.data.proto.ShipConfig;
import universetournament.shared.data.proto.WeaponConfig;
import universetournament.shared.data.proto.WeaponContainer;
import universetournament.shared.data.proto.util.ConfigurationManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.sub.Blaster;
import universetournament.shared.logic.entities.ingame.sub.RocketLauncher;
import universetournament.shared.logic.entities.requests.ShipRequest;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * Factory, die Schiffe erstellt, sie an den entsprechenden Stellen registriert
 * und sie an die Clients verteilt.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author Florian Holzschuher
 *
 */
public class ShipFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof ShipRequest)
			{
				ShipRequest request = (ShipRequest) container.getData();
				
				ShipConfig config = ConfigurationManager.getInstance().
					getConfiguration(ShipConfig.class, request.getConfigId());
				
				//TODO: wenn Konfiguration nicht gefunden abbrechen
				
				ShipEntity ship = new ShipEntity(
						EntityFactory.getInstance().getUniqueId(),
						request.getOwnerId(), config);
				
				//initialen Status setzen
				setStatus(ship, config);
			
				createWeapons(ship);
				
				//Controller
				EntityController<ShipEntity> controller =
					new EntityController<ShipEntity>(ship);
				UTEntityManager.getInstance().setController(ship.getId(),
						controller);
				
				//in Liste eintragen
				ServerMainController.getInstance().getEntities().addShip(ship);
				
				//Schiff von Spielmodus setzen lassen
				ServerMainController.getInstance().getGameModeController().
					placeShip(ship);
				
				//Movement weiterleiten
				controller.addSubController(new RelayMovement(ship));
				
				//Schaden annehmen
				controller.addSubController(new ServerDamageController(ship));
				
				//Schiff an Clients verteilen
				container = new EntityContainer(EntityType.SHIP, ship);
				
				EntityEvent ee = new EntityEvent(ship.getId(),
						Entity.SubEvents.CREATE, container);
				
				PJUTEventBuffManager.getInstance().sendToAll(ee);
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short sid = ((EntityEvent)event).getId();
				
				ServerMainController.getInstance().getEntities().
					removeShip(sid);
				
				UTEntityManager.getInstance().setController(sid, null);
				
				//bei Clients löschen lassen
				container = new EntityContainer(EntityType.SHIP, null);
				
				EntityEvent ee = new EntityEvent(sid,
						Entity.SubEvents.DELETE, container);
				PJUTEventBuffManager.getInstance().sendToAll(ee);
				
				//ID freigeben
				EntityFactory.getInstance().freeUniqueId(sid);
			}
		}
	}

	private void setStatus(ShipEntity ship, ShipConfig config)
	{
		ship.setHitPoints(config.getMaxHitpoints());
		ship.setShieldEnergy(config.getShieldCapacity());
		ship.setRocketCount(config.getMaxRockets());
	}

	private void createWeapons(ShipEntity ship)
	{
		List<WeaponContainer> weapons = ship.getConfiguration().getWeapons();
		
		for(WeaponContainer wc : weapons)
		{
			WeaponConfig config = ConfigurationManager.getInstance().
			getConfiguration(WeaponConfig.class, wc.getWeaponId());
			
			if(config instanceof BlasterConfig)
			{
				Blaster b = new Blaster((BlasterConfig) config);
				ship.addBlaster(b);
			}
			else if(config instanceof RLauncherConfig)
			{
				RocketLauncher rl = new RocketLauncher(
						(RLauncherConfig)config);
				ship.setLauncher(rl);
			}
		}
	}
}
