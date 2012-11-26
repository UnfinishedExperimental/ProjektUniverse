/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.shaders;

import java.util.*;
import javax.media.opengl.GL;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.geometrie.vertexattributstuff.*;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.shaders.ShaderMaterials.ShaderMaterial;

/**
 *
 * @author dheinrich
 */
public class HudShader extends ShaderContainer{

    private Sampler diffuse;
    private AttributeCollection attributes;
    private GenericVAttribute position, texcoord;

    public HudShader() {
        diffuse = new Sampler(true, "diffuse_sampler", GL.GL_TEXTURE1);
        
        GenericVAttribute[] atts = new GenericVAttribute[collectAttributes().size()];
        collectAttributes().toArray(atts);
        attributes = new AttributeCollection(atts);
    }

    protected List<GenericVAttribute> collectAttributes(){
        position = new GenericVAttribute(-1, 2, AttType.FLOAT, false);
        texcoord = new GenericVAttribute(-1, 2, AttType.FLOAT, false);
        List<GenericVAttribute> atts = new ArrayList<GenericVAttribute>();
        atts.add(position);
        atts.add(texcoord);
        return atts;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {

        position.setIndex(sprog.getAttrLocation("in_position"));
        texcoord.setIndex(sprog.getAttrLocation("in_texcoord"));
//        instances = new Sampler(true, sprog.getUniformLocation("instance_sampler"),
//                                GL.GL_TEXTURE0);
        diffuse.setShader(sprog);
    }

    public Sampler getDiffuse() {
        return diffuse;
    }

    public ShaderMaterial getShaderMaterial(Material mat) {
        return null;
    }

    public final AttributeCollection getAttributs(){
        return attributes;
    }

    public GenericVAttribute getPositionAttr() {
        return position;
    }

    public GenericVAttribute getTexCoordAttr() {
        return texcoord;
    }

}
