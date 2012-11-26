package universetournament.shared.logic.entities.requests;

import java.io.Serializable;

/**
 * Request für die PlayerFactory im Server, welches Namen und Farbe des
 * anzulegenden Spielers beinhaltet.
 * 
 * @author sylence
 *
 */
public class PlayerRequest implements Serializable
{
	private final String name;
	private final int colorRGB;
	private final byte clientId;
	
	/**
	 * Erstellt einen Spieler-Request mit dem angegebenen Namen, der
	 * angegebenen Farbe und der übergebenen Client-ID.
	 * Der Farbewert ist definiert nach der Color.getRGB() Methode.
	 * Wirdt eine NullPointerException, wenn der Name null ist oder keine
	 * Zeichen enthält.
	 * 
	 * @param name gewünschter Name
	 * @param colorRGB gewünschte Farbe.
	 * @param clientId ID des Clients zu dem der Spieler gehört
	 */
	public PlayerRequest(String name, int colorRGB, byte clientId)
	{
		if(name == null || name.length() == 0)
		{
			throw new NullPointerException("kein gültiger Spielername");
		}
		this.name = name;
		this.colorRGB = colorRGB;
		this.clientId = clientId;
	}

	/**
	 * @return gewünschter Spielername
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return gewünschte Farbe als RGB-Wert (siehe Color.getRGB())
	 */
	public int getColorRGB()
	{
		return colorRGB;
	}

	/**
	 * @return ID des Clients zu dem der Spieler gehört
	 */
	public byte getClientId()
	{
		return clientId;
	}
}
