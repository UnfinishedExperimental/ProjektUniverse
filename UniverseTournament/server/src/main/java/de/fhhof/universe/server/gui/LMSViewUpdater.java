package de.fhhof.universe.server.gui;

import java.util.Set;
import java.util.Map.Entry;

import de.fhhof.universe.shared.logic.entities.PJUTPlayer;
import de.fhhof.universe.shared.logic.gamemode.UTLastManStandingData;

/**
 * Runnable, welches in einem fest definierten Intervall die Daten des
 * "last man standing" Spielmodus in das GUI einträgt.
 * 
 * @author Daniela Geilert
 *
 */
public class LMSViewUpdater extends ViewUpdater<UTLastManStandingData>
{
	/**
	 * Erstellt einen Updater, welcher die Daten aus dem übergebenen
	 * Datenobjekt in das angegebene GUI einträgt.
	 * Wirft eine NullPointerException, wenn mindestens ein Parameter null ist.
	 * 
	 * @param gui zu aktualisierendes GUI
	 * @param data zu verwendendes Datenobjekt.
	 */
	public LMSViewUpdater(Gui_Serverans gui, UTLastManStandingData data)
	{
		super(gui, data);
	}

	@Override
	protected Set<Entry<Byte, PJUTPlayer>> updatePlayers()
	{
		Set<Entry<Byte, PJUTPlayer>> players = data.getPlayers().entrySet();
		model.setRowCount(players.size());
		
		int row = 0;
		for(Entry<Byte, PJUTPlayer> pe : players)
		{
			PJUTPlayer p = pe.getValue();
			
			if(data.isDead(p.getClientId()))
			{
				model.setValueAt(p.getName() + " (tot)", row, 0);
			}
			else
			{
				model.setValueAt(p.getName(), row, 0);
			}
			
			model.setValueAt(p.getScore(), row, 1);
			model.setValueAt("", row, 2);
			++row;
		}
		
		return players;
	}
}
