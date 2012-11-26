/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.terrain;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import universetournament.client.core.ClientWindow;

/**
 *
 * @author some
 */
public class TerrainTest
{
    public TerrainTest() {
        try {
            final FileHandler handler = new FileHandler("errors.txt");
            handler.setLevel(Level.WARNING);

            Logger logger =
                   Logger.getLogger("universetournament");
            logger.addHandler(handler);

            ClientWindow cw = new ClientWindow(800, 600, false);
            ini();
//            EditorFrame lf = new EditorFrame();
            cw.getFrame().addWindowListener(new WindowAdapter()
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

    private void ini() {

       
    }

    public static void main(String[] args) {
        new TerrainTest();
    }
}


