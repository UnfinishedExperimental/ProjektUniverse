package universetournament.shared.data.proto;

import java.io.Serializable;

/**
 * Abstrakte Oberklasse für alle Objekt-Konfigurationen, die lediglich
 * deren einzigartige ID enthält.
 * Bei der Remote-Objekterstellung sollte lediglich diese Nummer und
 * nicht die serialisierte Konfiguration übertragen werden.
 * 
 * @author Thikimchi Nguyen
 *
 */
public abstract class Config implements Serializable
{
	// ID, die die Konfiguration eindeutig identifiziert.
	private final short uid;
	private final String name, description;

	/**
	 * Erzeugt eine Konfiguration mit ihrer eindeutigen ID.
	 * Sollte kaum benötigt werden, da richtige Konfigurationen normalerweise
	 * über extern ins Programm geladen werden.
	 *
	 * @param uid neue eindeutige ID
	 */
	public Config()
	{
		this.uid = 0;
		name = "unbenannt";
		description = "keine Beschreibung";
	}

	/**
	 * @return einzigartige ID der Konfiguration
	 */
	public short getUid()
	{
		return uid;
	}

	/**
	 * @return Name der Konfiguration
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return Beschreibung der Konfiguration
	 */
	public String getDescription()
	{
		return description;
	}

	@Override
	public String toString()
	{
		return name;
	}
}
