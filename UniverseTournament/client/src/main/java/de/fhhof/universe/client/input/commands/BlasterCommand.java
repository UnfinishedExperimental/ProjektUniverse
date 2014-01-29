package de.fhhof.universe.client.input.commands;

import de.fhhof.universe.client.communication.PJUTCliConnector;
import de.fhhof.universe.client.input.PJUTCommand;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.sub.Blaster;
import de.fhhof.universe.shared.logic.entities.requests.ShotRequest;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;

/**
 * Kommando, welches die Blaster eines Schiffes abfeuert.
 * 
 * @author Florian Holzschuher
 *
 */
public class BlasterCommand implements PJUTCommand
{
	private final ShipEntity ship;
	
	/**
	 * Erzeugt ein Kommando, welches die Blaster des angegebenen Schiffes
	 * abfeuert.
	 * Wirft eine NullPointerException, wenn das Schiff null ist.
	 * 
	 * @param ship Schiff, dessen Blaster abgefeuert werden sollen
	 */
	public BlasterCommand(ShipEntity ship)
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
		for(Blaster b : ship.getBlasters())
		{
			//feuern wenn Waffe bereit und genügend Energie
			if(b.getCoolDownLeft() <= 0 && ship.getWeaponEnergy() >=
				b.getConfiguration().getEnergyDrain())
			{
				//Projektil anfordern
				ShotRequest request = new ShotRequest(
						b.getConfiguration().getShotConfig(),
						ship.getPilotId());
				EntityContainer container = new EntityContainer(
						EntityType.SHOT, request);
				EntityEvent ee = new EntityEvent((short)0,
						Entity.SubEvents.CREATE, container);
				PJUTCliConnector.getInstance().getBuffer().addEvent(ee);
				
				//Energie abziehen, Cooldown setzen
				ship.modWaeponEnergy(
						(short) -b.getConfiguration().getEnergyDrain());
				b.setCoolDownLeft(b.getConfiguration().getCoolDownTime());
			}
		}
	}
}
