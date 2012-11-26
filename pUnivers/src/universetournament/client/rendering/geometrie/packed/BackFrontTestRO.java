/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.geometrie.packed;

import javax.media.opengl.*;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.shaders.*;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;

/**
 *
 * @author dheinrich
 */
public class BackFrontTestRO extends RenderObjekt{
    private PhongShader phong
            ;
    public BackFrontTestRO(StandartShader shader, TransformationContainer t) {
        super(shader, t);
        phong = new PhongShader();
        RessourcesLoader.getInstance().loadShader(phong, "Phong.vert", "Phong.frag");
    }

    public BackFrontTestRO() {
        phong = new PhongShader();
        RessourcesLoader.getInstance().loadShader(phong, "Phong.vert", "Phong.frag");
    }

    @Override
    public void render() {
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        
        super.prepareRendering();

        gl.glCullFace(gl.GL_FRONT);
        phong.setLight_diffuse(1,0.6f,0.6f,1);
        super.renderModels();

        gl.glCullFace(gl.GL_BACK);
        phong.setLight_diffuse(0.6f,1,0.6f,1);
        super.renderModels();
        
        phong.setLight_diffuse(1, 1, 0.8f, 1);
    }
}
