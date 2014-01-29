package de.fhhof.universe.shared.logic.entities.requests;

import java.io.Serializable;

/**
 * Request für die ShotFactory im Server, welches Konfiguration und Besitzer
 * des anzulegenden Schusses beinhaltet.
 * 
 * @author sylence
 *
 */
public class ShotRequest implements Serializable
{
	private final short configId;
	private final byte ownerId;

	/**
	 * Erstellt eine Schussanfrage mit der angegebenen Konfiguration
	 * und dem übergebenen Besitzer.
	 * 
	 * @param configId Konfigurations-ID
	 * @param ownerId Besitzer-ID des Abschießenden
	 */
	public ShotRequest(short configId, byte ownerId)
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
