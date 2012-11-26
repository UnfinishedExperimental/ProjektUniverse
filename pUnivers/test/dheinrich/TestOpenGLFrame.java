/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dheinrich;

import com.sun.opengl.util.Animator;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;
import universetournament.client.rendering.GLRenderer;
import universetournament.client.rendering.RenderFrame;
import universetournament.client.rendering.Scene;

/**
 *
 * @author some
 */
public class TestOpenGLFrame {
        private GLCanvas canvas;
	private RenderFrame rFrame;
	private final Animator animator;

        public TestOpenGLFrame()
	{

		// TODO: Fullscreen
		createWindow(100, 100);

		// Animator, der das Bild auffrischt
		animator = new Animator(canvas);
		animator.setRunAsFastAsPossible(false);
		animator.start();

//                Scene scene = Scene.getInstance();
//		canvas.addGLEventListener(new GLRenderer(scene));
//		canvas.addGLEventListener(scene);
	}

	private void createWindow(int xSize, int ySize)
	{
		// OpenGL-Informationen holen
		GLProfile profile = GLProfile.get(GLProfile.GL2GL3);
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setHardwareAccelerated(true);

		// Canvas ("Leinwand") und Frame
		canvas = new GLCanvas(capabilities);
		rFrame = new RenderFrame(canvas);
		rFrame.setSize(xSize, ySize);
		rFrame.setResizable(false);
		rFrame.setLocationRelativeTo(null);
		rFrame.setVisible(true);
		rFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Workaround, der bewirkt, dass der Canvas seine Größe anpasst.
		canvas.setMinimumSize(new Dimension());

		rFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				/*
				 * Zur Sicherheit in eigenem Thread ausführen, da das
				 * Swing-System blockieren könnte.
				 */
				new Thread(new Runnable()
				{
					public void run()
					{
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
	}
}
