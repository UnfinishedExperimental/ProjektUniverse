package universetournament.server.logic.entities;

import java.util.BitSet;
import java.util.HashMap;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.util.EntityContainer;

/**
 * Factory auf Server-Seite, welche auf lokale oder Client-Anfrage Entities
 * erstellt, ins System übernimmt und an Clients verteilt.
 * Sie vergibt die eindeutigen IDs an die Entities.
 * Die eigentliche "Arbeit" erledigen für die Entity-Typen registrierte
 * SubFactories.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author Florian Holzschuher
 *
 */
public class EntityFactory implements PJUTEventHandler
{
	private static final EntityFactory instance = new EntityFactory();
	
	private BitSet idSet;
	private HashMap<EntityType, PJUTEventHandler> subFactories;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized EntityFactory getInstance()
	{
		return instance;
	}
	
	private EntityFactory()
	{
		idSet = new BitSet();
		subFactories = new HashMap<EntityType, PJUTEventHandler>();
	}

	@Override
	public void handleEvent(GameEvent event)
	{	
		if(event != null)
		{
			Object data = event.getData();
			
			if(data instanceof EntityContainer)
			{
				PJUTEventHandler sFact = subFactories.get(
						((EntityContainer)data).getType());
				
				if(sFact != null)
				{
					sFact.handleEvent(event);
				}
			}
		}
	}
	
	/**
	 * Fügt der Fabrik eine Unter-Fabrik für den angegebenen Typ hinzu oder
	 * entfernt eine, wenn null als subFactory übergeben wird.
	 * Ignoriert Aufrufe, bei denen der übergebene Typ null ist.
	 * 
	 * @param type Typ, für den die Factory (de)registriert werden soll
	 * @param handler hinzuzufügende Factory oder null
	 */
	public void setSubFactory(EntityType type, PJUTEventHandler subFact)
	{
		if(type != null)
		{
			if(subFact != null)
			{
				subFactories.put(type, subFact);
			}
			else
			{
				subFactories.remove(type);
			}
		}
	}
	
	/**
	 * Vergibt eine neue, nicht vergebene Entity-ID und setzt diese intern al
	 * "vergeben".
	 * 
	 * @return neue eindeutige Entity-ID 
	 */
	public short getUniqueId()
	{
		//sollte so nie zurückgegeben werden
		short newId = 0;
		
		synchronized(idSet)
		{
			newId = (short) idSet.nextClearBit(1);
			idSet.set(newId);
		}
		
		return newId;
	}
	
	/**
	 * Wird aufgerufen, um anzuzeigen, dass die Entity mit der übergebenen ID
	 * nichtmehr existiert, die ID also wieder frei ist.
	 * 
	 * @param id nichtmehr gebrauchte ID
	 */
	public void freeUniqueId(short id)
	{
		synchronized(idSet)
		{
			idSet.clear(id);
		}
	}
}
