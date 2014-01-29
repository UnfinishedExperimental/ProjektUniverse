package de.fhhof.universe.server.logic.entities;

import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.server.logic.controller.SimpleExtMovement;
import de.fhhof.universe.shared.data.proto.PDecConfig;
import de.fhhof.universe.shared.data.proto.util.ConfigurationManager;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.entities.ingame.container.SimplePhysic;
import de.fhhof.universe.shared.logic.entities.requests.PDecRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.EntityController;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;

/**
 * Factory, die Dekorationsobjekte mit Kollisionsabfrage erstellt, sie an den
 * entsprechenden Stellen registriert und sie an die Clients verteilt.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author Florian Holzschuher
 *
 */
public class PhysicalDecFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getData() instanceof PDecRequest)
		{
			PDecRequest pdr = (PDecRequest)event.getData();
			
			PDecConfig config = ConfigurationManager.getInstance().
				getConfiguration(PDecConfig.class, pdr.getConfig());
			
			WorldEntity<PhysicContainer> dec = new WorldEntity<PhysicContainer>
				(EntityFactory.getInstance().getUniqueId(), config,
						new SimplePhysic(config.getpConf()));
			
			dec.getTransformation().setWorldPosition(pdr.getPosition());
			
			ServerMainController.getInstance().getEntities().addWorldEntity(dec);
			
			EntityController<WorldEntity<PhysicContainer>> controller =
				new EntityController<WorldEntity<PhysicContainer>>(dec);
			UTEntityManager.getInstance().setController(dec.getId(), controller);
			
			SimpleExtMovement mov = new SimpleExtMovement(dec);
			controller.addSubController(mov);
			controller.addTimedController(mov);
			
			//Objekt an Clients verteilen
			EntityContainer container = new EntityContainer(
					EntityType.PHYSICAL_DEC, dec);
			
			EntityEvent ee = new EntityEvent(dec.getId(),
					Entity.SubEvents.CREATE, container);
			
			PJUTEventBuffManager.getInstance().sendToAll(ee);
		}
	}
}
