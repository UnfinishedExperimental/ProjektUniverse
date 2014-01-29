package de.fhhof.universe.shared.events.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.events.SubType;
import de.fhhof.universe.shared.events.GameEvent;

/**
 * Interner EventBus, auf dem sich Objekte für Event-Typen registrieren
 * können, um über neue Events dieser Art benachrichtigt zu werden, sowie
 * darüber auch Events bekanntgeben können.
 * Bezieht sich sowohl auf MainTypes als auch auf SubTypes.
 * Funktioniert nach dem singleton pattern.
 * Nicht auf Thread-Sicherheit getrimmt.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTEventBus implements PJUTEventHandler
{
	private static final int INITIAL_SIZE = 16;
	private static final int INITIAL_SUB_SIZE = 32;
	
	private static final PJUTEventBus instance = new PJUTEventBus();
	
	private HashMap<MainType, List<PJUTEventHandler>> mainHandlers;
	private HashMap<SubType, List<PJUTEventHandler>> subHandlers;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static final synchronized PJUTEventBus getInstance()
	{
		return instance;
	}
	
	private PJUTEventBus()
	{
		mainHandlers = new HashMap<MainType, List<PJUTEventHandler>>(
				INITIAL_SIZE);
		subHandlers = new HashMap<SubType, List<PJUTEventHandler>>(
				INITIAL_SUB_SIZE);
	}
	
	/**
	 * Registriert einen Handler für einen MainType von Events, so dass er bei
	 * jedem Eingang eines solchen Events dieses übergeben bekommt.
	 * Prüft nicht auf Doppelungen.
	 * Ignoriert Aufrufe, bei denen null-Referenzen übergeben werden.
	 * 
	 * @param handler zu registrierender Handler
	 * @param mainType Haupttyp, für den der Handler registriert wird
	 */
	public void register(PJUTEventHandler handler, MainType mainType)
	{
		if(handler != null && mainType != null)
		{
			List<PJUTEventHandler> list = mainHandlers.get(mainType);
			
			//Liste erstellen und eintragen, falls sie noch nicht existiert
			if(list == null)
			{
				list = new ArrayList<PJUTEventHandler>();
				mainHandlers.put(mainType, list);
			}
			
			list.add(handler);
		}
	}
	
	/**
	 * Entfernt die Registrierung für den eingetragenen Handler bei dem
	 * angegebenen Haupttyp, falls vorhanden.
	 * Dies gilt nur, wenn er direkt für den Haupttyp und nicht für den
	 * Untertyp registriert ist.
	 * Ignoriert Aufrufe, bei denen null-Referenzen übergeben werden.
	 * 
	 * @param handler zu deregistrierender Handler
	 * @param mainType Haupttyp, für den der Handler deregistriert wird
	 */
	public void deRegister(PJUTEventHandler handler, MainType mainType)
	{
		if(handler != null && mainType != null)
		{
			List<PJUTEventHandler> list = mainHandlers.get(mainType);
			
			if(list != null)
			{
				list.remove(handler);
			}
		}
	}
	
	/**
	 * Registriert einen Handler für einen Untertyp von Events, so dass er bei
	 * jedem Eingang eines solchen Events dieses übergeben bekommt.
	 * Prüft nicht auf Doppelungen.
	 * Ignoriert Aufrufe, bei denen null-Referenzen übergeben werden.
	 * 
	 * @param handler zu registrierender Handler
	 * @param subType Untertyp, für den der Handler registriert wird
	 */
	public void register(PJUTEventHandler handler, SubType subType)
	{
		if(handler != null && subType != null)
		{	
			//Map erstellen, falls noch nicht vorhanden
			List<PJUTEventHandler> list = subHandlers.get(subType);
			
			//Liste erstellen und eintragen, falls sie noch nicht existiert
			if(list == null)
			{
				list = new ArrayList<PJUTEventHandler>();
				subHandlers.put(subType, list);
			}
			
			list.add(handler);
		}
	}
	
	/**
	 * Entfernt die Registrierung für den eingetragenen Handler bei dem
	 * angegebenen Unterttyp, falls vorhanden.
	 * Dies gilt nur, wenn er nicht direkt für den Haupttyp, sondern für den
	 * Untertyp registriert ist.
	 * Ignoriert Aufrufe, bei denen null-Referenzen übergeben werden.
	 * 
	 * @param handler zu deregistrierender Handler
	 * @param subType Untertyp, für den der Handler deregistriert wird
	 */
	public void deRegister(PJUTEventHandler handler, SubType subType)
	{
		if(handler != null && subType != null)
		{
			List<PJUTEventHandler> list = subHandlers.get(subType);
			
			if(list != null)
			{
				list.remove(handler);
			}
		}
	}
	
	/**
	 * Entfernt alle registrierten Handler für alle Haupt- und Untertypen.
	 */
	public void deRegisterAll()
	{
		mainHandlers.clear();
		subHandlers.clear();
	}
	
	/**
	 * Entfernt alle direkt auf den Haupttyp registrierten Handler.
	 * Bezieht sich nicht auf auf Untertypen registrierte Handler.
	 * 
	 * @param mainType Typ, für den alle Handler entfernt werden sollen.
	 */
	public void deRegisterAll(MainType mainType)
	{
		List<PJUTEventHandler> list = mainHandlers.get(mainType);
		
		if(list != null)
		{
			list.clear();
		}
	}
	
	/**
	 * Entfernt alle direkt auf den Untertyp registrierten Handler.
	 * Bezieht sich nicht auf direkt auf Haupttypen registrierte Handler.
	 * 
	 * @param subType Untertyp, für den alle Handler entfernt werden sollen.
	 */
	public void deRegisterAll(SubType subType)
	{
		List<PJUTEventHandler> list = subHandlers.get(subType);
		
		if(list != null)
		{
			list.clear();
		}
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		distributeEvent(event);
	}
	
	/**
	 * Verteilt ein Event an alle dafür registrierten Handler.
	 * Ignoriert null.
	 * 
	 * @param event zu verteilendes Event
	 */
	public void distributeEvent(GameEvent event)
	{
		distributeEvent(event, null);
	}
	
	/**
	 * Verteilt ein Event an alle dafür registrierten Handler, außer den,
	 * welcher noch als Parameter angegeben ist.
	 * Dies kann beispielsweise dazu genutzt werden, das Event an alle, außer
	 * der Quelle des Events zu schicken.
	 * Ignoriert null als Event.
	 * 
	 * @param event zu verteilendes Event
	 * @param ignore zu ignorierender Handler
	 */
	public void distributeEvent(GameEvent event, PJUTEventHandler ignore)
	{
		if(event != null)
		{
			MainType mainType = event.getMain();
			SubType subType = event.getSub();
			
			//verteilen über Haupttyp
			List<PJUTEventHandler> list = mainHandlers.get(mainType);
			
			if(list != null)
			{
				for(PJUTEventHandler handler : list)
				{
					if(handler != ignore)
					{
						handler.handleEvent(event);
					}
				}
			}
			
			//verteilen über Untertyp
			list = subHandlers.get(subType);
			
			if(list != null)
			{
				for(PJUTEventHandler handler : list)
				{
					if(handler != ignore)
					{
						handler.handleEvent(event);
					}
				}
			}
		}
	}
}
