package universetournament.shared.data.proto;

import java.util.LinkedList;
import java.util.List;

/**
 * Konfiguration für eine Schiffs-Entity mit allen konstanten Werten für einen
 * Schiffstyp.
 * Die Werte sind maximale Trefferpunkte von Hülle und Schild, die maximale
 * Waffenenergie, die Maximalgeschwindigkeit (Drehung und vektoriell), die
 * Beschleunigungen in alle Richtungen und für Drehungen, die Laderaten für
 * Schilde und Waffenenergie und die maximale Raketenzahl.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class ShipConfig extends WorldConfig
{
	private final short maxHitpoints, shieldCapacity, maxWeaponEnergy;
	private final float maxSpeed, maxSpeedRot, accForward, accBackward,
			accStrafe, accRot, shieldRate, weaponRate;
	private final byte maxRockets;
	private final List<WeaponContainer> weapons;
	private final PhysicConfig physic_props;

	/**
	 * Erstellt eine Schiffskonfiguration mit der angegebenen ID bei der
	 * alle Werte bis auf die maximalen Trefferpunkte 0 betragen.
	 * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
	 * Konfigurationen über extern geladen werden.
	 *
	 * @param uid eindeutige ID
	 */
	public ShipConfig()
	{
		maxHitpoints = 1;
		shieldCapacity = 0;
		maxWeaponEnergy = 0;

		maxSpeed = 0.f;
		maxSpeedRot = 0.f;
		accForward = 0.f;
		accBackward = 0.f;
		accStrafe = 0.f;
		accRot = 0.f;

		shieldRate = 0.f;
		weaponRate = 0.f;
		maxRockets = 0;

		// damit XML besser manuell editiert werden kann
		weapons = new LinkedList<WeaponContainer>();
		weapons.add(new WeaponContainer());
		weapons.add(new WeaponContainer());

		physic_props = new PhysicConfig();
	}

	/**
	 * @return maximale Trefferpunkte der Hülle
	 */
	public short getMaxHitpoints()
	{
		return maxHitpoints;
	}

	/**
	 * @return maximale Schildenergie
	 */
	public short getShieldCapacity()
	{
		return shieldCapacity;
	}

	/**
	 * @return maximale Waffenenergie
	 */
	public short getMaxWeaponEnergy()
	{
		return maxWeaponEnergy;
	}

	/**
	 * @return maximale vektorielle Geschwindigkeit
	 */
	public float getMaxSpeed()
	{
		return maxSpeed;
	}

	/**
	 * @return maximale Rotationsgeschwindigkeit
	 */
	public float getMaxSpeedRot()
	{
		return maxSpeedRot;
	}

	/**
	 * @return Beschleunigung geradeaus
	 */
	public float getAccForward()
	{
		return accForward;
	}

	/**
	 * @return Beschleunigung rückwärts
	 */
	public float getAccBackward()
	{
		return accBackward;
	}

	/**
	 * @return Beschleunigung seitwärts
	 */
	public float getAccStrafe()
	{
		return accStrafe;
	}

	/**
	 * @return Rotationsbeschleunigung
	 */
	public float getAccRot()
	{
		return accRot;
	}

	/**
	 * @return Waffenenergie-Laderate
	 */
	public float getWeaponRate()
	{
		return weaponRate;
	}

	/**
	 * @return Schildenergie-Laderate
	 */
	public float getShieldRate()
	{
		return shieldRate;
	}

	/**
	 * @return maximale Anzahl geladener Raketen
	 */
	public byte getMaxRockets()
	{
		return maxRockets;
	}

	/**
	 * @return Liste aller Waffen und wo sie angebracht sind
	 */
	public List<WeaponContainer> getWeapons()
	{
		return weapons;
	}

	/**
	 * @return physikalische Eigenschaften
	 */
	public PhysicConfig getPhysic_props()
	{
		return physic_props;
	}
}
