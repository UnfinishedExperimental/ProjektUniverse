package de.fhhof.universe.shared.logic.entities.requests;

import java.io.Serializable;

/**
 * Request für die ShipFactory im Server, welches Konfiguration und Besitzer
 * des anzulegenden Schiffs beinhaltet.
 * 
 * @author sylence
 *
 */
public class ShipRequest implements Serializable
{
	private final short configId;
	private final byte ownerId;
	
	/**
	 * Erstellt eine Schiffsanfrage mit der angegebenen Konfiguration
	 * und dem übergebenen Besitzer.
	 * 
	 * @param configId Konfigurations-ID
	 * @param ownerId Besitzer-ID
	 */
	public ShipRequest(short configId, byte ownerId)
	{
		this.configId = configId;
		this.ownerId = ownerId;
	}

	/**
	 * @return die Konfigurations-ID
	 */
	public short getConfigId()
	{
		return configId;
	}

	/**
	 * @return die Besitzer-ID
	 */
	public byte getOwnerId()
	{
		return ownerId;
	}
}
