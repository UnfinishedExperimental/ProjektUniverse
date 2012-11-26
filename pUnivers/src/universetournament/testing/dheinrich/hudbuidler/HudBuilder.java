/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.hudbuidler;

import java.awt.event.*;
import java.io.IOException;
import java.util.logging.*;
import javax.swing.JFrame;
import universetournament.client.core.ClientWindow;

/**
 *
 * @author dheinrich
 */
public class HudBuilder
{
    public static void main(String[] args) {
        try {
            final FileHandler handler = new FileHandler("errors.txt");
            handler.setLevel(Level.WARNING);

            Logger logger =
                   Logger.getLogger("universetournament");
            logger.addHandler(handler);

            ClientWindow cw = new ClientWindow(800, 600, false);
            BuilderFrame lf = new BuilderFrame();
            lf.setVisible(true);
            lf.pack();
            lf.setLocationRelativeTo(null);
            lf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            lf.addWindowListener(new WindowAdapter()
            {
                @Override
                public void windowClosed(WindowEvent e) {
                    handler.flush();
                }
            });
        } catch (IOException ex) {
        } catch (SecurityException ex) {
        }
    }
}
