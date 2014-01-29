/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.shaders;

import de.fhhof.universe.client.rendering.ressourcen.wrapper.ShaderProgramm;
import de.fhhof.universe.shared.util.math.Matrix;

/**
 *
 * @author dheinrich
 */
public class SkyboxShader extends SimpleShader
{
    private Matrix viewdir;
    private float ratio;
    private int uni_ratio, uni_viewdir;

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        uni_ratio = sprog.getUniformLocation("ratio");
        uni_viewdir = sprog.getUniformLocation("mat_view");
        setRatio(ratio);
        setViewRotMat(viewdir);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
        ShaderProgramm sp = getShaderprogramm();
        if (sp != null) {
            sp.use();
            sp.getGl().glUniform1f(uni_ratio, ratio);
        }
    }

    public void setViewRotMat(Matrix viewdir) {
        this.viewdir = viewdir;
        ShaderProgramm sp = getShaderprogramm();
        if (sp != null && viewdir!=null) {
            sp.use();
            sp.getGl().glUniformMatrix3fv(uni_viewdir, 1, false,
                                          viewdir.getFloatArray(), 0);
        }
    }
}
