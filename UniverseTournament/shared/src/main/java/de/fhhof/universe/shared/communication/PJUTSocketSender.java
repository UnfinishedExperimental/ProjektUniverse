package de.fhhof.universe.shared.communication;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Runnable, das Objekte aus dem Puffer nimmt und über den Socket verschickt.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTSocketSender extends PJUTSocketThread
{
	/**
	 * Erzeugt ein Runnable, welches, wenn benachrichtigt, Objekte aus dem
	 * Puffer über den Socket verschickt.
	 * Darf auf keinen Fall mit null-Referenzen aufgerufen werden.
	 * 
	 * @param list Pufferliste, die Objekte liefert
	 * @param socket Socket, über den der Gegenseite Objekte Geschickt werden
	 */
	public PJUTSocketSender(LinkedList<Serializable> list, Socket socket)
		throws Exception
	{
		super(list, socket);
	}

	/**
	 * Verbindung initialisieren, Objekte aus dem Puffer einzeln über den
	 * Socket verschicken, so lange Socket offen und Sender aktiviert.
	 * Wartet per wait() auf "this", wenn keine Objekte mehr vorhanden sind.
	 */
	@Override
	public void run()
	{
		ObjectOutputStream ostream = null;
		
		try
		{
			ostream = new ObjectOutputStream(new BufferedOutputStream(
					socket.getOutputStream()));
			
			//Header zur Initialisierung des Streams senden.
			ostream.flush();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			active = false;
		}
		
		boolean transferred = true;
		Serializable current = null;
		int num = 0;
		
		while(active)
		{
			//wenn Liste leer, auf Benachrichtigung warten
			synchronized(list)
			{
				num = list.size();
			}
			
			if(num == 0)
			{
				try
				{
					synchronized(this)
					{
						this.wait();
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				continue;
			}
			
			//wenn letztes Objekt transferiert wurde, neues holen
			if(transferred)
			{
				synchronized(list)
				{
					current = list.pop();
				}
			}
			
			//anderenfalls Erfolgsvariable zurücksetzen
			else
			{
				transferred = true;
			}
			
			try
			{
				ostream.writeObject(current);
				ostream.flush();
				
				//Referenzen löschen, Objekte immer neu übertragen
				ostream.reset();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.err.println("Übertragung fehlgeschlagen -" +
						" Sender-Thread wird beendet");
				
				//Problem berichten
				connectionError();
				active = false;
				transferred = false;
			}
		}
		
		//wenn ungesendetes Objekt vorhanden, wieder zurück in die Queue
		if(!transferred && current != null)
		{
			synchronized(list)
			{
				list.addFirst(current);
			}
		}
	}
	
	@Override
	public void deactivate()
	{
		active = false;
		synchronized(list)
		{
			list.notify();
		}
	}

}
