package universetournament.shared.logic.entities.requests;

import java.io.Serializable;

import universetournament.shared.util.math.Vec3;

public class PDecRequest implements Serializable
{
	private Vec3 position;
	private short config;
	
	public PDecRequest(Vec3 position, short config)
	{
		this.position = position;
		this.config = config;
	}

	/**
	 * @return the position
	 */
	public Vec3 getPosition()
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
