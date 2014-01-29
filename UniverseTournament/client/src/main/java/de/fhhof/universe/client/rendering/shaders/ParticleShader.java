/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.client.rendering.shaders;

import java.util.List;
import de.fhhof.universe.client.rendering.geometrie.vertexattributstuff.*;
import de.fhhof.universe.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 *
 * @author dheinrich
 */
public class ParticleShader extends StandartShader{

    private GenericVAttribute size, alpha;

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {
        super.iniShaderProgramm(sprog);
        size.setIndex(sprog.getAttrLocation("in_size"));
        alpha.setIndex(sprog.getAttrLocation("in_alpha"));
    }

    @Override
    protected List<GenericVAttribute> collectAttributes() {
        size = new GenericVAttribute(-1, 1, AttType.FLOAT, false);
        alpha = new GenericVAttribute(-1, 1, AttType.FLOAT, false);
        List<GenericVAttribute> atts = super.collectAttributes();
        atts.add(size);
        atts.add(alpha);
        return atts;
    }

    public GenericVAttribute getAlphaAttr() {
        return alpha;
    }

    public GenericVAttribute getSizeAttr() {
        return size;
    }

}
