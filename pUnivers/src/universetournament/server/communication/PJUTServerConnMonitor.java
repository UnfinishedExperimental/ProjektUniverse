package universetournament.server.communication;

/**
 * Interface für eine Objekt, welches benachrichtigt wird, wenn ein Socket
 * geschlossen wurde oder nichtmehr benutzbar ist.
 * Variante für den Server, bei dem Verbindungen mit einer ID identifiziert
 * werden.
 * 
 * @author Florian Holzschuher
 *
 */
public interface PJUTServerConnMonitor
{
	/**
	 * Methode, die aufgerufen wird, wenn festgestellt wird, dass ein Socket
	 * nichtmehr zu verwenden ist.
	 * 
	 * @param id ID des Sockets, welcher nichtmehr funktioniert
	 */
	public void connectionLost(byte id);
}
