package universetournament.shared.logic.entities.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTBufferedHandler;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;

/**
 * Kennt für alle Entities unter deren ID einen Controller, an den er Events
 * weiterleitet, ihn auffrischt und verwaltet CREATE und DELETE Events.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author sylence
 *
 */
public class UTEntityManager extends PJUTBufferedHandler
	implements PJUTTimedRefreshable
{
	private static final UTEntityManager instance = new UTEntityManager();
	
	private HashMap<Short, EntityController<?>> controllers;
	
	//dynamisch: in Server und Client unterschiedlich
	private PJUTEventHandler entityCreator, entityDestroyer;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized UTEntityManager getInstance()
	{
		return instance;
	}
	
	private UTEntityManager()
	{
		controllers = new HashMap<Short, EntityController<?>>();
	}

	@Override
	public void refresh(float timeDiff)
	{
		LinkedList<GameEvent> temp = new LinkedList<GameEvent>();
		
		//angesammelte Events
		synchronized(buffer)
		{
			for(GameEvent ge : buffer)
			{
				temp.add(ge);
			}
			buffer.clear();
		}
		
		for(GameEvent ge : temp)
		{
			handleEventInternal(ge);
		}
		
		//Zeitdifferenz an zeitabhängige Controller melden
		for(Entry<Short, EntityController<?>> e : controllers.entrySet())
		{
			e.getValue().refresh(timeDiff);
		}
	}
	
	private void handleEventInternal(GameEvent ge)
	{
		if(ge instanceof EntityEvent)
		{
			try
			{
				if(ge.getSub() instanceof Entity.SubEvents)
				{
					switch((Entity.SubEvents) ge.getSub())
					{
						case CREATE:
							if(entityCreator != null)
							{
								entityCreator.handleEvent(ge);
							}
							break;
							
						case DELETE:
							if(entityDestroyer != null)
							{
								entityDestroyer.handleEvent(ge);
							}
							break;
					}
				}
				else
				{
					sendToController((EntityEvent) ge);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.err.println("Ankommende Daten unbrauchbar");
			}
		}
	}

	private void sendToController(EntityEvent ee)
	{
		PJUTEventHandler ctrl = controllers.get(ee.getId());
		
		if(ctrl != null)
		{
			ctrl.handleEvent(ee);
		}
	}
	
	/**
	 * Setzt den Handler, der für das Erstellen von Entities zuständig ist.
	 * Wird null gesetzt werden CREATE-Events verworfen.
	 * 
	 * @param creator EventHandler, der Entities erstellen kann
	 */
	public void setEntityCreator(PJUTEventHandler creator)
	{
		entityCreator = creator;
	}
	
	/**
	 * Setzt den Handler, der für das Löschen von Entities zuständig ist.
	 * Wird null gesetzt werden DELETE-Events verworfen.
	 * 
	 * @param destroyer EventHandler, der Entities löschen kann
	 */
	public void setEntityDestroyer(PJUTEventHandler destroyer)
	{
		entityDestroyer = destroyer;
	}
	
	/**
	 * @return Handler für CREATE-Events oder null
	 */
	public PJUTEventHandler getEntityCreator()
	{
		return entityCreator;
	}
	
	/**
	 * @return Handler für DELETE-Events oder null
	 */
	public PJUTEventHandler getEntityDestroyer()
	{
		return entityDestroyer;
	}
	
	/**
	 * Setzt den Controller für eine Entity ID.
	 * Geht ein EntityEvent mit dieser ID ein wird das Event an diesen
	 * Handler übergeben.
	 * Wird null übergeben wird der Handler für die ID entfernt, falls
	 * vorhanden.
	 * 
	 * @param id ID der Entity des Controllers
	 * @param controller Event-Handler für die Entity
	 */
	public void setController(short id, EntityController<?> controller)
	{
		if(controller != null)
		{
			controllers.put(id, controller);
		}
		else
		{
			controllers.remove(id);
		}
	}
	
	/**
	 * @param id ID des angefragten Controllers
	 * @return Controller zur ID oder null, falls nicht gefunden
	 */
	public EntityController<?> getController(short id)
	{
		return controllers.get(id);
	}
}
