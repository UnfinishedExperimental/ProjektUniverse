package universetournament.shared.data.proto;

/**
 * Konfiguration für eine Waffe im Spiel, die eine gewisse Abkühlzeit
 * besitzt.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class WeaponConfig extends Config
{
	private final double coolDownTime;
	
	/**
	 * Erzeugt eine Waffenkonfiguration mit der angegebenen ID, bei der
	 * die Cooldown-Zeit 0 beträgt.
	 * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
	 * Konfigurationen über extern geladen werden.
	 * 
	 * @param uid eindeutige ID
	 */
	public WeaponConfig()
	{		
		coolDownTime = 0.;
	}

	/**
	 * @return Abkühlzeit nach einem abgefeuerten Schuss.
	 */
	public double getCoolDownTime()
	{
		return coolDownTime;
	}
}
