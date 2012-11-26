package universetournament.shared.data.proto.util;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import universetournament.shared.data.proto.Config;
import universetournament.shared.util.io.UTXMLReader;

/**
 * Einfache Helferklasse, die lediglich die Konfigurationen von der Festplatte
 * einliest und in den ConfigurationManager einträgt.
 * Er liest dazu ein Array von Datei-Pfaden ein, die jeweils eine
 * Konfiguration enthalten (XML-Format).
 * 
 * @author Florian Holzschuher
 *
 */
public class ConfigurationLoader
{
	/**
	 * Standard-Pfad für die Konfigurationsliste.
	 */
	public static final String STD_PATH = "data/configurations.xml";
	private static final Logger logger = Logger
			.getLogger(ConfigurationLoader.class.getName());
	private final File configList;
	private final UTXMLReader reader;
	private final ConfigurationManager manager;

	/**
	 * Erstellt den Konfigurations-Leser, welcher vom angegebenen Pfad die
	 * Konfigurations-Liste einlesen soll.
	 *
	 * @param path Pfad zur Liste mit den Konfigurationsdateien
	 * @throws Exception wenn Pfad unbrauchbar oder Datei nicht existent
	 */
	public ConfigurationLoader(String path) throws Exception
	{
		configList = new File(path);
		if (!configList.exists())
		{
			throw new Exception("Config-Liste existiert nicht");
		}
		reader = new UTXMLReader();
		manager = ConfigurationManager.getInstance();
	}

	/**
	 * Liest alle in der Liste befindlichen Konfigurationen ein, bei denen dies
	 * möglich ist und trägt sie in den ConfigurationManager ein.
	 */
	public void readAll()
	{
		String[] paths = reader.read(String[].class, configList);

		if (paths != null)
		{
			File config = null;

			for (String path : paths)
			{
				config = new File(path);
				readConfig(config);
			}
		}
	}

	private void readConfig(File config)
	{
		if (config.exists())
		{
			Config c = reader.read(Config.class, config);
			if (c != null)
			{
				manager.addConfiguration(c);
			}
		}
		else
		{
			logger.log(Level.SEVERE, "Konnte Konfiguration nicht lesen: "
					+ config.getAbsolutePath());
		}
	}
}
