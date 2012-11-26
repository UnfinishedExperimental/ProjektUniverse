package fholzschuher;

import java.awt.event.MouseEvent;

import universetournament.client.input.PJUTInputDistributor;
import universetournament.shared.events.buffers.PJUTEventBuffer;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.events.GameEvent;

/**
 * 
 * @author Florian Holzschuher
 *
 */
public class TesterCommandCreator implements PJUTEventHandler
{
	private PJUTEventBuffer eventBuff;
	
	public TesterCommandCreator(PJUTEventBuffer eventBuff)
	{
		this.eventBuff = eventBuff;
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		TestEvent tEvent = (TestEvent) event;
		
		TesterCommand command = new TesterCommand(tEvent.getId(),
                eventBuff);

        PJUTInputDistributor.getInstance().bindToMousePress(
                MouseEvent.BUTTON1, command);
        PJUTInputDistributor.getInstance().bindToMouseHold(
                MouseEvent.BUTTON1, command);
	}
}
