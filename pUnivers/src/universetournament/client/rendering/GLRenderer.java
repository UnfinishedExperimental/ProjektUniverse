package universetournament.client.rendering;

import java.util.logging.*;
import javax.media.opengl.*;
import universetournament.shared.util.math.ProjektionMatrix;

/**
 * GLRenderer ist die Klasse die fuer das Render einer Szene zustaendig ist.
 * Sie setzt die Startwerte fuer den GLContext.
 * Ausserdem ist sie zustaendig die Projektion Matrix zu aktualisieren
 * und an die Szene weiterzugeben.
 * @author Daniel Heinrich
 */
public class GLRenderer implements GLEventListener
{
    private static final Logger logger = Logger.getLogger(GLRenderer.class.getName());

    private ProjektionMatrix projektion;
    private BasicScene scene;

    public GLRenderer(BasicScene scene)
    {
        this.scene = scene;
    }

    public void init(GLAutoDrawable drawable)
    {
        // Use debug pipeline, all OpenGL error codes will be automatically
        // converted to GLExceptions as soon as they appear
        drawable.setGL(new DebugGL2(drawable.getGL().getGL2()));

        GL gl = drawable.getGL();
        logger.log(Level.INFO, "INIT GL IS: " + gl.getClass().getName());
        
        projektion = new ProjektionMatrix(50, 0.1f, 1000f);

        gl.glEnable(gl.GL_DEPTH_TEST);
        gl.glEnable(gl.GL_CULL_FACE);
//        gl.glDisable(gl.GL_CULL_FACE);
        gl.glCullFace(gl.GL_BACK);
        gl.glDepthFunc(gl.GL_LEQUAL);
        // Enable VSync
//        gl.setSwapInterval(1);
        // Setup the drawing area and shading mode
        gl.glClearColor(0.3f, 0.4f, 0.3f, 1f);
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
    {
        GL gl = drawable.getGL();

        if (height <= 0) // avoid a divide by zero error!
            height = 1;
        final float ratio = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);

        projektion.perspective(ratio);
        scene.setProjektion(projektion);
    }

    public void display(GLAutoDrawable drawable)
    {
        GL gl = drawable.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        scene.render();
    }

    public void dispose(GLAutoDrawable arg0)
    {
    }
}

