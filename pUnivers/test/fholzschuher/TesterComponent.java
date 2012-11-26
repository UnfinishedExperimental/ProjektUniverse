package fholzschuher;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.events.GameEvent;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * 
 * @author Florian Holzschuher
 *
 */
public class TesterComponent extends JComponent implements PJUTEventHandler
{
	private List<SquareEntity> squares;
	
	public TesterComponent()
	{
		squares = new ArrayList<SquareEntity>();
	}
	
    @Override
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		for(SquareEntity se : squares)
		{
			g2.drawRect(se.getX() - 5, se.getY() - 5, 10, 10);
		}
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		try
		{
			short id = ((EntityEvent)event).getId();
			switch((Entity.SubEvents) event.getSub())
			{
				case CREATE:
					SquareEntity se = new SquareEntity(id);
					squares.add(se);
					
					EntityController<SquareEntity> ctrl =
						new EntityController<SquareEntity>(se);
					ctrl.addSubController(new TestMovementController(se));
					
					UTEntityManager.getInstance().setController(id, ctrl);
					break;
					
				case DELETE:
					for(SquareEntity sqe : squares)
					{
						if(sqe.getId() == id)
						{
							squares.remove(sqe);
							break;
						}
					}
					UTEntityManager.getInstance().setController(id, null);
					break;
			}
		}
		catch(Exception e)
		{
			System.out.println("Mist, alles Mist");
		}
	}
}
