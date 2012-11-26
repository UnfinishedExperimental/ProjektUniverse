/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

/**
 * Ein einzelnes Frame das ein GLCanvas beherbergt
 * Dieses wird direkt dem Frame hinzugefuegt
 * da so der GLContex beim veraendern der Fenster Groesse erhalten bleibt.
 * @author Daniel Heinrich
 */
public class RenderFrame extends JFrame{

    private GLCanvas canvas;

    public RenderFrame(GLCanvas canvas)
    {
        this.canvas = canvas;
        add(canvas);
        pack();
    }
}
