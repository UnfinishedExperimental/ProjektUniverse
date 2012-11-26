package universetournament.shared.data.proto;

import universetournament.shared.util.math.Vec3;

/**
 * Container für Schiffs-Konfigurationen, der eine Waffe über ihre
 * Konfigurations-ID und eine relative Position am Schiff angibt.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class WeaponContainer
{
	//TODO: relative Position
	
	private final short weaponId;
    private final Vec3 position;
	
	/**
	 * Erzeugt einen Waffen-Container mit der Waffen-ID 0 und noch ohne
	 * relative Position.
	 */
	public WeaponContainer()
	{
		weaponId = 0;
                position = new Vec3();
	}

	/**
	 * @return ID der angebrachten Waffe
	 */
	public short getWeaponId()
	{
		return weaponId;
	}

	/**
	 * @return the position
	 */
	public Vec3 getPosition()
	{
		return position;
	}
}
