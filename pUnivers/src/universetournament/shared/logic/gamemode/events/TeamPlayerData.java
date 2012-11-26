package universetournament.shared.logic.gamemode.events;

import java.io.Serializable;

/**
 * Enthält lediglich eine Spieler-ID und einen Team-Marker für die
 * gebündelte Übertragung.
 * 
 * @author Bernd Marbach
 *
 */
public class TeamPlayerData implements Serializable
{
	private final byte id;
	private final boolean blue;
	
	/**
	 * Erzeugt einen Container, in dem die angegebene Spieler-ID und der
	 * Team-Marker gehalten werden.
	 * 
	 * @param id ID des betroffenen Spielers
	 * @param blue ob das blaue Team betroffen ist
	 */
	public TeamPlayerData(byte id, boolean blue)
	{
		this.id = id;
		this.blue = blue;
	}

	/**
	 * @return Spieler-ID
	 */
	public byte getId()
	{
		return id;
	}

	/**
	 * @return ob das blaue Team betroffen ist
	 */
	public boolean isBlue()
	{
		return blue;
	}
}
