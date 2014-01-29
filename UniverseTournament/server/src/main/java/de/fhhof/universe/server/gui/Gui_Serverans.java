/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.server.gui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import de.fhhof.universe.server.core.ServerMainController;

/**
 *
 * @author Daniela Geilert
 */

public class Gui_Serverans extends JFrame
{
	// Attribute für das Fenster
	Container pane = getContentPane();
	EventHandler eh = new EventHandler ();
	JLabel spielzeit = new JLabel ("Restspielzeit:");
	JLabel tf_spielzeit = new JLabel("0:00");
	JLabel spieleranz = new JLabel ("Spieleranzahl:");
	JLabel tf_spieleranz = new JLabel ("0");

	//Tabelle der Spieler/Teams
	String[] columnNames = {"Spieler", "Punkte", "Team"};
	String[][] keineSpieler = {{"kein Spieler", "", ""}};
	DefaultTableModel spieler_daten = new DefaultTableModel(keineSpieler, columnNames);
	JTable table = new JTable(spieler_daten);
	JScrollPane scrollPane = new JScrollPane(table);

	JButton rauswerfen = new JButton ("Spieler rauswerfen");
	JButton beenden = new JButton ("Beenden");

	GridBagLayout gbl = new GridBagLayout ();
	GridBagConstraints gbc = new GridBagConstraints();

	public Gui_Serverans ()
	{
		addWindowListener(new WindowCloser());
		setTitle ("Server-Ansicht");
		setSize (500,400);
		setLocation (400,100);
		setDefaultCloseOperation (WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		pane.setLayout(gbl);
		gbc = setGbcValues (0,0,1,1);
		pane.add(spielzeit,gbc);
		gbc = setGbcValues (1,0,1,1);
		pane.add(tf_spielzeit,gbc);
		gbc = setGbcValues (0,1,1,1);
		pane.add(spieleranz,gbc);
		gbc = setGbcValues (1,1,1,1);
		pane.add(tf_spieleranz,gbc);
		gbc = setGbcValues (0,2,3,1);
		pane.add(scrollPane,gbc);
		gbc = setGbcValues (0,3,1,1);
		pane.add(rauswerfen,gbc);
		rauswerfen.addActionListener(eh);
		gbc = setGbcValues (1,3,1,1);
		pane.add(beenden,gbc);
		beenden.addActionListener(eh);
		pack();
	}
	
	public void setTimeLeft(int seconds)
	{
		String stunde = Integer.toString(seconds/3600);
		String minute = Integer.toString((seconds%3600)/60);
		String sekunde = Integer.toString(seconds%60);
		
		if(stunde.length() < 2)
		{
			stunde = '0' + stunde;
		}
		
		if(minute.length() < 2)
		{
			minute = '0' + minute;
		}
		
		if(sekunde.length() < 2)
		{
			sekunde = '0' + sekunde;
		}
		
		tf_spielzeit.setText(stunde + ':' + minute + ':'
				+ sekunde);
	}
	
	public void setPlayerCount(int number)
	{
		tf_spieleranz.setText(Integer.toString(number));
	}
	
	public DefaultTableModel getSpielerDaten()
	{
		return spieler_daten;
	}

    private GridBagConstraints setGbcValues(int x, int y, int w, int h)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets (5,5,5,5);
		return gbc;
	}

    class EventHandler implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if (e.getSource().equals(rauswerfen))
    		{
    			int row = table.getSelectedRow();
    			
    			if(row >= 0)
    			{
    				ServerMainController.getInstance().
    					getGameModeController().kick(row);
    			}
    		}

    		if (e.getSource().equals(beenden))
    		{
    			// Button "Schließen" beendet Programm
    			int ret = JOptionPane.showConfirmDialog (pane,
															"Wirklich beenden?" ,
															"Beenden?",
															JOptionPane.YES_NO_OPTION,
															JOptionPane.QUESTION_MESSAGE);
				if (ret == JOptionPane.YES_OPTION)
				{
					pane.setVisible(false);
					System.exit(0);
				}
    		}
    	}
    }

    class WindowCloser extends WindowAdapter	// fragt, ob wirklich beendet werden soll
	{
		public void windowClosing(WindowEvent event)
		{
			int ret = JOptionPane.showConfirmDialog (event.getWindow(),
									"Wirklich beenden?" ,
									"Beenden?",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
			if (ret == JOptionPane.YES_OPTION)
			{
				event.getWindow().setVisible(false);
				System.exit(0);
			}
		}
	}
}