package de.fhhof.universe.shared.data.proto;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;

/**
 * Container für Schiffs-Konfigurationen, der eine Waffe über ihre
 * Konfigurations-ID und eine relative Position am Schiff angibt.
 *
 * @author Thikimchi Nguyen
 *
 */
public class WeaponContainer {
	//TODO: relative Position

    private final short weaponId;
    private final Vector3 position;

    /**
     * Erzeugt einen Waffen-Container mit der Waffen-ID 0 und noch ohne relative
     * Position.
     */
    public WeaponContainer() {
        weaponId = 0;
        position = new Vector3();
    }

    /**
     * @return ID der angebrachten Waffe
     */
    public short getWeaponId() {
        return weaponId;
    }

    /**
     * @return the position
     */
    public Vector3 getPosition() {
        return position;
    }
}
