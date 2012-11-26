package universetournament.client.input.commands;

import javax.swing.JFrame;

import com.sun.opengl.util.Animator;

import universetournament.client.input.PJUTCommand;

/**
 * Kommando zum Schließen eines OpenGL-Fensters und anschließender Beendigung.
 * Ruft dispose()-Methode beim JFrame auf, stoppt den Animator und schließt
 * das Progamm in einem neuen Thread.
 * 
 * @author Florian Holzschuher
 *
 */
public class CloseCommand implements PJUTCommand
{
	private final JFrame frame;
	private final Animator anim;
	
	/**
	 * Erstellt ein Kommando für das Schließen des übergebenen Fensters,
	 * das Beenden des übergebenen Animators und dem Stoppen der VM.
	 * Ist ein Argument null wird für dieses nichts getan.
	 * 
	 * @param frame zu schließendes Fenster
	 */
	public CloseCommand(JFrame frame, Animator anim)
	{
		this.frame = frame;
		this.anim = anim;
	}
	
	@Override
	public void execute(float timeDiff)
	{
		new Thread(new Runnable()
        {
            public void run()
            {
            	if(frame != null);
            	{
            		frame.dispose();
            	}
            	if(anim != null)
            	{
            		anim.stop();
            	}
                System.exit(0);
            }
        }).start();
	}
}
