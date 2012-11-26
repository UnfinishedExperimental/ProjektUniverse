package universetournament.client.input.commands;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.client.input.PJUTCommand;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.sub.RocketLauncher;
import universetournament.shared.logic.entities.requests.RocketRequest;
import universetournament.shared.logic.entities.util.EntityContainer;

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
