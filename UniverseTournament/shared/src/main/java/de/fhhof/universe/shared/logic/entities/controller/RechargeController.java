package de.fhhof.universe.shared.logic.entities.controller;

import de.fhhof.universe.shared.events.SubType;
import de.fhhof.universe.shared.events.buffers.PJUTEventBuffer;
import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;

/**
 * Controller, welcher Schilde und Waffenenergie eines Schiffes mit der Zeit
 * wieder aufläd.
 *
 * @author sylence
 *
 */
public class RechargeController implements PJUTTimedRefreshable {

    private final PJUTEventBuffer buffer;
    private final ShipEntity entity;
    private final float shieldRecharge, energyRecharge;
    private final short shieldMax, energyMax;
    private float shieldBuff, energyBuff;

    /**
     * Enum, welche nur den Typ zum wieder aufladen der Schilde enthält.
     *
     * @author sylence
     *
     */
    public enum SubTypes implements SubType {

        RECHARGE;
    }

    /**
     * Erzeugt einen neuen Auflad-Controller für das angegebene Schiff. Wirft
     * eine NullPointerException, falls das Schiff null ist.
     *
     * @param se zu kontrollierendes Schiff
     */
    public RechargeController(PJUTEventBuffer buffer, ShipEntity se) {
        if (se == null) {
            throw new NullPointerException("Schiff war null");
        }
        this.buffer = buffer;
        entity = se;
        shieldRecharge = se.getConfiguration().getShieldRate();
        energyRecharge = se.getConfiguration().getWeaponRate();
        shieldMax = se.getConfiguration().getShieldCapacity();
        energyMax = se.getConfiguration().getMaxWeaponEnergy();
        shieldBuff = 0.f;
        energyBuff = 0.f;
    }

    @Override
    public void refresh(float timeDiff) {
        timeDiff /= 1000000000.f;
        short diff = 0;

        //Puffer dienen zur leichteren Übertragung von Float zu Short.
        if (entity.getShieldEnergy() < shieldMax) {
            shieldBuff += timeDiff * shieldRecharge;
            diff = (short) shieldBuff;
            shieldBuff -= diff;
            entity.modShieldEnergy(diff);

            if (diff != 0) {
                EntityEvent ee = new EntityEvent(entity.getId(),
                        SubTypes.RECHARGE, diff);
                buffer.addEvent(ee);
            }
        }

        if (entity.getWeaponEnergy() < energyMax) {
            energyBuff += timeDiff * energyRecharge;
            diff = (short) energyBuff;
            energyBuff -= diff;
            entity.modWaeponEnergy(diff);
        }
    }
}
