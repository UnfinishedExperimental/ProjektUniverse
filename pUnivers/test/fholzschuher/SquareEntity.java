package fholzschuher;

import universetournament.shared.logic.entities.Entity;
/**
 * 
 * @author Florian Holzschuher
 *
 */
public class SquareEntity extends Entity
{
	private short x, y;
	
	public SquareEntity(short id)
	{
		super(id);
		x = 0;
		y = 0;
	}
	
	public short getX()
	{
		return x;
	}
	
	public short getY()
	{
		return y;
	}
	
	public void setPos(short x, short y)
	{
		this.x = x;
		this.y = y;
	}
}
