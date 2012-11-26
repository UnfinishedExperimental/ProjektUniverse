/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.*;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 *
 * @author dheinrich
 */
public class Sampler
{
    private boolean active;
    private int uniform_pos, texture_unit;
    private String uniname;
    private GL2GL3 gl;

    public Sampler(boolean active, String uniform, int texture_unit) {
        setActive(active);
        this.uniform_pos = -1;
        uniname = uniform;
        this.texture_unit = texture_unit;
    }

    protected void setActive(boolean active) {
        this.active = active;
    }

    public void bindTexture(Texture tex) {
        if (!isActive() || tex == null)
            return;
        gl.glActiveTexture(texture_unit);
        tex.bind();
    }

    public boolean isActive() {
        return uniform_pos != -1 && active;
    }

    public void setShader(ShaderProgramm s) {
        uniform_pos = s.getUniformLocation(uniname);
        s.use();
        gl = s.getGl();
        gl.glUniform1i(uniform_pos, texture_unit - GL.GL_TEXTURE0);
    }
}
