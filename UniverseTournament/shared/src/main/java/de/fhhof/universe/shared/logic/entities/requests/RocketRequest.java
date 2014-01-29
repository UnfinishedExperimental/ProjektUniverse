package de.fhhof.universe.shared.logic.entities.requests;

import java.io.Serializable;

/**
 * Request für die RocketFactory im Server, welches Konfiguration und Besitzer
 * der anzulegenden Rakete beinhaltet.
 * 
 * @author sylence
 *
 */
public class RocketRequest implements Serializable
{
	private final short configId;
	private final byte ownerId;

	/**
	 * Erstellt eine Raketenanfrage mit der angegebenen Konfiguration
	 * und dem übergebenen Besitzer.
	 * 
	 * @param configId Konfigurations-ID
	 * @param ownerId Besitzer-ID des Abschießenden
	 */
	public RocketRequest(short configId, byte ownerId)
	{
		this.configId = configId;
		this.ownerId = ownerId;
	}

	/**
	 * @return Konfigurations-ID
	 */
	public short getConfigId()
	{
		return configId;
	}

	/**
	 * @return ID des Abfeuernden
	 */
	public byte getOwnerId()
	{
		return ownerId;
	}
}
