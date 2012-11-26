/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.server.gui;

/**
 *
 * @author Daniela Geilert
 */

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import universetournament.server.UTServer;
import universetournament.server.config.ServerConfig;
import universetournament.shared.logic.gamemode.GameModeType;
import universetournament.shared.util.io.UTXMLWriter;

public class Gui_Serverconf extends JFrame
{
	Gui_Serverconf window = this;
	
	// Attribute für das Fenster
	Container container = getContentPane();
	JPanel pane = new JPanel();
	EventHandler eh = new EventHandler ();
	JLabel spielmodus = new JLabel ("Spielmodus:");

	//Dropdown-Menü
	String[] elements = {"Death Match", "Team Death Match", "Last Man Standing", "Plünderung"};
	JComboBox box_modus = new JComboBox(elements);

	JLabel siegpunkte = new JLabel ("Sieg-Limit (Punkte):");
	JTextField tf_siegpunkte = new JTextField();
	JLabel siegzeit = new JLabel ("Zeit-Limit in Sekunden:");
	JTextField tf_siegzeit = new JTextField();
	JLabel maxanzahl = new JLabel ("Maximale Spieler:");
	JTextField tf_maxanzahl = new JTextField();
	JLabel port = new JLabel ("Port:");
	JTextField tf_port = new JTextField();
	JLabel passwort = new JLabel ("Passwort:");
	JPasswordField tf_passwort = new JPasswordField();
	JButton servstart = new JButton ("Server starten");

	GridBagLayout gbl = new GridBagLayout ();
	GridBagConstraints gbc = new GridBagConstraints();
	GridLayout gl = new GridLayout (7,2,10,10);

	public Gui_Serverconf ()
	{
		addWindowListener(new WindowCloser());
		setTitle ("Server-Konfiguration");
		setSize (500,400);
		setLocation (400,200);
		setDefaultCloseOperation (WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);

		container.setLayout(gbl);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets (10,10,10,10);
		container.add(pane,gbc);

		pane.setLayout(gl);
		pane.add(spielmodus);
		pane.add(box_modus);
		pane.add(siegpunkte);
		pane.add(tf_siegpunkte);
		pane.add(siegzeit);
		pane.add(tf_siegzeit);
		pane.add(maxanzahl);
		pane.add(tf_maxanzahl);
		pane.add(port);
		pane.add(tf_port);
		pane.add(passwort);
		pane.add(tf_passwort);
		pane.add(servstart);
		servstart.addActionListener(eh);
		
		ServerConfig conf = ServerConfig.getInstance();
		tf_siegzeit.setText(Double.toString(conf.getTimeLimit()));
		tf_siegpunkte.setText(Short.toString(conf.getScoreLimit()));
		tf_maxanzahl.setText(Byte.toString(conf.getPlayerLimit()));
		tf_port.setText(Integer.toString(conf.getPort()));
		tf_passwort.setText(conf.getPassword());
		
		pack();
	}

    class EventHandler implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if (e.getSource().equals(servstart))
    		{
    			if(readData())
    			{
    				new UTXMLWriter().write(new File(UTServer.configPath),
    						ServerConfig.getInstance());
    				window.dispose();
    				new UTServer();
    			}
    		}
    	}
    	
    	private boolean readData()
    	{
    		boolean erfolg = true;
    		ServerConfig conf = ServerConfig.getInstance();
    		
    		try
    		{
    			conf.setPort(Integer.parseInt(tf_port.getText()));
    			conf.setTimeLimit(Double.parseDouble(tf_siegzeit.getText()));
    			conf.setScoreLimit(Short.parseShort(tf_siegpunkte.getText()));
    			conf.setPlayerLimit(Byte.parseByte(tf_maxanzahl.getText()));
    			conf.setPassword(new String(tf_passwort.getPassword()));
    			
    			switch(box_modus.getSelectedIndex())
    			{
    				case 0:
    					conf.setGameType(GameModeType.DEATH_MATCH);
    					break;
    					
    				case 1:
    					conf.setGameType(GameModeType.TEAM_DEATH_MATCH);
    					break;
    					
    				case 2:
    					conf.setGameType(GameModeType.LAST_MAN_STANDING);
    					break;
    					
    				case 3:
    					conf.setGameType(GameModeType.LOOTING);
    					break;
    					
    				default:
    					erfolg = false;
    			}
    		}
    		catch(Exception e)
    		{
    			erfolg = false;
    		}
    		
    		return erfolg;
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