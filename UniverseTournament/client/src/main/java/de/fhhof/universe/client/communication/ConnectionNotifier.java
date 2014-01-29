package de.fhhof.universe.client.communication;

import de.fhhof.universe.shared.communication.PJUTSocketMonitor;
import javax.swing.JOptionPane;


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
