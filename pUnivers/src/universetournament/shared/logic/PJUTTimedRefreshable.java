package universetournament.shared.logic;

/**
 * Interface für ein Objekt, welches regelmäßig dazu aufgefordert wird,
 * sich zu aktualisieren, meistens in der Hauptschleife des jeweiligen
 * Programms.
 * 
 * @author sylence
 *
 */
public interface PJUTTimedRefreshable
{
	/**
	 * Wird von dem Objekt aufgerufen, bei dem das Refreshable registriert
	 * wurde und übergibt, wie viel Zeit seit der letzten Auffrischung
	 * vergangen ist.
	 * 
	 * @param timeDiff vergangene Zeit in Nanosekunden
	 */
	public abstract void refresh(float timeDiff);
}
