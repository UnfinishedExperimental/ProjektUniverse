/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.shaders;

import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.shaders.ShaderMaterials.*;

/**
 *
 * @author dheinrich
 */
public class ShieldShader extends LigthingShader{
    private int uni_offset, uni_color;
    private float offset;
    private float[] color;

    public ShieldShader() {
        uni_offset = -1;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        uni_offset = sprog.getUniformLocation("offset");
        uni_color = sprog.getUniformLocation("color");
        setOffset(offset);
        setColor(color);
    }

    public void setOffset(float offset) {
        this.offset = offset;
        ShaderProgramm sp = getShaderprogramm();
        if(uni_offset>=0 && sp != null){
            sp.use();
            sp.getGl().glUniform1f(uni_offset, offset);
        }
    }

    public void setColor(float[] color) {
        this.color = color;
        ShaderProgramm sp = getShaderprogramm();
        if(uni_color>=0 && sp != null && color != null){
            sp.use();
            sp.getGl().glUniform3f(uni_color, color[0], color[1], color[2]);
        }
    }

    @Override
    public ShaderMaterial getShaderMaterial(Material mat) {
        return new ShieldMateriall(mat, this);
    }

}
