package universetournament.client.logic.entities.controller;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.ingame.ShipEntity;

/**
 * Controller, welcher Schilde und Waffenenergie eines Schiffes mit der Zeit
 * wieder aufläd.
 * 
 * @author sylence
 *
 */
public class RechargeController implements PJUTTimedRefreshable
{
	private final ShipEntity entity;
	private final float shieldRecharge, energyRecharge;
	private final short shieldMax, energyMax;
	private float shieldBuff, energyBuff;
	
	/**
	 * Enum, welche nur den Typ zum wieder aufladen der Schilde enthält.
	 * 
	 * @author sylence
	 *
	 */
	public enum SubTypes implements SubType
	{
		RECHARGE;
	}
	
	/**
	 * Erzeugt einen neuen Auflad-Controller für das angegebene Schiff.
	 * Wirft eine NullPointerException, falls das Schiff null ist.
	 * 
	 * @param se zu kontrollierendes Schiff
	 */
	public RechargeController(ShipEntity se)
	{
		if(se == null)
		{
			throw new NullPointerException("Schiff war null");
		}
		
		entity = se;
		shieldRecharge = se.getConfiguration().getShieldRate();
		energyRecharge = se.getConfiguration().getWeaponRate();
		shieldMax = se.getConfiguration().getShieldCapacity();
		energyMax = se.getConfiguration().getMaxWeaponEnergy();
		shieldBuff = 0.f;
		energyBuff = 0.f;
	}
	
	@Override
	public void refresh(float timeDiff)
	{
		timeDiff /= 1000000000.f;
		short diff = 0;
		
		//Puffer dienen zur leichteren Übertragung von Float zu Short.
		if(entity.getShieldEnergy() < shieldMax)
		{
			shieldBuff += timeDiff * shieldRecharge;
			diff = (short) shieldBuff;
			shieldBuff -= diff;
			entity.modShieldEnergy(diff);
			
			if(diff != 0)
			{
				EntityEvent ee = new EntityEvent(entity.getId(),
						SubTypes.RECHARGE, diff);
				PJUTCliConnector.getInstance().getBuffer().addEvent(ee);
			}
		}
		
		if(entity.getWeaponEnergy() < energyMax)
		{
			energyBuff += timeDiff * energyRecharge;
			diff = (short) energyBuff;
			energyBuff -= diff;
			entity.modWaeponEnergy(diff);
		}
	}
}
