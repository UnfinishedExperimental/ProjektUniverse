package de.fhhof.universe.shared.logic.entities.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.SubType;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.Entity;

/**
 * Controller, dem eine Entity und eine Menge an SubControllern zugeordnet
 * werden kann, die gewisse Untertypen von Events behandeln sowie zeitabhänigte
 * Controller, die bei jedem refresh-Aufruf aktualisiert werden.
 * 
 * @author sylence
 *
 * @param <T> welche konkrete Entity bereitgestellt werden soll
 */
public class EntityController<T extends Entity> implements PJUTEventHandler,
	PJUTTimedRefreshable
{
	private HashMap<SubType, EntitySubController<?>> subControllers;
	private final T entity;
	private List<PJUTTimedRefreshable> timedControllers;
	
	/**
	 * Erstellt einen SubController, der Zugriff auf die übergebene Entity hat.
	 * Erzeugt eine NullPointerException, wenn die übergebene Entity null ist.
	 * 
	 * @param entity Enitity, zu der der Controller gehört.
	 */
	public EntityController(T entity)
	{
		if(entity != null)
		{
			this.entity = entity;
		}
		else
		{
			throw new NullPointerException("Null als Entity ist ungültig");
		}
		
		subControllers = new HashMap<SubType, EntitySubController<?>>();
		timedControllers = new ArrayList<PJUTTimedRefreshable>();
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getSub() != null)
		{
			PJUTEventHandler sctrl = subControllers.get(event.getSub());
			
			if(sctrl != null)
			{
				sctrl.handleEvent(event);
			}
		}
	}
	
	/**
	 * @return Entity, zu der der Controller gehört.
	 */
	public T getEntity()
	{
		return entity;
	}
	
	/**
	 * Registriert einen SubController für alle Untertypen, die er über die
	 * getTypes-Methode liefert.
	 * Bereits für Typen registrierte Controller werden dabei gegebenenfalls
	 * überschrieben.
	 * Ignoriert Aufrufe, bei denen der Controller oder seine Untertypenliste
	 * null sind.
	 * 
	 * @param subCtrl zu registrierender Controller
	 */
	public void addSubController(EntitySubController<?> subCtrl)
	{
		if(subCtrl != null)
		{
			SubType types[] = subCtrl.getTypes();
			
			if(types != null)
			{
				for(SubType sType : types)
				{
					subControllers.put(sType, subCtrl);
				}
			}
		}
	}
	
	/**
	 * Registriert den übergebenen SubController für den übergebenen SubTyp.
	 * Wird als Controller null übergeben, werden für den SubTyp registrierte
	 * SubController deregistriert.
	 * Falls schon ein Controller für den Typ registriert ist, wird diese
	 * Registrierung überschrieben.
	 * Ignoriert Aufrufe, bei denen der Controller oder der UnterTyp null sind.
	 * 
	 * @param sType SubTyp, für den Hanlder geändert werden sollen
	 * @param subCtrl zu registrierender SubController oder null
	 */
	public void setSubController(SubType sType, EntitySubController<?> subCtrl)
	{
		if(sType != null)
		{
			if(subCtrl != null)
			{
				subControllers.put(sType, subCtrl);
			}
			else
			{
				subControllers.remove(sType);
			}
		}
	}
	
	/**
	 * Deregistriert einen SubController für alle Untertypen, die die
	 * getTypes-Methode liefert.
	 * Ignoriert Aufrufe, bei denen der Controller oder seine Untertypenliste
	 * null sind.
	 * 
	 * @param subCtrl zu deregistrierender SubController
	 */
	public void removeSubController(EntitySubController<?> subCtrl)
	{
		if(subCtrl != null)
		{
			SubType types[] = subCtrl.getTypes();
			
			if(types != null)
			{
				for(SubType sType : types)
				{
					if(subControllers.get(sType) == subCtrl)
					{
						subControllers.remove(sType);
					}
				}
			}
		}
	}
	
	/**
	 * Fügt den Timing-abhängigen Controllern einen hinzu, sofern dieser nicht
	 * null ist.
	 * 
	 * @param controller hinzuzufügender Controller
	 */
	public void addTimedController(PJUTTimedRefreshable controller)
	{
		if(controller != null)
		{
			timedControllers.add(controller);
		}
	}
	
	/**
	 * Entfernt den übergebenen Controller, falls vorhanden.
	 * 
	 * @param controller zu entfernender Controller
	 */
	public void removeTimedController(PJUTTimedRefreshable controller)
	{
		if(controller != null)
		{
			timedControllers.remove(controller);
		}
	}

	@Override
	public void refresh(float timeDiff)
	{
		for(PJUTTimedRefreshable tr : timedControllers)
		{
			tr.refresh(timeDiff);
		}
	}
}
