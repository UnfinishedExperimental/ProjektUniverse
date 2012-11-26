package universetournament.server.logic.entities;

import java.util.List;
import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.data.proto.RocketConfig;
import universetournament.shared.data.proto.util.ConfigurationManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.ingame.RocketEntity;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.controller.ProjectileCollisionController;
import universetournament.shared.logic.entities.ingame.controller.RocketMovment;
import universetournament.shared.logic.entities.ingame.controller.SimpleMovement;
import universetournament.shared.logic.entities.requests.RocketRequest;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.util.math.Matrix;
import universetournament.shared.util.math.Vec3;

/**
 * Factory, die Raketen erstellt, sie an den entsprechenden Stellen registriert
 * und sie an die Clients verteilt.
 * Dient außerdem zu deren Entfernung aus dem System.
 * Reagiert also auf CREATE und DELETE Events mit entsprechenden Nutzdaten.
 * 
 * @author sylence
 *
 */
public class RocketFactory implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof RocketRequest)
			{
				RocketRequest request = (RocketRequest)container.getData();
				
				RocketConfig config = ConfigurationManager.getInstance().
					getConfiguration(RocketConfig.class,
					request.getConfigId());
				
				RocketEntity rocket = new RocketEntity(
						EntityFactory.getInstance().
						getUniqueId(), request.getOwnerId(), config);
				
				ShipEntity se = ServerMainController.getInstance().
				getEntities().getShip(rocket.getOwnerId());
				
				if(se != null)
				{
					Matrix m = se.getTransformation().getModelMatrix().
						getMinor(3, 3);
                    
					//TODO: mit waffen position ersetzten
					Vec3 pos = new Vec3(m.mult(new Vec3(0, 0,
							se.getTransformation().getRadius()
							+ rocket.getTransformation().getRadius()
							+ 0.25f))).add(
							se.getTransformation().getModelMatrix().
							getTranslation());
					Vec3 vel = new Vec3(m.mult(new Vec3(0, 0,
							rocket.getConfiguration().getSpeed())));
					
					rocket.getTransformation().setPosition(pos);
					rocket.getTransformation().setVelocity(vel);
                    	rocket.getTransformation().rotate(
                    			se.getTransformation().getModelMatrix());
				}
				
				//Controller
				EntityController<RocketEntity> controller =
					new EntityController<RocketEntity>(rocket);
				UTEntityManager.getInstance().setController(rocket.getId(),
						controller);
				
				//Schaden austeilen bei Kollision
				controller.addSubController(new ProjectileCollisionController(
						rocket));
                                
//                                ShipEntity max = null;
//				                        {
//                                List<ShipEntity> ships = ServerMainController.
//                                        getInstance().getEntities().getShips();
//                                float maxcos = 0;
//                                Vec3f dir = new Vec3f(rocket.getTransformation().
//                                        getModelMatrix().getMinor(3, 3).
//                                        mult(new Vec3f(0, 0, 1)));
//                                    dir.normalize(dir);
//                                for (ShipEntity s : ships) {
//                                    if(s.equals(se))
//                                        continue;
//                                    Vec3f tdir = s.getTransformation().
//                                            getPosition().
//                                            sub(rocket.getTransformation().
//                                            getPosition());
//
//                                    tdir.normalize(tdir);
//                                    float cos = dir.dot(tdir);
//                                    if(cos < 0 )
//                                        continue;
//                                    if (max == null || cos > maxcos) {
//                                        max = s;
//                                        maxcos = cos;
//                                    }
//                                }
//                            }
//
//				SimpleMovement mov = new RocketMovment(rocket,
//                                                                       max);
				SimpleMovement mov = new SimpleMovement(rocket);
				controller.addSubController(mov);
				controller.addTimedController(mov);
				
				//In Collection eintragen
				ServerMainController.getInstance().getEntities().
					addRocket(rocket);
				
				//Rakete an Clients verteilen
				container = new EntityContainer(EntityType.ROCKET, rocket);
				
				EntityEvent ee = new EntityEvent(rocket.getId(),
					Entity.SubEvents.CREATE, container);
				
				PJUTEventBuffManager.getInstance().sendToAll(ee);
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short rid = ((EntityEvent)event).getId();
				
				ServerMainController.getInstance().getEntities().
					removeRocket(rid);
				
				UTEntityManager.getInstance().setController(rid, null);
				
				//bei Clients löschen lassen
				container = new EntityContainer(EntityType.ROCKET, null);
				
				EntityEvent ee = new EntityEvent(rid,
						Entity.SubEvents.DELETE, container);
				PJUTEventBuffManager.getInstance().sendToAll(ee);
				
				//ID freigeben
				EntityFactory.getInstance().freeUniqueId(rid);
			}
		}
	}
}
