/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering;

import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import universetournament.client.rendering.geometrie.packed.RenderObjekt;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.geometrie.unpacked.Model;
import universetournament.client.rendering.geometrie.unpacked.ModelObjekt;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.client.rendering.shaders.TerrainShader;
import universetournament.shared.logic.entities.ingame.container.SimpleTransformation;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.Vec3;
import universetournament.shared.util.math.ViewMatrix;
import universetournament.testing.dheinrich.rendering.procedual.Grid;

/**
 *
 * @author some
 */
public class TerrainScene extends BasicScene
{
    private TerrainShader shader;

    public TerrainScene() {

        shader = new TerrainShader();
        RessourcesLoader.getInstance().loadShader(shader, "Terrain.vert",
                                                  "Terrain.frag");

        shader.setLight_ambient(0.1f, 0.1f, 0.1f, 1f);
        shader.setLight_diffuse(1, 1, 1f, 1);
        shader.setLight_specular(1, 1, 1, 1);
    }

    @Override
    protected void customRender() {
        GL2GL3 gl = getGl();

        ViewMatrix view = getCameraMatrix();
        for (StandartShader ss : getShaders())
            ss.setViewMatrix(view);

        setLigthDir(new Vec3(0, 1, 1), view);
        for (Renderable re : getRobjekts())
            re.render();

//        gl.glDepthMask(false);
//        setLigthDir(new Vec3(0, 1, -1), view);
//        shader.setLight_diffuse(0.5f, 1, 0.5f, 1);
//        gl.glEnable(gl.GL_BLEND);
//        gl.glBlendFunc(gl.GL_ONE, gl.GL_ONE);
//        gl.glBlendEquation(gl.GL_FUNC_ADD);
//        for (Renderable re : getRobjekts())
//            re.render();
//        gl.glDisable(gl.GL_BLEND);
//        gl.glDepthMask(true);

        for (Renderable re : getTransparent())
            re.render();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);

        Grid terrain = new Grid(shader, 512, 256);
        Material mat = new Material("test");
        mat.setAmbient(new float[]{0.1f, 0.1f, 0.1f, 1});
        mat.setSepcular(new float[]{1, 1, 1, 1});
        mat.setSpecular_exponent(1.5f);
        mat.setDiffuse(new float[]{1, 1, 1, 1});
        mat.setBumbTex("height.dds");
        mat.setDiffuseTex("height.dds");
        Model mo = new Model(terrain, mat);

        final TransformationContainer trans = new SimpleTransformation();
        trans.scale(new Vec3(10, 10, 5));
        trans.rotateEuler(new Vec3(-90, 0, 0));
        RenderObjekt normal = new RenderObjekt(shader, trans, new ModelObjekt(
                new Model[]{mo}, "test"));
        this.addRenderable(normal);
    }

    private void setLigthDir(Vec3 pos, ViewMatrix view) {
        //TODO: Licht vektor irgendwo abspeichern, nicht hardcoded(hier der 0,1,1 vektor)
        Vec3 light = new Vec3(view.getMinor(3, 3).mult(pos));
        light.normalize(light);
        shader.setLight_dir(light);

        Vec3 halfvector = new Vec3(0, 0, 1);
        halfvector.add(light, halfvector);
        halfvector.normalize(halfvector);

        shader.setHalfVector(halfvector);
    }
}
