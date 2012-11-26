/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering;

import com.sun.opengl.util.texture.Texture;
import java.io.IOException;
import java.util.Random;
import java.util.logging.*;
import javax.media.opengl.*;
import universetournament.client.rendering.filters.*;
import universetournament.client.rendering.hud.Hud;
import universetournament.client.rendering.particles.*;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.ressourcen.wrapper.*;
import universetournament.client.rendering.shaders.*;
import universetournament.client.rendering.util.*;
import universetournament.shared.util.math.*;

/**
 * Die Szenen Klasse initialisiert alle f�r sich wichtigen render elemente
 * und h�lt eine List in die WorldObjekts hinterlegt werden die die Szene
 * dann rendert.
 * @author Daniel Heinrich
 */
public class Scene extends BasicScene
{
    //Basics
    private SkyboxShader skybox;
    private Texture sky;
    private Hud hud;
    private PhongShader phong;
    private ParticleEmitter smokeemitter;
    //Filter Stuff
    private ScreenQuad squad;
    private final int texsize = 1024;
    private FrameBufferObject fbo;
    private SSFilterList filter;
    private FilterShader radialblur;
    private boolean usefilter = true;
    static int count = 0;
    private int uniblur = -1;
    private boolean noiseInitialized = false;

    private static class Instance
    {
        private static final BasicScene scene = new Scene();
    }

    public static BasicScene getInstance() {
        return Instance.scene;
    }

    private Scene() {

        radialblur = new FilterShader(texsize);
        RessourcesLoader.getInstance().loadShader(radialblur, "ScreenQuad.vert",
                                                  "RadialBlur.frag");
        setRadialBlurMul(1);
        radialblur.setActive(false);

        skybox = new SkyboxShader();
        RessourcesLoader.getInstance().loadShader(skybox, "Skybox.vert",
                                                  "Skybox.frag");


        phong = new PhongShader();
        RessourcesLoader.getInstance().loadShader(phong, "Phong.vert",
                                                  "Phong.frag");
        phong.setLight_ambient(0.1f, 0.1f, 0.1f, 1f);
        phong.setLight_diffuse(1, 1, 0.8f, 1);
        phong.setLight_specular(1, 1, 1, 1);

        DeferredParticleShader dpshader = new DeferredParticleShader();
        Matrix4 mm = new Matrix4();
        mm.loadIdentity();
        dpshader.setModelMatrix(mm);
        addShader(dpshader);
        RessourcesLoader.getInstance().loadShader(dpshader,
                                                  "Particle.vert",
                                                  "DefferedParticle.frag");
        smokeemitter = new ParticleEmitter(2, dpshader, "DeferredParticle.dds");
        addRenderable(smokeemitter);
    }

    private void iniFilter() {
        fbo = FboUtil.newStandartFBO(texsize);

        squad = new ScreenQuad();

        filter = new SSFilterList(texsize, radialblur);
    }

    private void iniNoiseTable() {
        ShaderProgramm sp = phong.getShaderprogramm();
        if (sp == null)
            return;
        Random rnd = new Random(System.currentTimeMillis());
        float[] noise_table = new float[4 * 66];
        for (int i = 0; i < 32; ++i) {
            float[] gp = genRandPermutGrad(rnd);
            copyTo(noise_table, gp, i * 4);
        }

        for (int i = 0; i < 32; ++i)
            for (int j = 0; j < 4; ++j)
                noise_table[(i + 32) * 4 + j] = noise_table[i * 4 + j];

        copyTo(noise_table, genRandPermutGrad(rnd), 64 * 4);
        copyTo(noise_table, genRandPermutGrad(rnd), 65 * 4);

        sp.use();
        int uninoise = sp.getUniformLocation("permu_grad");
        sp.getGl().glUniform4fv(uninoise, 66, noise_table, 0);

        noiseInitialized = true;
    }

    private void copyTo(float[] to, float[] from, int offset) {
        for (int j = 0; j < from.length; ++j)
            to[offset + j] = from[j];
    }

    private float[] genRandPermutGrad(Random rnd) {
        Vec3 grad = new Vec3(rnd.nextDouble() * 2 - 1,
                               rnd.nextDouble() * 2 - 1,
                               rnd.nextDouble() * 2 - 1);
        grad.normalize(grad);
        float permu = rnd.nextFloat() * 700 - 1;
        double[] vec = grad.getCoords();
        return new float[]{(float)vec[0], (float)vec[1], (float)vec[2], permu};
    }

    /**
     * Rendert die alle WorldObjekts der Szene wahlweise mit Filtern oder ohne
     */
    public void customRender() {
        GL2GL3 gl = getGl();

        if (!noiseInitialized)
            iniNoiseTable();

        if (usefilter) {
            fbo.bind();
            fbo.pushViewPort();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        }

        ViewMatrix view = getCameraMatrix();
        for (StandartShader ss : getShaders())
            ss.setViewMatrix(view);

        skybox.setViewRotMat(view.getMinor(3, 3).inverse());
        skybox.getDiffuse().bindTexture(sky);
        gl.glDepthMask(false);
        squad.getQuad().render(skybox);
        gl.glDepthMask(true);

        setLigthDir(new Vec3(0, 1, 1), view);
        phong.setLight_diffuse(0.65f, 0.5f, 1.0f, 1);
        phong.getEnv().bindTexture(sky);
        for (Renderable re : getRobjekts())
            re.render();

//        gl.glDepthMask(false);
        setLigthDir(new Vec3(0, 1, -1), view);
        phong.setLight_diffuse(0.5f, 1, 0.5f, 1);
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_ONE, gl.GL_ONE);
        gl.glBlendEquation(gl.GL_FUNC_ADD);
        for (Renderable re : getRobjekts())
            re.render();
        gl.glDisable(gl.GL_BLEND);
//        gl.glDepthMask(true);

        for (Renderable re : getTransparent())
            re.render();

        if (usefilter) {
            fbo.popViewPort();
            filter.applyFilters(fbo, squad);

            fbo.disable();
            squad.bindTexture(fbo.getColor_AttachmentTexture(0));
            squad.render();
        }

        if (hud != null)
            hud.render();


//        try {
//            TextureIO.write(fbo.getColor_AttachmentTexture(0),
//                            new File(String.format("screens/%04d.jpg", count++)));
//            //        }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (GLException ex) {
//            ex.printStackTrace();
//        }


        smokeemitter.advance(getFps(), view);
    }

    private void setLigthDir(Vec3 pos, ViewMatrix view) {
        //TODO: Licht vektor irgendwo abspeichern, nicht hardcoded(hier der 0,1,1 vektor)
        Vec3 light = new Vec3(view.getMinor(3, 3).mult(pos));
        light.normalize(light);
        phong.setLight_dir(light);

        Vec3 halfvector = new Vec3(0, 0, 1);
        halfvector.add(light, halfvector);
        halfvector.normalize(halfvector);

        phong.setHalfVector(halfvector);
    }

    @Override
    public void setProjektion(ProjektionMatrix projektion) {
        super.setProjektion(projektion);
        skybox.setRatio((float)projektion.getAspect());
    }

    public FilterShader getRadialblur() {
        return radialblur;
    }

    public void setRadialBlurMul(float mul) {
        ShaderProgramm sp = radialblur.getShaderprogramm();
        if (sp == null)
            return;

        if (uniblur < 0)
            uniblur = sp.getUniformLocation("sampleDistMul");
        mul *= 3;
        if (mul > 1)
            mul = 1;
        sp.use();
        getGl().glUniform1f(uniblur, mul);
    }

    public void emmitSmokeBall(Vec3 pos, float size, float speed, float life) {
        size *= size * size;
        int c = (int) (size * 20);
        for (int i = 0; i < c; i++) {
            Vec3 vel = new Vec3((float) (Math.random() - 0.5) * 2f,
                                  (float) (Math.random() - 0.5) * 2f,
                                  (float) (Math.random() - 0.5) * 2f);
            vel.mult(speed, vel);
            Vec3 p = new Vec3((float) (Math.random() - 0.5),
                                (float) (Math.random() - 0.5),
                                (float) (Math.random() - 0.5));
            p.add(pos, p);
            smokeemitter.addParticle(new Particle(p, vel, size * 600, life));
        }
    }

    public void setUsefilter(boolean usefilter) {
        this.usefilter = usefilter;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        iniFilter();
        try {
            sky = TextureUtil.loadCubeMap("Textures/skyboxes/SKY");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "SkyboxTexture konnte nicht geladen werden.\n"
                    + ex.getLocalizedMessage());
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
//        fbo.delete();
    }

    public Hud getHud() {
        return hud;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }
}
