package universetournament.shared.core;

import java.io.Serializable;

/**
 * Container, welcher bei der Intialisierung des Spiels zur Kapselung einer
 * ID und von Daten dient.
 * 
 * @author Florian Holzschuher
 *
 */
public class InitContainer implements Serializable
{
	private final byte clientId;
	private final Object data;
	
	public InitContainer(byte clientId, Object data)
	{
		this.clientId = clientId;
		this.data = data;
	}

	/**
	 * @return übermittelte Client-ID
	 */
	public byte getClientId()
	{
		return clientId;
	}

	/**
	 * @return enthaltene Daten
	 */
	public Object getData()
	{
		return data;
	}
}