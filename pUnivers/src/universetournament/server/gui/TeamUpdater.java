package universetournament.server.gui;

import java.util.Set;
import java.util.Map.Entry;

import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.gamemode.UTTeamModeData;
/**
 * Runnable, welches in einem fest definierten Intervall die Daten des
 * teambasierten Spielmodus in das GUI einträgt.
 * 
 * @author Daniela Geilert
 *
 */
public class TeamUpdater extends ViewUpdater<UTTeamModeData>
{
	/**
	 * Erstellt einen Updater, welcher die Daten aus dem übergebenen
	 * Datenobjekt in das angegebene GUI einträgt.
	 * Wirft eine NullPointerException, wenn mindestens ein Parameter null ist.
	 * 
	 * @param gui zu aktualisierendes GUI
	 * @param data zu verwendendes Datenobjekt.
	 */
	public TeamUpdater(Gui_Serverans gui, UTTeamModeData data)
	{
		super(gui, data);
	}

	@Override
	protected Set<Entry<Byte, PJUTPlayer>> updatePlayers()
	{
		Set<Entry<Byte, PJUTPlayer>> players = data.getPlayers().entrySet();
		model.setRowCount(players.size() + 2);
		
		model.setValueAt("", 0, 0);
		model.setValueAt(Short.toString(data.getTeamScoreBlue()), 0, 1);
		model.setValueAt("Team Blau", 0, 2);
		
		model.setValueAt("", 1, 0);
		model.setValueAt(Short.toString(data.getTeamScoreRed()), 1, 1);
		model.setValueAt("Team Rot", 1, 2);
		
		int row = 2;
		for(Entry<Byte, PJUTPlayer> pe : players)
		{
			PJUTPlayer p = pe.getValue();
			model.setValueAt(p.getName(), row, 0);
			model.setValueAt(p.getScore(), row, 1);
			
			if(data.getTeam(p.getClientId()) == null)
			{
				model.setValueAt("", row, 2);
			}
			if(data.getTeam(p.getClientId()))
			{
				model.setValueAt("Blau", row, 2);
			}
			else
			{
				model.setValueAt("Rot", row, 2);
			}
			
			++row;
		}
		
		return players;
	}
}
