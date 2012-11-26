/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.gui;

/**
 *
 * @author Daniela Geilert
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import universetournament.client.UTClient;
import universetournament.client.config.ClientConfig;
import universetournament.shared.util.io.UTXMLWriter;

public class Gui_Client extends JFrame
{
    private static final ResourceBundle internationlizedStrings =
                                        ResourceBundle.getBundle("resources/internationlizedStrings");
	Gui_Client window = this;
	
    // Attribute für das Fenster
	Container pane = getContentPane();
	EventHandler eh = new EventHandler ();
	JLabel nickname = new JLabel (internationlizedStrings.getString("nick"));
	JTextField tf_nickname = new JTextField();
	JLabel farbe = new JLabel (internationlizedStrings.getString("color"));
	JButton color = new JButton();	//Farbselektor
	JSeparator teiler = new JSeparator (SwingConstants.HORIZONTAL);
	JLabel schiffsklasse = new JLabel (internationlizedStrings.getString("select_ship_class"));
	JComboBox schiffsauswahl = new JComboBox();
	ShipBoxModel model;
	JSeparator teiler2 = new JSeparator (SwingConstants.HORIZONTAL);
	JLabel server = new JLabel (internationlizedStrings.getString("server"));
	JLabel ip = new JLabel (internationlizedStrings.getString("ip"));
	JTextField tf_ip = new JTextField();
	JLabel port = new JLabel (internationlizedStrings.getString("port"));
	JTextField tf_port = new JTextField ();
	JLabel passwort = new JLabel (internationlizedStrings.getString("password"));
	JPasswordField tf_passwort = new JPasswordField();
	JLabel pw = new JLabel (internationlizedStrings.getString("password"));
	JLabel tf_pw = new JLabel ("");
	JButton join = new JButton (internationlizedStrings.getString("join"));
	JButton schließen = new JButton (internationlizedStrings.getString("exit"));

	GridBagLayout gbl = new GridBagLayout ();
	GridBagConstraints gbc = new GridBagConstraints ();
	Font bigButton = new Font ("Sans Serif", Font.BOLD, 18);

	public Gui_Client ()
	{
		addWindowListener(new WindowCloser());
		setTitle ("UT Client");
		setSize (400,600);
		setLocation (400,200);
		setDefaultCloseOperation (WindowConstants.DO_NOTHING_ON_CLOSE);
		setResizable(false);

		pane.setLayout(gbl);
		teiler.setSize(500, 1);
		teiler2.setSize(500, 1);

		gbc = setGbcValues (0,0,2,1);
		pane.add(nickname,gbc);
		gbc = setGbcValues (2,0,3,1);
		pane.add(tf_nickname,gbc);
		gbc = setGbcValues (6,0,2,1);
		pane.add(farbe,gbc);
		color.setBackground(new Color (255,0,0));
		gbc = setGbcValues (8,0,2,1);
		pane.add(color,gbc);
		color.addActionListener(eh);

		gbc = setGbcValues (0,1,10,1);
		pane.add(teiler,gbc);

		gbc = setGbcValues (0,2,4,1);
		pane.add(schiffsklasse,gbc);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(schiffsauswahl, BorderLayout.CENTER);
		gbc = setGbcValues (0,3,10,2);
		pane.add(panel,gbc);

		gbc = setGbcValues (0,5,10,1);
		pane.add(teiler2,gbc);

		gbc = setGbcValues (0,6,3,1);
		pane.add(server,gbc);
		gbc = setGbcValues (0,7,2,1);
		pane.add(ip,gbc);
		gbc = setGbcValues (2,7,3,1);
		tf_ip.setSize(100, 25);
		pane.add(tf_ip,gbc);
		gbc = setGbcValues (5,7,2,1);
		pane.add(port,gbc);
		gbc = setGbcValues (7,7,3,1);
		pane.add(tf_port,gbc);
		gbc = setGbcValues (0,8,2,1);
		pane.add(passwort,gbc);
		gbc = setGbcValues (2,8,3,1);
		pane.add(tf_passwort,gbc);
		
		
		gbc = setGbcValues (0,10,3,1);
		pane.add(join,gbc);
		join.addActionListener(eh);
		gbc = setGbcValues (7,10,3,1);
		pane.add(schließen,gbc);
		schließen.addActionListener(eh);

		ClientConfig conf = ClientConfig.getInstance();
		tf_nickname.setText(conf.getPlayerName());
		color.setBackground(new Color(conf.getPlayerColor()));
		tf_ip.setText(conf.getHost());
		tf_port.setText(Integer.toString(conf.getPort()));
		tf_passwort.setText(conf.getPassword());
		
		model = new ShipBoxModel(conf.getShips());
		schiffsauswahl.setModel(model);
		
		pack();
	}

    private GridBagConstraints setGbcValues(int x, int y, int w, int h)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = w;
		gbc.gridheight = h;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets (5,5,5,5);
		return gbc;
	}

    class EventHandler implements ActionListener
    {
    	public void actionPerformed(ActionEvent e)
    	{
    		if (e.getSource().equals(color))
    		{
    			color.setBackground(JColorChooser.showDialog(pane,internationlizedStrings.getString("choose_color"),color.getBackground()));
    		}

    		else if (e.getSource().equals(join))
    		{
    			if(readData())
    			{
    				new UTXMLWriter().write(new File(UTClient.configPath),
    						ClientConfig.getInstance());
    				window.dispose();
    				new UTClient();
    			}
    		}

    		else if (e.getSource().equals(schließen))
    		{
    			// Button "Schließen" beendet Programm
    			int ret = JOptionPane.showConfirmDialog (pane,
															internationlizedStrings.getString("exit_realy?") ,
															internationlizedStrings.getString("exit??"),
															JOptionPane.YES_NO_OPTION,
															JOptionPane.QUESTION_MESSAGE);
				if (ret == JOptionPane.YES_OPTION)
				{
					pane.setVisible(false);
					System.exit(0);
				}
    		}

    	}
    	
    	private boolean readData()
    	{
    		boolean erfolg = true;
    		
    		ClientConfig conf = ClientConfig.getInstance();
    		if(tf_nickname.getText().length() > 0)
    		{
    			conf.setPlayerName(tf_nickname.getText());
    		}
    		else
    		{
    			erfolg = false;
    		}
    		
    		conf.setPlayerColor(color.getBackground().getRGB());
    		
    		if(tf_ip.getText().length() > 0)
    		{
    			conf.setHost(tf_ip.getText());
    		}
    		else
    		{
    			erfolg = false;
    		}
    		
    		try
    		{
    			conf.setPort(Integer.parseInt(tf_port.getText()));
    		}
    		catch(Exception e)
    		{
    			erfolg = false;
    		}
    		
    		conf.setPassword(new String(tf_passwort.getPassword()));
    		
    		conf.setShipConfig(model.getSelectedItem().getUid());
    		
    		return erfolg;
    	}
    }

    class WindowCloser extends WindowAdapter	// fragt, ob wirklich beendet werden soll
	{
		public void windowClosing(WindowEvent event)
		{
			int ret = JOptionPane.showConfirmDialog (event.getWindow(),
													internationlizedStrings.getString("exit_realy?") ,
													internationlizedStrings.getString("BEENDEN?"),
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