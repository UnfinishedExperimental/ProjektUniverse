package universetournament.client.logic.entities;

import java.util.HashMap;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.util.EntityContainer;

/**
 * Klasse, die auf Client-Seite vom Server erstellte Entites ins System
 * übernimmt und die nötigen Controller registriert.
 * 
 * @author sylence
 *
 */
public class EntityAssembler implements PJUTEventHandler
{
	private static final EntityAssembler instance = new EntityAssembler();

	private HashMap<EntityType, PJUTEventHandler> subAssemblers;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized EntityAssembler getInstance()
	{
		return instance;
	}
	
	private EntityAssembler()
	{
		subAssemblers = new HashMap<EntityType, PJUTEventHandler>();
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null)
		{
			Object data = event.getData();
			
			if(data instanceof EntityContainer)
			{
				PJUTEventHandler sAss = subAssemblers.get(
						((EntityContainer)data).getType());
				
				if(sAss != null)
				{
					sAss.handleEvent(event);
				}
			}
		}
	}

	/**
	 * Fügt dem einen Unter-Assembler für den angegebenen Typ hinzu oder
	 * entfernt einen, wenn null als subAssembler übergeben wird.
	 * Ignoriert Aufrufe, bei denen der übergebene Typ null ist.
	 * 
	 * @param type Typ, für den die Factory (de)registriert werden soll
	 * @param subAss hinzuzufügende Factory oder null
	 */
	public void setSubAssembler(EntityType type, PJUTEventHandler subAss)
	{
		if(type != null)
		{
			if(subAss != null)
			{
				subAssemblers.put(type, subAss);
			}
			else
			{
				subAssemblers.remove(type);
			}
		}
	}
}
