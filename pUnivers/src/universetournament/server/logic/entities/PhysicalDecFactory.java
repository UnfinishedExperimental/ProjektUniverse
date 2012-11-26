package universetournament.server.logic.entities;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.server.logic.controller.SimpleExtMovement;
import universetournament.shared.data.proto.PDecConfig;
import universetournament.shared.data.proto.util.ConfigurationManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.container.SimplePhysic;
import universetournament.shared.logic.entities.requests.PDecRequest;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;

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
