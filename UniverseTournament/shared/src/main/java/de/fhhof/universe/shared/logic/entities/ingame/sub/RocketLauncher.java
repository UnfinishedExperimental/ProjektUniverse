package de.fhhof.universe.shared.logic.entities.ingame.sub;

import de.fhhof.universe.shared.data.proto.RLauncherConfig;

/**
 * Ingame-Objekt für einen Raketenwerfer, welches selbst keine Entity ist.
 * Es besitzt lediglich eine RLauncherConfig die dieses Verhalten definiert.
 * 
 * @author sylence
 *
 */
public class RocketLauncher extends Weapon<RLauncherConfig>
{
	/**
	 * Erzeugt einen Raketenwerfer nach mit der angegebenen Konfiguration.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 * 
	 * @param configuration Konfiguration, die den Raketenwerfer beschreibt.
	 */
	public RocketLauncher(RLauncherConfig configuration)
	{
		super(configuration);
	}
}
