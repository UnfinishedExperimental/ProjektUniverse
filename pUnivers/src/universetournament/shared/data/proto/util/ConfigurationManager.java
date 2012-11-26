package universetournament.shared.data.proto.util;

import java.util.HashMap;

import universetournament.shared.data.proto.Config;

/**
 * Manager, der die Objekt-Konfigurationen für den internen Gebrauch
 * über ihre eindeutige ID bereitstellt.
 * Die Eindeutigen IDs sollten >= 1 sein, um Fehler leichter erkennen zu
 * können.
 * Wird ein Objekt über Remote erstellt können hier die Konfigurationen
 * abgerufen werden, wenn sie vorher geladen wurden.
 * 
 * @author Florian Holzschuher
 *
 */
public class ConfigurationManager
{
	private static final ConfigurationManager instance =
		new ConfigurationManager();
	
	private static final int INITIAL_SIZE = 32;
	
	private HashMap<Short, Config> configurations;
	
	/**
	 * @return Instanz des Managers
	 */
	public static synchronized ConfigurationManager getInstance()
	{
		return instance;
	}
	
	private ConfigurationManager()
	{
		configurations = new HashMap<Short, Config>(INITIAL_SIZE);
	}
	
	/**
	 * Sieht nach, ob unter der angegebenen ID eine Konfiguration gespeichert
	 * ist und ob diese eine Instanz der gewüschten Klasse ist.
	 * Trifft beides zu, wird die Konfiguration zurückgegeben, wenn nicht wird
	 * null zurückgegeben.
	 * 
	 * @param <T> Konfigurationstyp der benötigt wird.
	 * @param cls angeforderte Klasse (zur Überprüfung)
	 * @param uid eindeutige ID der angefragten Konfiguration
	 * @return benötigte Konfiguration oder null
	 */
	public <T extends Config> T getConfiguration(Class<T> cls, short uid)
	{
		Config c = configurations.get(uid);
		T result = null;
		
		if(cls.isInstance(c))
		{
			result = cls.cast(c);
		}
		
		return result;
	}
	
	/**
	 * Fügt den bekannten Konfigurationen eine neue über ihre Id hinzu.
	 * Ist die übergebene Konfiguration null oder ist unter der ID bereits eine
	 * Konfiguration registriert, wird nichts verändert.
	 * 
	 * @param c hinzuzufügende Konfiguration
	 * @return Erfolg
	 */
	public boolean addConfiguration(Config c)
	{
		boolean success = false;
		
		if(c != null)
		{
			short uid = c.getUid();
			
			if(configurations.get(uid) == null)
			{
				configurations.put(uid, c);
				success = true;
			}
		}
		
		return success;
	}
	
	/**
	 * Entfernt für die übergebene ID, falls vorhanden, die registrierte
	 * Konfiguration.
	 * 
	 * @param uid ID der zu löschenden Konfiguration
	 */
	public void removeConfiguration(short uid)
	{
		configurations.remove(uid);
	}
}
