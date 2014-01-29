package de.fhhof.universe.server.gui;

import java.util.Map.Entry;
import java.util.Set;

import javax.swing.table.DefaultTableModel;

import de.fhhof.universe.shared.logic.entities.PJUTPlayer;
import de.fhhof.universe.shared.logic.gamemode.UTGameModeData;

/**
 * Runnable, welches in einem fest definierten Intervall die Daten des
 * Spielmodus in das GUI einträgt.
 * 
 * @author Daniela Geilert
 *
 * @param <T> Klasse der überwachten Daten
 */
public class ViewUpdater<T extends UTGameModeData> implements Runnable
{
	private static final long REF_TIME = 500;
	private boolean active;
	
	/**
	 * Ansicht, die aufgesfrischt wird
	 */
	protected final Gui_Serverans gui;
	
	/**
	 * Model, welches manipuliert wird
	 */
	protected final DefaultTableModel model;
	
	/**
	 * Daten, die angezeigt werden sollen
	 */
	protected final T data;
	
	/**
	 * Erstellt einen Updater, welcher die Daten aus dem übergebenen
	 * Datenobjekt in das angegebene GUI einträgt.
	 * Wirft eine NullPointerException, wenn mindestens ein Parameter null ist.
	 * 
	 * @param gui zu aktualisierendes GUI
	 * @param data zu verwendendes Datenobjekt.
	 */
	public ViewUpdater(Gui_Serverans gui, T data)
	{
		if(gui == null)
		{
			throw new NullPointerException("GUI war null");
		}
		
		if(data == null)
		{
			throw new NullPointerException("Daten waren null");
		}
		
		active = true;
		this.gui = gui;
		model = gui.getSpielerDaten();
		this.data = data;
	}
	
	/**
	 * Trägt die Daten aus dem Spielmodus in das GUI ein.
	 */
	public void update()
	{
		Set<Entry<Byte, PJUTPlayer>> players = updatePlayers();
		
		gui.setTimeLeft((int)data.getTimeLeft());
		gui.setPlayerCount(players.size());
	}
	
	/**
	 * Frischt die Spieler-Tabelle auf.
	 * Sollte von den Unterklassen entsprechend überschrieben werden.
	 * 
	 * @return Spieler-Liste
	 */
	protected Set<Entry<Byte, PJUTPlayer>> updatePlayers()
	{
		Set<Entry<Byte, PJUTPlayer>> players = data.getPlayers().entrySet();
		model.setRowCount(players.size());
		
		int row = 0;
		for(Entry<Byte, PJUTPlayer> pe : players)
		{
			PJUTPlayer p = pe.getValue();
			model.setValueAt(p.getName(), row, 0);
			model.setValueAt(p.getScore(), row, 1);
			model.setValueAt("", row, 2);
			++row;
		}
		
		return players;
	}
	
	/**
	 * @param active Schleifen-Parameter (ob sie laufen soll)
	 */
	public void setActive(boolean active)
	{
		this.active = active;
	}

	@Override
	public void run()
	{
		while(active)
		{
			update();
			
			try
			{
				Thread.sleep(REF_TIME);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				return;
			}
		}
	}
}
