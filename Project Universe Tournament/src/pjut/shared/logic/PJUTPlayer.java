package pjut.shared.logic;

import java.io.Serializable;

/**
 * Ein Objekt dieser Klasse enthält die Informationen zu einem Spieler.
 * 
 * @author sylence
 *
 */
public class PJUTPlayer implements Serializable
{
	private static final long serialVersionUID = -2171364221286430053L;
	
	private String name;
	
	//sollte mit Socket-ID übereinstimmen
	private byte id;

	/**
	 * Erzeugt einen neuen Spieler mit der ID 0 und dem Namen "Spieler".
	 */
	public PJUTPlayer()
	{
		name = "Spieler";
		id = 0;
	}

	/**
	 * Setzt den Spielernamen.
	 * Akzeptiert kein null und keine Strings der Länge 0.
	 * 
	 * @param name neuer Spielername
	 * @return Erfolg
	 */
	public boolean setName(String name)
	{
		boolean success = false;
		
		if(name != null && name.length() > 0)
		{
			success = true;
			this.name = name;
		}
		
		return success;
	}

	/**
	 * @return Name des Spielers.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Setzt die ID des Spielers.
	 * Akzeptiert nur positive Werte und die 0 als "nicht gesetzt"-Wert.
	 * 
	 * @param id neue Spieler-ID
	 */
	public void setId(byte id)
	{
		if(id >= 0)
		{
			this.id = id;
		}
	}

	/**
	 * @return ID des Spielers.
	 */
	public byte getId()
	{
		return id;
	}
}