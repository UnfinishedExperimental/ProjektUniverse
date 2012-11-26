package universetournament.shared.communication;

/**
 * Interface für eine Objekt, welches benachrichtigt wird, wenn ein Socket
 * geschlossen wurde oder nichtmehr benutzbar ist.
 * 
 * @author Florian Holzschuher
 *
 */
public interface PJUTSocketMonitor
{
	/**
	 * Methode, die aufgerufen wird, wenn festgestellt wird, dass ein Socket
	 * nichtmehr zu verwenden ist.
	 */
	public abstract void connectionLost();
}
