package fholzschuher.junit;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Lässt alle JUnit-Tests im package als eine Suite durchlaufen.
 * 
 * @author sylence
 *
 */
public class AllTest
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite();
		
		/*
		 * Systeme in hierarchischer Reihenfolge, in der sie aufeinander
		 * basieren testen, trotzdem möglichst unabhängig.
		 */
		suite.addTestSuite(CoreTest.class);
		suite.addTestSuite(ConnectionTest.class);
		suite.addTestSuite(EventTest.class);
		suite.addTestSuite(EntityManagerTest.class);
		suite.addTestSuite(InputTest.class);
		suite.addTestSuite(EntityConfigTest.class);
		return suite;
	}
}
