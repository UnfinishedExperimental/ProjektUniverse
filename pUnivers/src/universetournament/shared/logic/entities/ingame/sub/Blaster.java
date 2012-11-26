package universetournament.shared.logic.entities.ingame.sub;

import universetournament.shared.data.proto.BlasterConfig;

/**
 * Ingame-Objekt für einen Blaster, welches selbst keine Entity ist.
 * Es besitzt lediglich eine BlasterConfig die dieses Verhalten definiert.
 * 
 * @author sylence
 *
 */
public class Blaster extends Weapon<BlasterConfig>
{
	/**
	 * Erzeugt einen Blaster nach mit der angegebenen Konfiguration.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 * 
	 * @param configuration Konfiguration, die den Blaster beschreibt.
	 */
	public Blaster(BlasterConfig configuration)
	{
		super(configuration);
	}
}
