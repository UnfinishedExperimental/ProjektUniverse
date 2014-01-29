package de.fhhof.universe.client.input.commands;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import de.fhhof.universe.client.input.PJUTCommand;
import de.fhhof.universe.client.logic.entities.controller.PlayerMovement;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;

/**
 * Bewegungskommando, welches an eine Spieler-Bewegungs-Controller gebunden
 * wird.
 * Es kann für Bewegung in alle vier definierten Richtungen genutzt werden.
 * Addiert auf den temporären Beschleunigungsvektor des Controllers einen
 * definierten Wert auf.
 * 
 * @author Florian Holzschuher
 *
 */
public class MovementCommand implements PJUTCommand
{
	/**
	 * Enum, welche die Bewegungsrichtung angibt.
	 * 
	 * @author Florian Holzschuher
	 *
	 */
    public enum MOVE_DIRECTION
    {
        /**
         * Haupttriebwerk.
         */
        MAIN_THRUST,
        /**
         * Umkehrschub.
         */
        BACK_THRUST,
        /**
         * Seitwärtsschub nach links.
         */
        STRAFE_LEFT,
        /**
         * Seitwärtsschub nach rechts.
         */
        STRAFE_RIGHT;

    }
    private final ImmutableVector<Vector3> acceleration;
    private final PlayerMovement controller;

    /**
     * Erzeugt ein Bewegungskommando für das übergebene Schiff mit dem
     * angegebenen Typen.
     * Wirft eine NullPointerException, wenn das Schiff oder der
     * Spieler-Movement-Controller null sind oder die Konstante unbekannt
     * ist.
     *
     * @param se zu kontrollierende Schiffs-Entität
     * @param pm Spieler-Movement-Controller
     * @param type Bewegungstyp (siehe Konstanten)
     */
    public MovementCommand(ShipEntity se, PlayerMovement pm, MOVE_DIRECTION type) {
        if (se == null)
            throw new NullPointerException("Schiff war null");

        if (pm == null)
            throw new NullPointerException("PlayerMovement war null");

        controller = pm;

        switch (type) {
            case MAIN_THRUST:
                acceleration = new Vector3(0.f,
                                         0.f, se.getConfiguration().
                        getAccForward());
                break;

            case BACK_THRUST:
                acceleration = new Vector3(0.f,
                                         0.f, -se.getConfiguration().
                        getAccBackward());
                break;

            case STRAFE_LEFT:
                acceleration = new Vector3(
                        se.getConfiguration().getAccStrafe(), 0.0f, 0.f);
                break;

            case STRAFE_RIGHT:
                acceleration = new Vector3(
                        -se.getConfiguration().getAccStrafe(), 0.0f, 0.f);
                break;

            default:
                throw new NullPointerException("Unbekannte Konstante");//unreachable
        }
    }

    @Override
    public void execute(float timeDiff) {
        //zum derzeitigen Beschleunigungsvektor hinzuaddieren
        Vector3 acc = controller.getAcceleration();
        acc.add(acceleration);
    }
}
