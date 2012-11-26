package fholzschuher;

import universetournament.client.input.PJUTCommand;
import universetournament.client.input.PJUTInputBuffer;
import universetournament.shared.events.buffers.PJUTEventBuffer;
import universetournament.testing.dheinrich.MoveData;

/**
 * 
 * @author Florian Holzschuher
 *
 */
public class TesterCommand implements PJUTCommand
{
    private short id;
    private PJUTEventBuffer buff;
    private PJUTInputBuffer inBuff;

    public TesterCommand(short id, PJUTEventBuffer buff)
    {
        this.id = id;
        this.buff = buff;
        inBuff = PJUTInputBuffer.getInstance();
    }

    @Override
    public void execute(float timeDiff)
    {
        buff.addEvent(new TestEvent(id, TestEvent.SubEvents.MOVE,
            new MoveData((short) inBuff.getMouseX(),
            (short) inBuff.getMouseY())));
    }
}
