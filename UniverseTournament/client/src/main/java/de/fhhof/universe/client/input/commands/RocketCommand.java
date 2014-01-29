package de.fhhof.universe.client.input.commands;

import de.fhhof.universe.client.communication.PJUTCliConnector;
import de.fhhof.universe.client.input.PJUTCommand;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.sub.RocketLauncher;
import de.fhhof.universe.shared.logic.entities.requests.RocketRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;

/**
 * Kommando, welches ein Schiff Raketen abfeuern lässt.
 * 
 * @author Florian Holzschuher
 *
 */
public class RocketCommand implements PJUTCommand
{
	private final ShipEntity ship;
	
	/**
	 * Erzeugt ein Kommando, welches den Raktenwerfer des angegebenen Schiffes
	 * ansteuert.
	 * Wirft eine NullPointerException, wenn das Schiff null ist.
	 * 
	 * @param ship Schiff, dessen Raktenwerfer feuern soll
	 */
	public RocketCommand(ShipEntity ship)
	{
		if(ship == null)
		{
			throw new NullPointerException("Schiff war null");
		}
		
		this.ship = ship;
	}
	
	@Override
	public void execute(float timeDiff)
	{
		RocketLauncher l = ship.getLauncher();
		
		//feuern, wenn vorhanden, abgekühlt und Schiff Rakete übrig
		if(l != null && ship.getRocketCount() > 0 && l.getCoolDownLeft() <= 0)
		{
			ship.modRocketCount((byte) -1);
			
			//Projektil anfordern
			RocketRequest request = new RocketRequest(
					l.getConfiguration().getRocketConfig(),
					ship.getPilotId());
			EntityContainer container = new EntityContainer(
					EntityType.ROCKET, request);
			EntityEvent ee = new EntityEvent((short)0,
					Entity.SubEvents.CREATE, container);
			PJUTCliConnector.getInstance().getBuffer().addEvent(ee);
			
			//Cooldown-Zeit setzen
			l.setCoolDownLeft(l.getConfiguration().getCoolDownTime());
		}
	}
}
