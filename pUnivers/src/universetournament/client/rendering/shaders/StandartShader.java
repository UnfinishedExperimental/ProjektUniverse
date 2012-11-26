/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders;

import java.util.List;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.shaders.ShaderMaterials.ShaderMaterial;
import universetournament.client.rendering.shaders.ShaderMaterials.SimpleShaderMaterial;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;
import universetournament.client.rendering.geometrie.vertexattributstuff.attribute.TexCoordAttribute;
import universetournament.shared.util.math.Matrix4;

/**
 * Konfiguriert ein Shader f�r normalen gebrauch.
 * @author Daniel Heinrich
 */
public class StandartShader extends SimpleShader
{
    private int mat_m, mat_v, mat_p;
    private Matrix4 model, view, projektion;
    private GenericVAttribute texcoord;

    public StandartShader() {
        mat_m = -1;
        mat_v = -1;
        mat_p = -1;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        texcoord.setIndex(sprog.getAttrLocation("in_texcoord"));

        mat_m = sprog.getUniformLocation("mat_m");
        mat_v = sprog.getUniformLocation("mat_v");
        mat_p = sprog.getUniformLocation("mat_p");

        setModelMatrix(model);
        setProjektionMatrix(projektion);
        setViewMatrix(view);
    }

    @Override
    protected List<GenericVAttribute> collectAttributes() {
        texcoord = new TexCoordAttribute(-1);
        List<GenericVAttribute> atts = super.collectAttributes();
        atts.add(texcoord);
        return atts;
    }

    public GenericVAttribute getTexCoordAttr() {
        return texcoord;
    }

    @Override
    public ShaderMaterial getShaderMaterial(Material mat) {
        return new SimpleShaderMaterial(mat, this);
    }

    public void setModelMatrix(Matrix4 mat) {
        model = mat;
        sendMatrix(mat_m, mat);
    }

    public void setViewMatrix(Matrix4 mat) {
        view = mat;
        sendMatrix(mat_v, mat);
    }

    public void setProjektionMatrix(Matrix4 mat) {
        projektion = mat;
        sendMatrix(mat_p, mat);
    }

    private void sendMatrix(int matid, Matrix4 mat) {
        ShaderProgramm sp = getShaderprogramm();
        if (sp != null && mat != null){
            sp.use();
            sp.getGl().glUniformMatrix4fv(matid, 1, false, mat.getFloatArray(), 0);
        }
    }
}
