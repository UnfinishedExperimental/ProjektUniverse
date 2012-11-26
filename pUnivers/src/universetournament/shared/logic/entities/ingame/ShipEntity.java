package universetournament.shared.logic.entities.ingame;

import java.util.ArrayList;
import java.util.List;

import universetournament.shared.logic.entities.ingame.container.SimplePhysic;
import universetournament.shared.data.proto.ShipConfig;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.sub.Blaster;
import universetournament.shared.logic.entities.ingame.sub.RocketLauncher;

/**
 * Schiffs-Entity, die über eine ShipConfig ihre Eigenschaften erhält.
 * Sie besitzt den aktuellen Zustand von Hülle, Schilden, Waffenenergie und
 * die ID des Piloten (des Clients).
 * 
 * @author sylence
 *
 */
public class ShipEntity extends WorldEntity<PhysicContainer>
{
	private List<Blaster> blasters;
	private RocketLauncher launcher;
	private short hitPoints, shieldEnergy, weaponEnergy;
	private byte pilotId, rocketCount;

	/**
	 * Erzeugt eine Schiffs-Entity mit der angegebenen ID und der übergebenen
	 * Konfiguration.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 *
	 * @param id ID der Entity
	 * @param pilotId ID des Clients, dem das Schiff gehört
	 * @param configuration Konfiguration, die das Schiff definiert
	 */
	public ShipEntity(short id, byte pilotId, ShipConfig configuration)
	{
		super(id, configuration, new SimplePhysic(
				configuration.getPhysic_props()));

		this.pilotId = pilotId;

		blasters = new ArrayList<Blaster>();
		launcher = null;

		hitPoints = configuration.getMaxHitpoints();
		shieldEnergy = configuration.getShieldCapacity();
		weaponEnergy = configuration.getMaxWeaponEnergy();
		rocketCount = configuration.getMaxRockets();
	}

	/**
	 * @param hitPoints neue Trefferpunkte
	 */
	public void setHitPoints(short hitPoints)
	{
		this.hitPoints = hitPoints;
	}

	/**
	 * @param diff Änderung der Hitpoints
	 */
	public void modHitPoints(short diff)
	{
		this.hitPoints += diff;
	}

	/**
	 * @return aktuelle Trefferpunkte
	 */
	public short getHitPoints()
	{
		return hitPoints;
	}

	/**
	 * @param shieldEnergy neue Schildenergie
	 */
	public void setShieldEnergy(short shieldEnergy)
	{
		this.shieldEnergy = shieldEnergy;
	}

	/**
	 * @param diff Änderung der Schildenergie
	 */
	public void modShieldEnergy(short diff)
	{
		shieldEnergy += diff;
	}

	/**
	 * @return aktuelle Schildenergie
	 */
	public short getShieldEnergy()
	{
		return shieldEnergy;
	}

	/**
	 * @param weaponEnergy neue Waffenenergie
	 */
	public void setWeaponEnergy(short weaponEnergy)
	{
		this.weaponEnergy = weaponEnergy;
	}

	/**
	 * @param diff Änderung der Waffenenergie
	 */
	public void modWaeponEnergy(short diff)
	{
		weaponEnergy += diff;
	}

	/**
	 * @return aktuelle Waffenergie
	 */
	public short getWeaponEnergy()
	{
		return weaponEnergy;
	}

	/**
	 * @param rocketCount neue Anzahl geladener Raketen
	 */
	public void setRocketCount(byte rocketCount)
	{
		this.rocketCount = rocketCount;
	}

	/**
	 * @param diff Änderung der Raketenzahl
	 */
	public void modRocketCount(byte diff)
	{
		rocketCount += diff;
	}

	/**
	 * @return Anzahl aktuell geladener Raketen
	 */
	public byte getRocketCount()
	{
		return rocketCount;
	}

	/**
	 * @return ID des Piloten
	 */
	public byte getPilotId()
	{
		return pilotId;
	}

	@Override
	public ShipConfig getConfiguration()
	{
		return (ShipConfig) super.getConfiguration();
	}

	public void addBlaster(Blaster b)
	{
		if (b != null)
		{
			blasters.add(b);
		}
	}

	/**
	 * @param blasters the blasters to set
	 */
	public void setBlasters(List<Blaster> blasters)
	{
		this.blasters = blasters;
	}

	/**
	 * @return the blasters
	 */
	public List<Blaster> getBlasters()
	{
		return blasters;
	}

	/**
	 * @param launcher the launcher to set
	 */
	public void setLauncher(RocketLauncher launcher)
	{
		this.launcher = launcher;
	}

	/**
	 * @return the launcher
	 */
	public RocketLauncher getLauncher()
	{
		return launcher;
	}
}
