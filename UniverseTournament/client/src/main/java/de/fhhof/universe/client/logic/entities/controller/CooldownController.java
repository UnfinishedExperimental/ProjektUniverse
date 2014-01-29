package de.fhhof.universe.client.logic.entities.controller;

import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.sub.Blaster;
import de.fhhof.universe.shared.logic.entities.ingame.sub.RocketLauncher;

/**
 * Steuert den Abkühlvorgang aller Waffen eines Schiffs.
 * 
 * @author sylence
 *
 */
public class CooldownController implements PJUTTimedRefreshable
{
	private final ShipEntity entity;
	
	/**
	 * Erzeugt einen neuen Abkühl-Controller für das angegebene Schiff.
	 * Wirft eine NullPointerException, falls das Schiff null ist.
	 * 
	 * @param se zu kontrollierendes Schiff
	 */
	public CooldownController(ShipEntity se)
	{
		if(se == null)
		{
			throw new NullPointerException("Schiff war null");
		}
		
		entity = se;
	}
	
	@Override
	public void refresh(float timeDiff)
	{
		timeDiff /= 1000000.f;
		for(Blaster b : entity.getBlasters())
		{
			if(b.getCoolDownLeft() > 0)
			{
				b.modCoolDownLeft(-timeDiff);
			}
		}
		
		RocketLauncher rl = entity.getLauncher();
		if(rl != null)
		{
			if(rl.getCoolDownLeft() > 0)
			{
				rl.modCoolDownLeft(-timeDiff);
			}
		}
	}

}
