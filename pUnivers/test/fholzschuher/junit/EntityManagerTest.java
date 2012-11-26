package fholzschuher.junit;

import universetournament.shared.logic.entities.util.UTEntityManager;
import junit.framework.TestCase;

/**
 * Testet den EntityManager, welcher die Controller der Entities verwaltet.
 * 
 * @author sylence
 *
 */
public class EntityManagerTest extends TestCase
{
	private UTEntityManager manager;
	
	@Override
	protected void setUp()
	{
		manager = UTEntityManager.getInstance();
	}
	
	@Override
	protected void tearDown()
	{
		manager = null;
	}
}
