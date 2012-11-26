package universetournament.client.communication;

import javax.swing.JOptionPane;

import universetournament.shared.communication.PJUTSocketMonitor;

/**
 * Einfacher Notifier, welcher einen Dialog anzeigt, wenn die Verbindung
 * verloren geht.
 * 
 * @author Florian Holzschuher
 *
 */
public class ConnectionNotifier implements PJUTSocketMonitor
{
	@Override
	public void connectionLost()
	{
		JOptionPane.showMessageDialog(null, "Verbindung zum Server verloren\n"
				+ "bitte schließen sie ihren Client.");
	}
}
