package de.fhhof.universe.shared.data.proto;

/**
 * Konfiguration für eine Rakete, welche die Projektilkonfiguration um
 * die Rotationsgeschwindigkeit der Rakete erweitert.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class RocketConfig extends ProjectileConfig
{
	private final double constSpeedRot;
	
	/**
	 * Erzeugt eine Raketenkonfiguration mit der angegebenen UID.
	 * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
	 * Konfigurationen über extern geladen werden.
	 * 
	 * @param uid eindeutige ID
	 */
	public RocketConfig()
	{		
		constSpeedRot = 0.f;
	}

	/**
	 * @return Geschwindigkeit, mit der sich die Rakete drehen kann.
	 */
	public double getConstSpeedRot()
	{
		return constSpeedRot;
	}
}
