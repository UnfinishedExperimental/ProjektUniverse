package universetournament.server.communication;

/**
 * Interface für ein Objekt, welches darauf lauscht, dass neue Verbindungen
 * aufgebaut und alte entfernt werden.
 * 
 * @author Florian Holzschuher
 *
 */
public interface PJUTConnectionListener
{
	/**
	 * Wird aufgerufen, wenn der Liste der Verbindungen eine neue Verbindung
	 * übergeben wird, bevor diese in die aktiven Verbindungen einsortiert
	 * wird.
	 * So wird der Implementierung des Interfaces ermöglicht, die
	 * enstprechenden Verarbeitungsroutinen einzuklinken, bevor der Socket
	 * in die Verwaltung aufgenommen wird.
	 * Wird unter normalen Umständen nie mit Werten kleiner als 1 aufgerufen.
	 * 
	 * @param sock neu verbundener Socket.
	 */
	public abstract void connectionAdded(PJUTServerSocket sock);
	
	/**
	 * Wird aufgerufen, nachdem aus der Liste der Verbindungen eine Verbindung
	 * entfernt wurde.
	 * Zu diesem Zeitpunkt kann davon ausgegangen werden, dass die Verbindung
	 * nichtmehr offen ist.
	 * Wird unter normalen Umständen nie mit Werten kleiner als 1 aufgerufen.
	 * 
	 * @param id ID der entfernten Verbindung
	 */
	public abstract void connectionClosed(byte id);
}
