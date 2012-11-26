package pjut.shared.events;

import java.io.Serializable;

/**
 * Oberklasse für alle Events, die an den entsprechenden Empfänger
 * geleitet werden sollen, meist über das Netzwerk.
 * Besitzt lediglich einen Basis- und einen Subtyp.
 * Events sollten generell schlank gehalten werden, um die
 * Netzwerkkommunikation effizient zu halten. Daher ist es rastam immer nur den
 * minimal nötigen Datentyp (byte statt int, float statt double) zu benutzen,
 * falls dies möglich ist.
 * In dieser Klasse sind beispielhafte Basis-Bits definiert, welche die
 * Basistypen der Events unterscheiden können. Diese können, müssen aber nicht
 * verwendet werden.
 * 
 * @author sylence
 *
 */
public abstract class PJUTEvent implements Serializable
{
	/**
	 * Basis-Bits, die anzeigen, dass kein Basistyp vorhanden ist.
	 * Dient vorrangig dazu, anzuzeigen, dass noch kein Typ gesetzt wurde.
	 */
	public static final byte NO_TYPE = 0;
	
	/**
	 * Basis-Bits, die anzeigen, dass ein Objekt-Event vorliegt.
	 * Diese sind Events, die direkt Objekte im Spieluniversum betreffen.
	 */
	public static final byte TYPE_OBJECT = 1;
	
	/**
	 * Basis-Bits, die anzeigen, dass ein Spiel-Event vorliegt.
	 * Diese sind spezifisch für die aktuelle Spiellogik, also Modus-abhängig.
	 */
	public static final byte TYPE_GAME = 2;
	
	/**
	 * Basis-Bits, die anzeigen, dass ein Entitäten-Event vorliegt.
	 * Sie betreffen direkt besondere Instanzen, die direkt mit Events versorgt
	 * werden können.
	 */
	public static final byte TYPE_ENTITY = 3;
	
	/**
	 * Basis-Bits, die anzeigen, dass ein Programm-Event vorliegt.
	 * Sie repräsentieren Ereignisse, die dem Spielegeschehen übergeordnet
	 * sind und das ganze Programm betreffen.
	 */
	public static final byte TYPE_PROGRAM = 4;
	
	/**
	 * Basis-Bits, die anzeigen, dass ein Nachrichten-Event vorliegt.
	 * Sie dienen zum Verschicken von Textnachrichten zwischen Client und
	 * Server.
	 */
	public static final byte TYPE_MESSAGE = 5;
	
	private static final long serialVersionUID = 7024392954089509792L;
	
	private byte type, subType;
	
	/**
	 * Erstellt das Event ohne definierte Typen (beide sind 0).
	 */
	public PJUTEvent()
	{
		this(NO_TYPE, NO_TYPE);
	}
	
	/**
	 * Erstellt das Event direkt mit dem angegebenen Typ, noch ohne Unter-Typ.
	 * Alle Typen sollten größer als 0 sein.
	 * Führt keine Überprüfungen durch, ob die Werte Sinn machen.
	 * 
	 * @param type gewünschter Basis-Typ
	 */
	public PJUTEvent(byte type)
	{
		this(type, NO_TYPE);
	}
	
	/**
	 * Erstellt das Event direkt mit dem angegebenen Basistyp und Untertyp.
	 * Alle Typen sollten größer als 0 sein.
	 * Führt keine Überprüfungen durch, ob die Werte Sinn machen.
	 * 
	 * @param type gewünschter Basistyp
	 * @param subType gewünschter Untertyp
	 */
	public PJUTEvent(byte type, byte subType)
	{
		this.type = type;
		this.subType = subType;
	}
	
	/**
	 * Liefert den Basistyp des Events.
	 * Ist dieser 0, wurde noch kein Typ gesetzt.
	 * 
	 * @return Basistyp des Events
	 */
	public byte getType()
	{
		return type;
	}
	
	/**
	 * Setzt den Basistyp des Events.
	 * Ignoriert Werte, die kleiner als eins sind.
	 * 
	 * @param type neuer Event-Basistyp
	 */
	public void setType(byte type)
	{
		if(type > 0)
		{
			this.type = type;
		}
	}

	/**
	 * Setzt den Untertyp des Events.
	 * Ignoriert Werte, die kleiner als eins sind.
	 * 
	 * @param subType neuer Event-Untertyp
	 */
	public void setSubType(byte subType)
	{
		if(subType > 0)
		{
			this.subType = subType;
		}
	}

	/**
	 * Liefert den Untertyp des Events.
	 * Ist dieser 0, wurde noch kein Typ gesetzt.
	 * 
	 * @return Untertyp des Events
	 */
	public byte getSubType()
	{
		return subType;
	}
}
