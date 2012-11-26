package universetournament.shared.data.proto;

/**
 * Konfiguration für ein Projektil mit einer festen Geschwindigkeit, einer
 * maximalen Lebensdauer und dem verursachten Schaden für Hülle und Schilde.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class ProjectileConfig extends WorldConfig
{
	private final float speed;
	private final double lifeTime;
	private final short  hullDamage, shieldDamage;
    private final PhysicConfig physic_props;
	
	/**
	 * Erzeugt ein neues Projektil mit der angegebenen UID bei dem
	 * alle Werte auf 0 stehen.
	 * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
	 * Konfigurationen über extern geladen werden.
	 * 
	 * @param uid
	 */
	public ProjectileConfig()
	{		
		speed = 0.f;
		lifeTime = 0.;
		hullDamage = 0;
		shieldDamage = 0;
        physic_props = new PhysicConfig();
	}
	
	/**
	 * @return feste Projektilgeschwindigkeit
	 */
	public float getSpeed()
	{
		return speed;
	}

	/**
	 * @return maximale Lebensdauer
	 */
	public double getLifeTime()
	{
		return lifeTime;
	}

	/**
	 * @return vom Projektil verursachter Hüllenschaden
	 */
	public short getHullDamage()
	{
		return hullDamage;
	}

	/**
	 * @return vom Projektil verursachter Schildschaden
	 */
	public short getShieldDamage()
	{
		return shieldDamage;
	}

    public PhysicConfig getPhysic_props()
    {
        return physic_props;
    }
}
