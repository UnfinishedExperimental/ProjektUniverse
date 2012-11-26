package universetournament.server.communication.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import universetournament.server.communication.PJUTServerSocket;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.events.MainType;

/**
 * Verwaltung für die PJUTServerReceiver, die an die jeweiligen Socket-Puffer
 * gekoppelt sind.
 * Stellt wenn richtig benutzt sicher, dass für jeden Socket nur ein Puffer
 * erstellt wird.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTReceiverManager
{
	/**
	 * Anfängliche Größe der Buffer-Map.
	 */
	public static final int DEFAULT_INIT_CAP = 16;
	
	private static final PJUTReceiverManager instance =
		new PJUTReceiverManager();
	
	private final HashMap<Byte, PJUTServerReceiver> receivers;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized PJUTReceiverManager getInstance()
	{		
		return instance;
	}
	
	private PJUTReceiverManager()
	{
		receivers = new HashMap<Byte, PJUTServerReceiver>(
				DEFAULT_INIT_CAP);
	}
	
	/**
	 * Erzeugt, falls möglich und noch nicht vorhanden, einen
	 * PJUTServerreceiver, der die Events aus dem übergebenen Socket abgreift
	 * und weiterleitet.
	 * Die Existenz eines Receivers wird nicht über die Socketreferenz geprüft,
	 * sondern nur über dessen ID, auch wenn der Receiver direkt an ein
	 * konkretes Socket-Objekt gebunden ist. Sind zwei Receiver an einen Socket
	 * gebunden wird dies zu Problemen führen.
	 * Nach dem Methodenaufruf ist der Receiver-Thread noch nicht gestartet.
	 * Ignoriert null-Referenzen.
	 * 
	 * @param socket Socket, an den der Receiver gebunden werden soll
	 */
	public void createReceiver(PJUTServerSocket socket)
	{
		if(socket != null)
		{
			byte id = socket.getId();
			
			synchronized(receivers)
			{
				if(id > 0 && receivers.get(id) == null)
				{
					PJUTServerReceiver receiver = new PJUTServerReceiver();
					receiver.setClientId(id);
					receiver.setList(socket.getIncoming());
					
					receivers.put(id, receiver);
				}
			}
		}
	}
	
	/**
	 * Deaktiviert den entsprechenden Reiceiver, entzieht ihm zur Sicherheit
	 * den überwachten Puffer und entfernt ihn aus der Manager-Verwaltung.
	 * 
	 * @param id ID des zu entfernenden Receivers.
	 */
	public void removeReceiver(byte id)
	{
		if(id > 0)
		{
			PJUTServerReceiver receiver = null;
			
			synchronized(receivers)
			{
				receiver = receivers.get(id);
				receivers.remove(id);
			}
			
			if(receiver != null)
			{
				receiver.setActive(false);
				receiver.setList(null);
			}
		}
	}
	
	/**
	 * Fügt der Verwaltung einen extern erzeugten Receiver über seine ID hinzu.
	 * Achtung: dabei werden eventuell schon vorhandene Receiver mit der
	 * gleichen ID überschrieben, aber nicht vorher beendet oder ähnliches.
	 * Ignoriert null und Receiver ohne gültige ID (<1).
	 * 
	 * @param receiver hinzuzufügender Receiver
	 */
	public void setReceiver(PJUTServerReceiver receiver)
	{
		if(receiver != null && receiver.getId() > 0)
		{
			synchronized(receivers)
			{
				receivers.put(receiver.getClientId(), receiver);
			}
		}
	}
	
	/**
	 * Startet, falls dieser gefunden wird und schon eine Pufferliste
	 * registriert wurde, den Receiver mit der angegebenen ID.
	 * Läuft der Receiver bereits, gilt dies als Erfolgt.
	 * Es ist zu beachten, dass ein gestarteter Receiver sofort Events aus dem
	 * Socket ausliest und diese verwirft, wenn noch keine entsprechenden
	 * Handler registriert sind.
	 * 
	 * @param id ID des zu startenden Receivers
	 * @return Erfolg
	 */
	public boolean startReceiver(byte id)
	{
		boolean success = false;
		
		synchronized(receivers)
		{
			PJUTServerReceiver receiver = receivers.get(id);
			if(receiver != null && !receiver.isStarted())
			{
				receiver.start();
				
				//Status ist nach außen hin wichtig, deswegen noch synchronized
				receiver.setStarted(true);
				success = true;
			}
		}
		
		return success;
	}
	
	/**
	 * Liefert den Receiver mit der angegebenen ID oder null, falls für diese
	 * ID keiner registriert wurde.
	 * 
	 * @param id ID des gesuchten Receivers
	 * @return Receiver mit der angegebenen ID
	 */
	public PJUTServerReceiver getReceiver(byte id)
	{		
		synchronized(receivers)
		{
			return receivers.get(id);
		}
	}
	
	private void setHandlerFor(PJUTServerReceiver receiver,
			PJUTEventHandler handler, MainType type)
	{
		if(receiver != null)
		{
			receiver.setHandler(type, handler);
		}
	}
	
	/**
	 * Setzt den Handler für den angegebenen Typ bei dem Receiver mit der
	 * angegebenen ID, falls dieser existiert.
	 * Wird null als Handler übergeben, wird, falls vorhanden, der Handler für
	 * die ID beim Receiver gelöscht.
	 * 
	 * @param id ID des Ziel-Receivers
	 * @param handler zu registrierender Handler
	 * @param type Typ, für den der Handler registriert werden soll
	 */
	public void setHandlerFor(byte id, PJUTEventHandler handler, MainType type)
	{			
			synchronized(receivers)
			{
				PJUTServerReceiver receiver = receivers.get(id);
                                setHandlerFor(receiver, handler, type);
			}
	}
	
	/**
	 * Setzt den Handler für den angegebenen Typ bei den Receivern mit den
	 * angegebenen IDs, falls diese existieren.
	 * Wird null als Handler übergeben, wird, falls vorhanden, der Handler für
	 * die ID bei den Receivern gelöscht.
	 * 
	 * @param ids IDs der Ziel-Receiver
	 * @param handler zu registrierender Handler
	 * @param type Typ, für den der Handler registriert werden soll
	 */
	public void setHandlerFor(List<Byte> ids, PJUTEventHandler handler,
			MainType type)
	{
			for(Byte id : ids)
			{
				setHandlerFor(id, handler, type);
			}
	}
	
	/**
	 * Setzt den Handler für den angegebenen Typ bei allen Receivern.
	 * Wird null als Handler übergeben, wird, falls vorhanden, der Handler für
	 * die ID bei den Receivern gelöscht.
	 * 
	 * @param handler zu registrierender Handler
	 * @param type Typ, für den der Handler registriert werden soll
	 */
	public void setHandlerForAll(PJUTEventHandler handler, MainType type)
	{
			synchronized(receivers)
			{
				for(PJUTServerReceiver res : receivers.values())
				{
					setHandlerFor(res, handler, type);
				}
			}
	}
	
	/**
	 * Setzt den Handler für den angegebenen Typ bei allen Receivern, außer bei
	 * dem mit einer der angegebenen ID, falls diese existieren.
	 * Wird null als Handler übergeben, wird, falls vorhanden, der Handler für
	 * die ID bei den Receivern gelöscht.
	 * 
	 * @param id ID des auszulassenden Receivers
	 * @param handler zu registrierender Handler
	 * @param type Typ, für den der Handler registriert werden soll
	 */
	public void setHandlerForAllExcept(byte id, PJUTEventHandler handler,
			MainType type)
	{
			synchronized(receivers)
			{
				for(Entry<Byte, PJUTServerReceiver> entry :
					receivers.entrySet())
				{
					if(entry.getKey() != id)
					{
						setHandlerFor(entry.getValue(), handler, type);
					}
				}
			}
	}
	
	/**
	 * Setzt den Handler für den angegebenen Typ bei allen Receivern, außer bei
	 * denen mit einer der angegebenen IDs, falls diese existieren.
	 * Wird null als Handler übergeben, wird, falls vorhanden, der Handler für
	 * die ID bei den Receivern gelöscht.
	 * 
	 * @param ids IDs der auszulassenden Receiver
	 * @param handler zu registrierender Handler
	 * @param type Typ, für den der Handler registriert werden soll
	 */
	public void setHandlerForAllExcept(List<Byte> ids,
			PJUTEventHandler handler, MainType type)
	{
			synchronized(receivers)
			{
				for(Entry<Byte, PJUTServerReceiver> entry :
					receivers.entrySet())
				{
					if(!ids.contains(entry.getKey()))
					{
						setHandlerFor(entry.getValue(), handler, type);
					}
				}
			}
	}
	
	/**
	 * Entfernt bei allen bekannten Receiver alle Handler.
	 */
	public void clearHandlers()
	{
		synchronized(receivers)
		{
			for(PJUTServerReceiver res : receivers.values())
			{
				res.clearHandlers();
			}
		}
	}
	
	private void clearHandlers(PJUTServerReceiver receiver)
	{
		if(receiver != null)
		{
			receiver.clearHandlers();
		}
	}
	
	/**
	 * Entfernt bei dem Receiver mit der angegebenen ID alle Handler, sofern
	 * dieser existiert.
	 * 
	 * @param id ID des Ziel-Receivers
	 */
	public void clearHandlers(byte id)
	{
		if(id > 0)
		{			
			synchronized(receivers)
			{
				PJUTServerReceiver receiver = receivers.get(id);
                                clearHandlers(receiver);
			}
			
		}
	}
	
	/**
	 * Entfernt bei den Receivern mit den angegebenen IDs alle Handler, sofern
	 * diese existieren.
	 * 
	 * @param ids IDs der Ziel-Receiver
	 */
	public void clearHandlers(List<Byte> ids)
	{
		for(Byte id : ids)
		{
			clearHandlers(id);
		}
	}
	
	/**
	 * Entfernt bei allen Receivern alle Handler, bis auf bei dem mit der
	 * angegebenen ID.
	 * 
	 * @param id ID des auszulassenden Receivers
	 */
	public void clearHandlersExcept(byte id)
	{
		if(id > 0)
		{
			synchronized(receivers)
			{
				for(Entry<Byte, PJUTServerReceiver> entry : receivers.entrySet())
				{
					if(entry.getKey() != id)
					{
						clearHandlers(entry.getValue());
					}
				}
			}
		}
	}
	
	/**
	 * Entfernt bei allen Receivern alle Handler, und lässt die mit den
	 * angegebenen IDs dabei aus.
	 * 
	 * @param ids IDs der auszulassenden Receiver
	 */
	public void clearHandlersExcept(List<Byte> ids)
	{
		synchronized(receivers)
		{
			for(Entry<Byte, PJUTServerReceiver> entry : receivers.entrySet())
			{
				if(!ids.contains(entry.getKey()))
				{
					clearHandlers(entry.getValue());
				}
			}
		}
	}
}
