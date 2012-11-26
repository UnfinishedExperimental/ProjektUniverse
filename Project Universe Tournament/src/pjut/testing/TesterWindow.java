package pjut.testing;

import java.awt.event.MouseEvent;

import javax.swing.JFrame;

import pjut.client.input.PJUTInputBuffer;
import pjut.client.input.PJUTInputDistributor;
import pjut.shared.events.PJUTEvent;
import pjut.shared.events.buffers.PJUTEventBuffer;
import pjut.shared.logic.PJUTEventHandler;

public class TesterWindow extends JFrame implements PJUTEventHandler, Runnable
{
	private TesterComponent comp;
	private PJUTEventBuffer eventBuff;
	
	public TesterWindow(PJUTEventBuffer eventBuff)
	{
		this.eventBuff = eventBuff;
		
		comp = new TesterComponent();
		this.add(comp);
		
		PJUTInputBuffer buffer = PJUTInputBuffer.getInstance();
		comp.addMouseListener(buffer);
		comp.addMouseMotionListener(buffer);
		comp.addKeyListener(buffer);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setSize(800,600);
		this.setVisible(true);
	}
	
	public void handleEvent(PJUTEvent event)
	{
		TesterEvent tEvent = (TesterEvent) event;
		
		if(tEvent.getSubType() == 0)
		{
			comp.setCirclePos(tEvent.id, tEvent.x, tEvent.y);
		}
		else
		{
			TesterCommand command = new TesterCommand(tEvent.id, eventBuff);
			
			PJUTInputDistributor.getInstance().bindToMousePress(
					MouseEvent.BUTTON1, command);
			PJUTInputDistributor.getInstance().bindToMouseHold(
					MouseEvent.BUTTON1, command);
		}
	}

	@Override
	public void run()
	{
		while(true)
		{
			PJUTInputDistributor.getInstance().refresh(0);
			
			try
			{
				Thread.sleep(16);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			
			comp.repaint();
		}
	}
}
