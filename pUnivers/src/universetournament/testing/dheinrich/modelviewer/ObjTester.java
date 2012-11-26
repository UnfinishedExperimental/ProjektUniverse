/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.modelviewer;

import java.awt.event.*;
import java.io.IOException;
import java.util.logging.*;
import javax.swing.JFrame;
import universetournament.client.core.ClientWindow;

/**
 *
 * @author dheinrich
 */
public class ObjTester
{
    public static void main(String[] args) {
        try {
            final FileHandler handler = new FileHandler("errors.txt");
            handler.setLevel(Level.WARNING);

            Logger logger =
                   Logger.getLogger("universetournament");
            logger.addHandler(handler);

            ClientWindow cw = new ClientWindow(800, 600, false);
            LoadFrame lf = new LoadFrame();
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
