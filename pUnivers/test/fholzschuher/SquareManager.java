package fholzschuher;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * 
 * @author Florian Holzschuher
 *
 */
public class SquareManager
{
	private static final SquareManager instance = new SquareManager();
	
	private HashMap<Short, SquareEntity> squares;
	
	public static synchronized SquareManager getInstance()
	{
		return instance;
	}
	
	private SquareManager()
	{
		squares = new HashMap<Short, SquareEntity>();
	}
	
	public SquareEntity createSquare(short id)
	{
		SquareEntity square = new SquareEntity(id);
		
		EntityController<SquareEntity> ctrl =
			new EntityController<SquareEntity>(square);
		ctrl.addSubController(new TestMovementController(square));
		
		UTEntityManager.getInstance().setController(id, ctrl);
		
		squares.put(id, square);
		TestEvent te = new TestEvent(id,
        		Entity.SubEvents.CREATE, null);
        PJUTEventBuffManager.getInstance().sendToAll(te);
		
		return square;
	}
	
	public SquareEntity getSquare(short id)
	{
		return squares.get(id);
	}
	
	public void removeSquare(short id)
	{
		TestEvent te = new TestEvent(id, Entity.SubEvents.DELETE, null);
        PJUTEventBuffManager.getInstance().sendToAll(te);
        
        UTEntityManager.getInstance().setController(id, null);
        
		squares.remove(id);
	}
	
	public Set<Entry<Short, SquareEntity>> getSquares()
	{
		return squares.entrySet();
	}
}
