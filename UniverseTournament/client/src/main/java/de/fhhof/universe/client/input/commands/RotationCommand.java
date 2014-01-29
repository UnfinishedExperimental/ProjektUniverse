package de.fhhof.universe.client.input.commands;

import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.client.input.PJUTInputBuffer;
import de.fhhof.universe.client.logic.entities.controller.PlayerMovement;
import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.TransformationContainer;

/**
 * Kommando, welches das Schiff zur Rotation in eine Richtung veranlasst, wobei
 * die Richtung von der Position des Mauszeigers abhängt. Muss nicht gebunden
 * werden, holt sich seine Daten selbst.
 *
 * @author Florian Holzschuher
 *
 */
public class RotationCommand implements PJUTTimedRefreshable {

    private final TransformationContainer container;
    private final PlayerMovement pm;
    private final Vector3 accelRot;
    private final PJUTInputBuffer buffer;

    public RotationCommand(ShipEntity se, PlayerMovement pm) {
        if (se == null) {
            throw new NullPointerException("Schiff war null");
        }
        if (pm == null) {
            throw new NullPointerException("Movement war null");
        }

        this.pm = pm;
        container = se.getTransformation();
        accelRot = new Vector3(0.f, se.getConfiguration().getAccRot(), 0.f);
        buffer = PJUTInputBuffer.getInstance();
    }

    @Override
    public void refresh(float timeDiff) {
        timeDiff /= 1000000000f;

        float pos = -buffer.getMouseRelX();

        /*
         * die übergebene Zeit lang beschleunigen, je nach Mausposition mehr
         * oder weniger stark.
         */
        Vector3 rotation = accelRot.mul(timeDiff * pos);
        container.rotateEuler(rotation);
        pm.setRotation(rotation);
    }
}
