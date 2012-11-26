package pjut.shared.communication;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Runnable, das ankommende Objekte entgegennimmt und in den Puffer schreibt.
 * 
 * @author sylence
 *
 */
public class PJUTSocketReceiver extends PJUTSocketThread
{
	/**
	 * Erzeugt ein Runnable, welches über den Socket ankommende Objekte in den
	 * Puffer schreibt.
	 * Darf auf keinen Fall mit null-Referenzen aufgerufen werden.
	 * 
	 * @param list Pufferliste, die Objekte aufnimmt
	 * @param socket Socket, über den von der Gegenseite Objekte ankommen
	 */
	public PJUTSocketReceiver(LinkedList<Serializable> list, Socket socket)
		throws Exception
	{
		super(list, socket);
	}

	@Override
	/**
	 * Verbindung initialisieren und über den Socket kommende Objekte
	 * annehmen und in den Puffer schreiben, so lange Socket offen und
	 * Receiver aktiviert.
	 */
	public void run()
	{
		ObjectInputStream istream = null;
		
		try
		{
			istream = new ObjectInputStream(new BufferedInputStream(
					socket.getInputStream()));
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
			active = false;
		}
		
		while(active)
		{
			try
			{
				/*
				 * Cast sollte sicher sein - wäre es nicht serialisierbar,
				 * könnte es nicht über den Socket verschickt werden.
				 */
				Serializable o = (Serializable) istream.readObject();
				
				synchronized(list)
				{
					list.add(o);
					list.notify();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.err.println("Übertragung fehlgeschlagen -" +
					" Empfänger-Thread wird beendet");
				
				//Problem berichten
				connectionError();
				active = false;
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

}
