package de.fhhof.universe.shared.logic.entities.ingame.sub;

import java.io.Serializable;

import de.fhhof.universe.shared.data.proto.WeaponConfig;

/**
 * Ingame-Objekt für eine Waffe, welches selbst keine Entity ist.
 * Es besitzt lediglich Informationen über den Abkühl-Status einer Waffe und
 * eine WeaponConfig, die dieses Verhalten definiert.
 * 
 * @author sylence
 *
 */
public class Weapon<T extends WeaponConfig> implements Serializable
{
	//TODO: relative Position
	
	private T configuration;
	private double coolDownLeft;
	
	/**
	 * Erzeugt eine Ingame-Waffe mit dem Cooldown-Status "bereit" nach der
	 * angegebenen Konfiguration.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 * 
	 * @param configuration Konfiguration, die die Waffe definiert.
	 */
	public Weapon(T configuration)
	{
		if(configuration != null)
		{
			this.configuration = configuration;
		}
		else
		{
			throw new NullPointerException("keine gültige Konfiguration");
		}
		
		coolDownLeft = 0;
	}

	/**
	 * @param coolDownLeft neue Rest-Abkühlzeit
	 */
	public void setCoolDownLeft(double coolDownLeft)
	{
		this.coolDownLeft = coolDownLeft;
	}
	
	/**
	 * @param diff differenz zur vorherigen Rest-Abkühlzeit
	 */
	public void modCoolDownLeft(double diff)
	{
		coolDownLeft += diff;
	}

	/**
	 * @return restliche Abkühlzeit bis wieder gefeuert werden kann
	 */
	public double getCoolDownLeft()
	{
		return coolDownLeft;
	}
	
	/**
	 * @return Konfiguration, die diese Waffe beschreibt
	 */
	public T getConfiguration()
	{
		return configuration;
	}
}
