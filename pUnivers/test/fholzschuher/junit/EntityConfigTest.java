package fholzschuher.junit;

import universetournament.shared.data.proto.BlasterConfig;
import universetournament.shared.data.proto.ShipConfig;
import universetournament.shared.data.proto.util.ConfigurationLoader;
import universetournament.shared.data.proto.util.ConfigurationManager;
import junit.framework.TestCase;

/**
 * Testet das Konfigurationsmanagement.
 * 
 * @author sylence
 *
 */
public class EntityConfigTest extends TestCase
{
	private ConfigurationLoader loader;
	private ConfigurationManager manager;
	
	@Override
	protected void setUp()
	{
		loader = null;
		manager = ConfigurationManager.getInstance();
	}
	
	@Override
	protected void tearDown()
	{
		loader = null;
		manager = null;
	}
	
	/**
	 * "Null" laden wollen.
	 */
	public void testLoadNull()
	{
		boolean correct = false;
		
		try
		{
			loader = new ConfigurationLoader(null);
		}
		catch (NullPointerException e)
		{
			correct = true;
		}
		catch (Exception e)
		{
			//der null-Pointer ist entscheidend
		}
		
		TestCase.assertTrue(correct);
	}
	
	/**
	 * nicht existierende Datei laden wollen.
	 */
	public void testNonexistentLoad()
	{
		boolean correct = false;
		
		try
		{
			loader = new ConfigurationLoader(
					"/komischer/nichtexistenter/pfad.xml");
		}
		catch (Exception e)
		{
			correct = true;
		}
		
		TestCase.assertTrue(correct);
	}
	
	/**
	 * Testet den Zugriff auf einen Ungefüllten Manager
	 */
	public void testUnfilled()
	{
		TestCase.assertNull(manager.getConfiguration(ShipConfig.class,
				(short)1));
	}
	
	/**
	 * Normale Konfigurationsliste einlesen.
	 */
	public void testNormalLoad()
	{
		//geht natürlich nur, wenn die Daten vollständig sind!
		boolean correct = true;
		
		try
		{
			loader = new ConfigurationLoader(ConfigurationLoader.STD_PATH);
		}
		catch (Exception e)
		{
			correct = false;
		}
		TestCase.assertTrue(correct);
		
		//bekannt gewordene Konfigurationen einlesen
		loader.readAll();
		
		//bekannte Testkonfiguration abprüfen
		TestCase.assertNotNull(manager.getConfiguration(ShipConfig.class,
				(short)1));
	}
	
	/**
	 * Nochmaliges Einlesen darf keine Fehler erzeugen.
	 */
	public void testLoadRepeat()
	{
		//geht natürlich nur, wenn die Daten vollständig sind!
		boolean correct = true;
		
		try
		{
			loader = new ConfigurationLoader(ConfigurationLoader.STD_PATH);
		}
		catch (Exception e)
		{
			correct = false;
		}
		TestCase.assertTrue(correct);
		
		//einmal
		loader.readAll();
		
		//noch einmal
		loader.readAll();
		
		//und noch einmal
		loader.readAll();
		
		//bekannte Testkonfiguration abprüfen
		TestCase.assertNotNull(manager.getConfiguration(ShipConfig.class,
				(short)1));
		
		//neuer loader
		try
		{
			loader = new ConfigurationLoader(ConfigurationLoader.STD_PATH);
		}
		catch (Exception e)
		{
			correct = false;
		}
		TestCase.assertTrue(correct);
		
		//ein weiteres Mal
		loader.readAll();
		
		//und weils so schön ist ...
		loader.readAll();
		
		//bekannte Testkonfiguration abprüfen
		TestCase.assertNotNull(manager.getConfiguration(ShipConfig.class,
				(short)1));
	}
	
	/**
	 * Anfrage von unbekannten und falsch klassifizierten Konfigurationen.
	 */
	public void testWrongRequest()
	{
		//geht natürlich nur, wenn die Daten vollständig sind!
		boolean correct = true;
		
		try
		{
			loader = new ConfigurationLoader(ConfigurationLoader.STD_PATH);
		}
		catch (Exception e)
		{
			correct = false;
		}
		TestCase.assertTrue(correct);
		
		loader.readAll();
		
		//Konfiguration ist kein Blaster, aber vorhanden
		TestCase.assertNull(manager.getConfiguration(BlasterConfig.class,
				(short)1));
		
		//Konfiguration existiert nicht
		TestCase.assertNull(manager.getConfiguration(ShipConfig.class,
				(short)-1));
	}
}
