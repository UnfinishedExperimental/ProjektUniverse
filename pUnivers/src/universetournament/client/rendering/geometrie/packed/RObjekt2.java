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
public class RObjekt2 extends RenderObjekt implements TransparentRO
{

    public RObjekt2(StandartShader shader, TransformationContainer t) {
        super(shader, t);
    }

    public RObjekt2() {
    }

    @Override
    public void render() {
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        super.render();
        gl.glDisable(gl.GL_BLEND);
    }


}
