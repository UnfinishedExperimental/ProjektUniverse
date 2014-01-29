package de.fhhof.universe.shared.logic.entities;

import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;
import de.fhhof.universe.shared.events.SubType;

/**
 * Eine Instanz dieser Klasse, eine Player Entity enthält die Informationen zu
 * einem Spieler, wie Name, ID, Farbe, Punktzahl, Abschüsse und Tode.
 * 
 * @author sylence
 *
 */
public class PJUTPlayer extends Entity implements Serializable
{
	private static final long serialVersionUID = -2171364221286430053L;

	public enum SubEvents implements SubType
	{
		SetScore, SetKills, SetDeaths;
	}

	transient private String name;
	transient private byte clientId;
	transient private short score, kills, deaths;
	transient private Color color;

	/**
	 * Erzeugt einen neuen Spieler mit der angegebenen ID, dem angegebenen
	 * Namen und der anfänglichen Farbe weiß.
	 * Wirft eine NullPointerException wenn der übergebene Name unbrauchbar
	 * ist.
	 */
	public PJUTPlayer(String name, short id, byte clientId)
	{
		super(id);
		
		if(name != null && name.length() > 0)
		{
			this.name = name;
		}
		else
		{
			throw new NullPointerException("Name kann nicht verwendet werden");
		}
		
		this.clientId = clientId;
		score = 0;
		kills = 0;
		deaths = 0;
		color = new Color(1.f, 1.f, 1.f);
	}

	/**
	 * @return Name des Spielers.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return wie oft der Spieler gestorben ist.
	 */
	public short getDeaths()
	{
		return deaths;
	}

	/**
	 * @param deaths neue Anzahl der Tode.
	 */
	public void setDeaths(short deaths)
	{
		this.deaths = deaths;
	}

	/**
	 * Erhöht den Todes-Zähler um 1.
	 */
	public void died()
	{
		++deaths;
	}
	
	/**
	 * @param kills neue Anzahl der Abschüsse.
	 */
	public void setKills(short kills)
	{
		this.kills = kills;
	}

	/**
	 * @return Zahl der Abschüsse durch den Spieler.
	 */
	public short getKills()
	{
		return kills;
	}

	/**
	 * Erhöht den Kill-Zähler um 1.
	 */
	public void killed()
	{
		++kills;
	}
	
	/**
	 * @param score neue Punktzahl
	 */
	public void setScore(short score)
	{
		this.score = score;
	}
	
	/**
	 * @param diff Wert um den sich die Punktzahl ändern soll
	 */
	public void modScore(short diff)
	{
		score += diff;
	}

	/**
	 * @return aktuelle Punktzahl des Spielers
	 */
	public short getScore()
	{
		return score;
	}

	/**
	 * @return ID des Clients, bzw. der Verbindung
	 */
	public byte getClientId()
	{
		return clientId;
	}

	/**
	 * Setzt die neue Spielerfarbe.
	 * Ignoriert Aufrufe mit null.
	 * 
	 * @param color neue Spielerfarbe
	 */
	public void setColor(Color color)
	{
		if(color != null)
		{
			this.color = color;
		}
	}

	/**
	 * @return aktuelle Spielerfarbe
	 */
	public Color getColor()
	{
		return color;
	}
	
    private void writeObject(java.io.ObjectOutputStream out) throws IOException
    {
        out.writeUTF(name);
        out.writeByte(clientId);
        out.writeShort(score);
        out.writeShort(kills);
        out.writeShort(deaths);
        
        out.writeInt(color.getRGB());
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException
    {
        name = in.readUTF();
        clientId = in.readByte();
        score = in.readShort();
        kills = in.readShort();
        deaths = in.readShort();
        
        color = new Color(in.readInt());
    }
}
