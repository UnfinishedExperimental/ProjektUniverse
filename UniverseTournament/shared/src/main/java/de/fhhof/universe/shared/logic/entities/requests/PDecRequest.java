package de.fhhof.universe.shared.logic.entities.requests;

import darwin.util.math.base.vector.ImmutableVector;
import darwin.util.math.base.vector.Vector3;
import java.io.Serializable;


public class PDecRequest implements Serializable
{
	private ImmutableVector<Vector3> position;
	private short config;
	
	public PDecRequest(ImmutableVector<Vector3> position, short config)
	{
		this.position = position;
		this.config = config;
	}

	/**
	 * @return the position
	 */
	public ImmutableVector<Vector3> getPosition()
	{
		return position;
	}

	/**
	 * @return the config
	 */
	public short getConfig()
	{
		return config;
	}
}
