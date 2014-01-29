/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.client.rendering.shaders;

import de.fhhof.universe.client.rendering.ressourcen.wrapper.ShaderProgramm;
import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;

/**
 * Enth�t ein Shaderprogramm das f�r das benutzten als Noise-Filter konfiguriert wurde
 * @author Daniel Heinrich
 */
public class NoiseShader extends FilterShader{
    private Sampler noise;
    private Texture noise_tex;

    public NoiseShader(int texsize, Texture noisetex)
    {
        super(texsize);
        noise = new Sampler(true, "noise_sampler", GL.GL_TEXTURE1);
        noise_tex = noisetex;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        noise.setShader(sprog);
    }

    public Sampler getNoise(){
        return noise;
    }

    @Override
    public void use()
    {
        getNoise().bindTexture(noise_tex);
        super.use();
    }
}
