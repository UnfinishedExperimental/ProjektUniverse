package de.fhhof.universe.shared.logic.entities.util;

import java.io.Serializable;

import de.fhhof.universe.shared.logic.entities.EntityType;

/**
 * Container, welcher einen Entity-Typ und dazugehörige Nutzdaten enthält.
 * Die Nutzdaten können sich je nach Empfänger und Typ unterscheiden.
 * 
 * @author sylence
 *
 */
public class EntityContainer implements Serializable
{
	private EntityType type;
	private Object data;
	
	/**
	 * Erzeugt einen EntityContainer mit dem angegebenen Entity-Typ und den
	 * übergebenen Nutzdaten.
	 * Wirft eine NullPointerException, wenn der übergebene Typ null ist.
	 * 
	 * @param type Typ der zu erzeugenden Entity
	 * @param data benötigte Nutzdaten
	 */
	public EntityContainer(EntityType type, Object data)
	{
		if(type == null)
		{
			throw new NullPointerException("Ungültiger Typ");
		}
		this.type = type;
		this.data = data;
	}

	/**
	 * @return Typ der zu erstellenden Entity
	 */
	public EntityType getType()
	{
		return type;
	}

	/**
	 * Liefert die Nutzdaten zum Entity-Typ.
	 * Im Server sind dies in der Regel lediglich Typ-Angaben, im Client
	 * eher das bereits fertiggestellte Entity-Objekt.
	 * 
	 * @return Nutzdaten des Containers
	 */
	public Object getData()
	{
		return data;
	}
}
