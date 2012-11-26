/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.packed;

import javax.media.opengl.*;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;

/**
 *
 * @author dheinrich
 */
public class RObject3 extends RenderObjekt implements TransparentRO
{
    public RObject3(StandartShader shader, TransformationContainer t) {
        super(shader, t);
    }

    protected  RObject3() {
    }

    @Override
    public void render() {
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        super.prepareRendering();
        gl.glDepthMask(false);
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        gl.glCullFace(gl.GL_FRONT);
        super.renderModels();
        gl.glCullFace(gl.GL_BACK);
        super.renderModels();
        gl.glDisable(gl.GL_BLEND);
        gl.glDepthMask(true);
    }
}
