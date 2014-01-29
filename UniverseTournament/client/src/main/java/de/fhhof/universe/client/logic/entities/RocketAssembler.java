package de.fhhof.universe.client.logic.entities;

import java.util.HashMap;
import de.fhhof.universe.client.core.ClientMainController;
import de.fhhof.universe.client.logic.entities.controller.RocketTrailController;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.*;
import de.fhhof.universe.shared.logic.entities.ingame.RocketEntity;
import de.fhhof.universe.shared.logic.entities.ingame.controller.SimpleMovement;
import de.fhhof.universe.shared.logic.entities.util.*;

public class RocketAssembler implements PJUTEventHandler {

    private final HashMap<Short, Renderable> renderables;

    public RocketAssembler() {
        renderables = new HashMap<Short, Renderable>();
    }

    @Override
    public void handleEvent(GameEvent event) {
        if (event != null && event.getData() instanceof EntityContainer) {
            EntityContainer container = (EntityContainer) event.getData();

            if (event.getSub() == Entity.SubEvents.CREATE
                    && container.getData() instanceof RocketEntity) {
                RocketEntity rocket = (RocketEntity) container.getData();

                try {
                    Renderable rend = rocket.buildRenderObject();
                    renderables.put(rocket.getId(), rend);
                    Scene.getInstance().addRenderable(rend);
                } catch (InstantiationException ex) {
                    // TODO: Exception handling verbessern
                    throw new RuntimeException(
                            "RenderObject konnte nicht initialisiert werden."
                            + "Fehler in der RenderConfiguration(\""
                            + rocket.getConfiguration().getName() + "\")");
                } catch (IllegalAccessException ex) {
                    throw new RuntimeException(
                            "RenderObject konnte nicht initialisiert"
                            + " werden. Fehler in der RenderConfiguration(\""
                            + rocket.getConfiguration().getName() + "\")");
                }

                //Controller registrieren
                EntityController<RocketEntity> controller
                        = new EntityController<RocketEntity>(rocket);
                UTEntityManager.getInstance().setController(rocket.getId(),
                        controller);

                //TODO: Bewegung mit Lerp
                SimpleMovement mov = new SimpleMovement(rocket);

                controller.addSubController(mov);
                controller.addTimedController(mov);
                controller.addTimedController(new RocketTrailController(
                        rocket, 0.3f));

                //Schuss ins System übernehmen
                ClientMainController.getInstance().getEntities().
                        addRocket(rocket);
            } else if (event.getSub() == Entity.SubEvents.DELETE
                    && event instanceof EntityEvent) {
                short rid = ((EntityEvent) event).getId();

                //Rakete mit ID entfernen
                ClientMainController.getInstance().getEntities().
                        removeRocket(rid);

                UTEntityManager.getInstance().setController(rid, null);

                Renderable rend = renderables.get(rid);
                Scene.getInstance().removeRenderable(rend);
            }
        }
    }
}
