package pjut.shared.events;

import java.io.UnsupportedEncodingException;

/**
 * Nachrichtenevent, welches einen Nachrichtenstring für die Gegenseite
 * enthält.
 * Diese Nachrichten sollten in der Regel dem Nutzer ausgegeben und/oder
 * geloggt werden.
 * Gespeichert werden die Bytes des Strings im UTF-8-Format, um unnötige
 * Datenübermittlungen und Kodierungsprobleme zu vermeiden.
 * 
 * @author sylence
 *
 */
public class PJUTMessageEvent extends PJUTEvent
{
	/**
	 * Verwendetes Encoding.
	 */
	public static final String encoding = "UTF-8";
	
	private static final long serialVersionUID = -1091581431976335726L;
	
	private byte[] message;
	
	/**
	 * Erzeugt ein neues Nachrichtenevent, der interne Spreicherpointer wird
	 * auf "null" gesetzt.
	 */
	public PJUTMessageEvent()
	{
		super(PJUTEvent.TYPE_MESSAGE);
		message = null;
	}

	/**
	 * Setzt den zu übermittelnden Nachrichtenstring.
	 * Akzeptiert auch null, dann wird nichts übermittelt.
	 * Tritt ein Kodierungsfehler auf, wird dies auf stderr ausgegeben, und
	 * keine Nachricht wird gespeichert, da dies als schwerer systembedingter
	 * Fehler aufgefasst wird, der nicht auftreten sollte, weil das Encoding
	 * fest vorgegeben ist.
	 * 
	 * @param message neue Nachricht.
	 */
	public void setMessage(String message)
	{
		if(message == null)
		{
			this.message = null;
		}
		else
		{
			try
			{
				this.message = message.getBytes(encoding);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Konvertiert die gespeicherten Bytes zu einem String.
	 * Gibt null zurück, wenn keine Nachricht enthalten ist oder
	 * ein Kodierungsfehler auftritt.
	 * 
	 * @return enthaltene Nachricht.
	 */
	public String getMessage()
	{
		String string = null;
		
		if(message != null)
		{
			try
			{
				string = new String(message, encoding);
			}
			catch (UnsupportedEncodingException e)
			{
				e.printStackTrace();
			}
		}
		
		return string;
	}
}
