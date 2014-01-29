package de.fhhof.universe.server.logic.entities;

import darwin.util.math.base.matrix.Matrix;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.shared.data.proto.ProjectileConfig;
import de.fhhof.universe.shared.data.proto.util.ConfigurationManager;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.ShotEntity;
import de.fhhof.universe.shared.logic.entities.ingame.controller.ProjectileCollisionController;
import de.fhhof.universe.shared.logic.entities.ingame.controller.SimpleMovement;
import de.fhhof.universe.shared.logic.entities.requests.ShotRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.EntityController;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;

/**
 * Factory, die Blasterprojektile erstellt, sie an den entsprechenden Stellen
 * registriert und sie an die Clients verteilt.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author Florian Holzschuher
 *
 */
public class ShotFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof ShotRequest)
			{
				ShotRequest request = (ShotRequest)container.getData();
				
				ProjectileConfig config = ConfigurationManager.getInstance().
					getConfiguration(ProjectileConfig.class,
							request.getConfigId());
				
				//TODO: wenn Konfiguration nicht gefunden abbrechen
				
				ShotEntity shot = new ShotEntity(EntityFactory.getInstance().
						getUniqueId(), request.getOwnerId(), config);
				
				ShipEntity se = ServerMainController.getInstance().
					getEntities().getShip(shot.getOwnerId());
				
				if(se != null)
				{
                                    Matrix m = se.getTransformation().getModelMatrix().
                                            getMinor(3, 3);
                    
					//TODO: mit waffen position ersetzten
					Vector3 pos = m.mult(new Vector3(0, 0,
							se.getTransformation().getRadius()
							+ shot.getTransformation().getRadius()
							+ 0.25f)).toVector3().add(
							se.getTransformation().getModelMatrix().
							getTranslation());
					Vector3 vel = m.mult(new Vector3(0, 0,
							shot.getConfiguration().getSpeed())).toVector3();
					
					shot.getTransformation().setPosition(pos);
					shot.getTransformation().setVelocity(vel);
                    shot.getTransformation().rotate(se.getTransformation().
                    		getModelMatrix());
				}
				
				
				//Controller
				EntityController<ShotEntity> controller =
					new EntityController<>(shot);
				UTEntityManager.getInstance().setController(shot.getId(),
						controller);
				
				//Controller + intern registrieren
				SimpleMovement mov = new SimpleMovement(shot);
				controller.addSubController(mov);
				controller.addTimedController(mov);
				
				//Schaden austeilen bei Kollision
				controller.addSubController(new ProjectileCollisionController(
						shot));
				
				//In Collection eintragen
				ServerMainController.getInstance().getEntities().addShot(shot);
				
				//Schuss an Clients verteilen
				container = new EntityContainer(EntityType.SHOT, shot);
				
				EntityEvent ee = new EntityEvent(shot.getId(),
						Entity.SubEvents.CREATE, container);
				
				PJUTEventBuffManager.getInstance().sendToAll(ee);
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short sid = ((EntityEvent)event).getId();
				
				//intern wieder deregistrieren (Logik/Controller)
				ServerMainController.getInstance().getEntities().
					removeShot(sid);
				
				UTEntityManager.getInstance().setController(sid, null);
				
				//bei Clients löschen lassen
				container = new EntityContainer(EntityType.SHOT, null);
				
				EntityEvent ee = new EntityEvent(sid,
						Entity.SubEvents.DELETE, container);
				PJUTEventBuffManager.getInstance().sendToAll(ee);
				
				//ID freigeben
				EntityFactory.getInstance().freeUniqueId(sid);
			}
		}
	}
}
