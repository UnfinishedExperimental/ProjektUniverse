package de.fhhof.universe.server.logic.entities;

import darwin.util.math.base.matrix.Matrix;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.server.events.buffers.PJUTEventBuffManager;
import de.fhhof.universe.shared.data.proto.RocketConfig;
import de.fhhof.universe.shared.data.proto.util.ConfigurationManager;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.ingame.RocketEntity;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.controller.ProjectileCollisionController;
import de.fhhof.universe.shared.logic.entities.ingame.controller.SimpleMovement;
import de.fhhof.universe.shared.logic.entities.requests.RocketRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.EntityController;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;

/**
 * Factory, die Raketen erstellt, sie an den entsprechenden Stellen registriert
 * und sie an die Clients verteilt. Dient außerdem zu deren Entfernung aus dem
 * System. Reagiert also auf CREATE und DELETE Events mit entsprechenden
 * Nutzdaten.
 *
 * @author sylence
 *
 */
public class RocketFactory implements PJUTEventHandler {

    @Override
    public void handleEvent(GameEvent event) {
        if (event != null && event.getData() instanceof EntityContainer) {
            EntityContainer container = (EntityContainer) event.getData();

            if (event.getSub() == Entity.SubEvents.CREATE
                    && container.getData() instanceof RocketRequest) {
                RocketRequest request = (RocketRequest) container.getData();

                RocketConfig config = ConfigurationManager.getInstance().
                        getConfiguration(RocketConfig.class,
                                request.getConfigId());

                RocketEntity rocket = new RocketEntity(
                        EntityFactory.getInstance().
                        getUniqueId(), request.getOwnerId(), config);

                ShipEntity se = ServerMainController.getInstance().
                        getEntities().getShip(rocket.getOwnerId());

                if (se != null) {
                    Matrix m = se.getTransformation().getModelMatrix().
                            getMinor(3, 3);

                    //TODO: mit waffen position ersetzten
                    Vector3 pos = m.mult(new Vector3(0, 0,
                            se.getTransformation().getRadius()
                            + rocket.getTransformation().getRadius()
                            + 0.25f)).toVector3().add(
                                    se.getTransformation().getModelMatrix().
                                    getTranslation());
                    Vector3 vel = m.mult(new Vector3(0, 0,
                            rocket.getConfiguration().getSpeed())).toVector3();

                    rocket.getTransformation().setPosition(pos);
                    rocket.getTransformation().setVelocity(vel);
                    rocket.getTransformation().rotate(
                            se.getTransformation().getModelMatrix());
                }

                //Controller
                EntityController<RocketEntity> controller
                        = new EntityController<>(rocket);
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
            } else if (event.getSub() == Entity.SubEvents.DELETE
                    && event instanceof EntityEvent) {
                short rid = ((EntityEvent) event).getId();

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
