package pjut.testing;

import pjut.shared.events.PJUTEvent;

public class TesterEvent extends PJUTEvent
{
	private static final long serialVersionUID = -7056291833256611450L;

	public byte id;
	public short x, y;
	
	public TesterEvent(byte id, short x, short y)
	{
		super((byte)1);
		this.id = id;
		this.x = x;
		this.y = y;
	}
}
