/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.shaders;

import java.util.List;
import de.fhhof.universe.client.rendering.ressourcen.wrapper.ShaderProgramm;
import javax.media.opengl.GL;
import de.fhhof.universe.client.rendering.shaders.ShaderMaterials.LigthingShaderMaterial;
import de.fhhof.universe.client.rendering.shaders.ShaderMaterials.ShaderMaterial;
import de.fhhof.universe.client.rendering.geometrie.unpacked.Material;
import de.fhhof.universe.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;
import de.fhhof.universe.client.rendering.geometrie.vertexattributstuff.attribute.NormalAttribute;
import de.fhhof.universe.shared.util.math.Matrix;

/**
 * Enth�t ein Shaderprogramm das f�r das benutzten als Beleuchtungs Shader konfiguriert wurde.
 * @author Daniel Heinrich
 */
public class LigthingShader extends StandartShader
{
    private int settings;
    private Sampler normal, specular, alpha;
    private int mat_n;
    private Matrix normal_mat;

    private GenericVAttribute att_nor;

    public LigthingShader() {
        mat_n = -1;
        settings = -1;

        specular = new Sampler(true, "specular_sampler", GL.GL_TEXTURE2);
        normal = new Sampler(true, "normal_sampler", GL.GL_TEXTURE3);
        alpha = new Sampler(true, "alpha_sampler", GL.GL_TEXTURE4);
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        mat_n = sprog.getUniformLocation("mat_n");
        att_nor.setIndex(sprog.getAttrLocation("in_normal"));

        settings = sprog.getUniformLocation("smapler_settings");
                                  
        specular.setShader(sprog);
        normal.setShader(sprog);
        alpha.setShader(sprog);

        setSettings();

        setNormalMatrix(normal_mat);
    }

    @Override
    protected List<GenericVAttribute> collectAttributes() {
        att_nor = new NormalAttribute(-1);
        List<GenericVAttribute> atts = super.collectAttributes();
        atts.add(att_nor);
        return atts;
    }

    public GenericVAttribute getNormalAttr() {
        return att_nor;
    }

    @Override
    public ShaderMaterial getShaderMaterial(Material mat) {
        return new LigthingShaderMaterial(mat, this);
    }

    /**
     * Setzt Shader Parameter, die bestimmen welche Sampler genutzt werden sollen.
     */
    public void setUsage(boolean deffuse, boolean specular, boolean normal,
                         boolean alpha) {
        getDiffuse().setActive(deffuse);
        getSpecular().setActive(specular);
        getNormal().setActive(normal);
        getAlpha().setActive(alpha);
        setSettings();
    }

    private void setSettings() {
        ShaderProgramm sp = getShaderprogramm();
        if (sp != null) {
            sp.use();
            sp.getGl().glUniform4f(settings,
                                   getDiffuse().isActive() ? 1 : 0,
                                   getSpecular().isActive() ? 1 : 0,
                                   getNormal().isActive() ? 1 : 0,
                                   getAlpha().isActive() ? 1 : 0);
        }
    }

    public void setNormalMatrix(Matrix mat) {
        if(mat == null)
            return;
        if (mat.getDimension() != 3)
            throw new IllegalArgumentException(
                    "Die Normalen Matrix muss eine 3x3 Matrix sein");
        normal_mat = mat;

        ShaderProgramm sp = getShaderprogramm();
        if (sp != null){
            sp.use();
            sp.getGl().glUniformMatrix3fv(mat_n, 1, false, mat.getFloatArray(), 0);
        }
    }

    public Sampler getAlpha() {
        return alpha;
    }

    public Sampler getNormal() {
        return normal;
    }

    public Sampler getSpecular() {
        return specular;
    }
}
