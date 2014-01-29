package de.fhhof.universe.client.logic.entities;

import darwin.geometrie.unpacked.Model;
import darwin.renderer.geometrie.packed.RenderObjekt;
import darwin.renderer.shader.Shader;
import darwin.resourcehandling.dependencies.annotation.InjectBundle;
import darwin.resourcehandling.dependencies.annotation.InjectResource;
import darwin.resourcehandling.resmanagment.ObjConfig;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.client.communication.PJUTCliConnector;
import java.util.HashMap;

import de.fhhof.universe.client.core.ClientMainController;
import de.fhhof.universe.client.logic.entities.container.LinkedTransformation;
import de.fhhof.universe.client.logic.entities.controller.CooldownController;
import de.fhhof.universe.client.logic.entities.controller.HUDrefresher;
import de.fhhof.universe.client.logic.entities.controller.LerpMovment;
import de.fhhof.universe.client.logic.entities.controller.PlayerMovement;
import de.fhhof.universe.client.logic.entities.controller.ShieldAnimator;
import de.fhhof.universe.client.rendering.Renderable;
import de.fhhof.universe.client.rendering.Scene;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.*;
import de.fhhof.universe.shared.logic.entities.controller.RechargeController;
import de.fhhof.universe.shared.logic.entities.controller.ShipResetController;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.controller.ShipDamageController;
import de.fhhof.universe.shared.logic.entities.util.*;
import java.util.Map;
import javafx.scene.transform.Scale;

/**
 * Erhält vom Server erzeugte Schiff-Entites und übernimmt sie ins lokale System
 * des Clients oder entfernt sie wieder.
 *
 * @author sylence
 *
 */
public class ShipAssembler implements PJUTEventHandler {

    private final Map<Short, Renderable[]> renderables = new HashMap<>();

    @InjectBundle(files = {"Shield.vert", "Shield.frag"})
    private Shader shieldShader;

    @InjectResource(file = "Models/uvsphere32.obj", options = {"width:1"})
    private Model[] oc;

    @Override
    public void handleEvent(GameEvent event) {
        if (event != null && event.getData() instanceof EntityContainer) {
            EntityContainer container = (EntityContainer) event.getData();

            if (event.getSub() == Entity.SubEvents.CREATE
                    && container.getData() instanceof ShipEntity) {
                ShipEntity ship = (ShipEntity) container.getData();

                EntityController<ShipEntity> controller
                        = new EntityController<ShipEntity>(ship);
                UTEntityManager.getInstance().setController(ship.getId(),
                        controller);

                //Renderer
                try {
                    RenderObjekt rend = ship.buildRenderObject();

                    LinkedTransformation lt = new LinkedTransformation(
                            rend.getTransf());
                    float r
                            = ship.getConfiguration().getPhysic_props().getRadius();
                    lt.scale(new Vector3(r, r, r));

                    RenderObjekt shield = new RObject3(shieldShader, lt);
                    RessourcesLoader.getInstance().loadRenderObjekt(
                            shield, oc, true);

                    PJUTPlayer p = ClientMainController.getInstance().
                            getEntities().getPlayer(ship.getPilotId());
                    float[] c = p.getColor().getRGBColorComponents(null);
                    shield.getModels()[0].getMaterial().setDiffuse(c);

                    addRenderable(ship.getId(), new Renderable[]{rend, shield});

                    //Schild animieren
                    ShieldAnimator sa = new ShieldAnimator(shieldShader);
                    controller.addTimedController(sa);
                } catch (InstantiationException ex) {
                    throw new RuntimeException(
                            "RenderObject konnte nicht initialisiert werden."
                            + "Fehler in der RenderConfiguration(\""
                            + ship.getConfiguration().getName() + "\")");
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(
                            "RenderObject konnte nicht initialisiert"
                            + " werden. Fehler in der RenderConfiguration(\""
                            + ship.getConfiguration().getName() + "\")");
                }

                //überprüfen ob eigenes Schiff
                if (ship.getPilotId() == ClientMainController.getInstance()
                        .getClientId()) {
                    HUDrefresher hudref = new HUDrefresher(ship);
                    controller.addTimedController(hudref);
                    PlayerMovement mov = new PlayerMovement(ship);
                    ClientMainController.getInstance().setPlayerShip(ship,
                            mov);

                    controller.addSubController(mov);
                    controller.addTimedController(mov);
                    controller.addTimedController(new CooldownController(
                            ship));
                    controller.addTimedController(new RechargeController(PJUTCliConnector.getInstance().getBuffer(),
                            ship));
                    controller.addSubController(new ShipDamageController(
                            ship));
                    controller.addSubController(new ShipResetController(
                            ship));
                } else {
                    LerpMovment mov = new LerpMovment(ship);

                    controller.addSubController(mov);
                    controller.addTimedController(mov);
                }

                ClientMainController.getInstance().getEntities().addShip(ship);
            } else if (event.getSub() == Entity.SubEvents.DELETE
                    && event instanceof EntityEvent) {
                short sid = ((EntityEvent) event).getId();

                UTEntityManager.getInstance().setController(sid, null);

                ClientMainController.getInstance().getEntities().
                        removeShip(sid);
                removeRenderable(sid);
            }
        }
    }

    private void addRenderable(short sid, Renderable[] rend) {
        if (rend != null) {
            renderables.put(sid, rend);
            for (Renderable r : rend) {
                Scene.getInstance().addRenderable(r);
            }
        }
    }

    private void removeRenderable(short sid) {
        if (renderables.get(sid) != null) {
            for (Renderable r : renderables.get(sid)) {
                Scene.getInstance().removeRenderable(r);
            }
            renderables.remove(sid);
        }
    }
}
