package pjut.testing;

import pjut.client.input.PJUTCommand;
import pjut.client.input.PJUTInputBuffer;
import pjut.shared.events.buffers.PJUTEventBuffer;

public class TesterCommand implements PJUTCommand
{
	private byte id;
	private PJUTEventBuffer buff;
	private PJUTInputBuffer inBuff;
	
	public TesterCommand(byte id, PJUTEventBuffer buff)
	{
		this.id = id;
		this.buff = buff;
		inBuff = PJUTInputBuffer.getInstance();
	}
	
	@Override
	public void execute(float timeDiff)
	{
		buff.addEvent(new TesterEvent(id, (short)inBuff.getMouseX(),
				(short)inBuff.getMouseY()));
		buff.flush();
	}
}
