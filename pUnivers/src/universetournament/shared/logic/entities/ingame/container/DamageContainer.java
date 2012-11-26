package universetournament.shared.logic.entities.ingame.container;

import java.io.Serializable;

/**
 * Container, welcher Schild- und Hüllenschaden enthält und den Verursacher.
 * Die enthaltenen Werte sind negativ.
 * 
 * @author sylence
 *
 */
public class DamageContainer implements Serializable
{
	private final short shieldDamage, hullDamage;
	private final byte cause;
	
	/**
	 * Initialisiert den Container mit den angegebenen Werten, und negativiert
	 * sie für einfache Addition.
	 * 
	 * @param shieldDamage Schildschaden
	 * @param hullDamage Hüllenschaden
	 * @param cause Verursacher
	 */
	public DamageContainer(short shieldDamage, short hullDamage, byte cause)
	{
		this.shieldDamage = (short) -shieldDamage;
		this.hullDamage = (short) -hullDamage;
		this.cause = cause;
	}

	/**
	 * @return Schildschaden
	 */
	public short getShieldDamage()
	{
		return shieldDamage;
	}

	/**
	 * @return Hüllenschaden
	 */
	public short getHullDamage()
	{
		return hullDamage;
	}

	/**
	 * @return ID des Verursachers
	 */
	public byte getCause()
	{
		return cause;
	}
}
