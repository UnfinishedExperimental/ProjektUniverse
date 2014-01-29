package de.fhhof.universe.shared.data.proto;

/**
 * Konfiguration für einen Blaster im Spiel, welcher  die Konfiguration für die
 * abgefeuerten Raketen über ihre ID kennt.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class RLauncherConfig extends WeaponConfig
{
	private final short rocketConfig;
	
	/**
	 * Erzeugt eine Blaster-konfiguration mit der angegebenen ID, bei der
	 * die RocketConfig-ID 0 beträgt
	 * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
	 * Konfigurationen über extern geladen werden.
	 * 
	 * @param uid eindeutige ID
	 */
	public RLauncherConfig()
	{		
		rocketConfig = 0;
	}

	/**
	 * @return UID der Konfiguration eines Rakete
	 */
	public short getRocketConfig()
	{
		return rocketConfig;
	}
}
