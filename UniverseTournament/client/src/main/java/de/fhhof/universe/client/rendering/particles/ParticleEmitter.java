/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.particles;

import darwin.geometrie.unpacked.Material;
import darwin.resourcehandling.texture.TextureUtil;
import javax.media.opengl.*;
import de.fhhof.universe.client.rendering.shaders.ParticleShader;

/**
 *
 * @author dheinrich
 */
public class ParticleEmitter extends ParticleBuffer
{
    private SimpleShaderMaterial sm;
    private String tex;

    public ParticleEmitter(float fadetime, ParticleShader pa, String texture) {
        super(fadetime, pa);
        tex = texture;
    }

    @Override
    public void render() {
        GL gl = GLContext.getCurrentGL().getGL2GL3();
        if (sm == null) {
            try {
                Material mat =
                         new Material("as");
                mat.setDiffuseTex(tex);
                sm =
                (SimpleShaderMaterial) getShader().getShaderMaterial(mat);
                TextureUtil.setTexturePara(sm.getDiffuse(),
                                           GL.GL_LINEAR,
                                           GL.GL_CLAMP_TO_EDGE);
                gl.glEnable(GL2.GL_VERTEX_PROGRAM_POINT_SIZE);
            } catch (InstantiationException ex) {
                sm = null;
            }
            return;
        }
        sm.prepareShader();
        gl.glDepthMask(false);
        gl.glEnable(GL2.GL_POINT_SPRITE);
        gl.glEnable(gl.GL_BLEND);
        gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
        super.render();
        gl.glDisable(gl.GL_BLEND);
        gl.glDisable(GL2.GL_POINT_SPRITE);
        gl.glDepthMask(true);
    }
}
