/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import universetournament.client.rendering.cameras.Camera;
import universetournament.client.rendering.geometrie.packed.TransparentRO;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.shared.util.math.ProjektionMatrix;
import universetournament.shared.util.math.ViewMatrix;

/**
 *
 * @author some
 */
public abstract class BasicScene implements GLEventListener
{
    protected static final Logger logger = Logger.getLogger(BasicScene.class.
            getName());

    private GL2GL3 gl;
    private Camera camera;
    private List<Renderable> robjekts = new ArrayList<Renderable>();
    private List<Renderable> transparent = new ArrayList<Renderable>();
    private Set<StandartShader> shaders = new HashSet<StandartShader>();
    private Stack<StandartShader> needprojektion = new Stack<StandartShader>();
    private ProjektionMatrix projektion;
    private long time, frametime;
    private float fps;

    public final void render() {
        if (projektion == null)
            return;

        long t = System.currentTimeMillis();

        while (!needprojektion.empty())
            needprojektion.pop().setProjektionMatrix(projektion);

        customRender();

        time = System.currentTimeMillis();
        frametime = time - t;
        fps = frametime / 1000f;
        if (frametime > 0)
            logger.log(Level.FINEST,
                       (robjekts.size() + transparent.size()) + " Objects to render took " + frametime + "ms");
    }

    protected abstract void customRender();

    protected ViewMatrix getCameraMatrix() {
        ViewMatrix view;
        if (camera != null)
            view = camera.getViewMatrix();
        else {
            view = new ViewMatrix();
            view.loadIdentity();
        }
        return view;
    }

    public void addRenderable(Renderable wo) {
        if (wo instanceof TransparentRO)
            transparent.add(0, wo);
        else
            robjekts.add(wo);

        addShader(wo.getShader());
    }

    protected void addShader(StandartShader sshader) {
        if (shaders.add(sshader))
            needprojektion.add(sshader);
    }

    public void removeRenderable(Renderable wo) {
        robjekts.remove(wo);
        transparent.remove(wo);
    }

    public void setProjektion(ProjektionMatrix projektion) {
        this.projektion = projektion;
        for (StandartShader ss : shaders)
            ss.setProjektionMatrix(projektion);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    protected List<Renderable> getRobjekts() {
        return robjekts;
    }

    protected Set<StandartShader> getShaders() {
        return shaders;
    }

    protected List<Renderable> getTransparent() {
        return transparent;
    }

    public float getFps() {
        return fps;
    }

    public long getFrametime() {
        return frametime;
    }

    protected GL2GL3 getGl() {
        return gl;
    }

    public void dispose(GLAutoDrawable drawable) {
        robjekts.clear();
        transparent.clear();
        shaders.clear();
    }

    public void display(GLAutoDrawable drawable) {
    }

    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
    }
    
    public void init(GLAutoDrawable drawable) {
        gl = drawable.getGL().getGL2GL3();
        logger.log(Level.INFO, "Scene Init");
    }
}
