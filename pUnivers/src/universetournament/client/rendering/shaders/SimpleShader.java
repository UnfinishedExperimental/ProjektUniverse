/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders;

import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import java.util.ArrayList;
import java.util.List;
import javax.media.opengl.GL;
import universetournament.client.rendering.shaders.ShaderMaterials.ShaderMaterial;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeCollection;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;
import universetournament.client.rendering.geometrie.vertexattributstuff.attribute.PositionAttribute;
import universetournament.client.rendering.shaders.ShaderMaterials.SimpleShaderMaterial;

/**
 * Konfiguriert einen minimalen Shader.
 * @author Daniel Heinrich
 */
public class SimpleShader extends ShaderContainer
{
   
//    private Sampler instances;
    private Sampler diffuse;
    private AttributeCollection attributes;
    private GenericVAttribute position;

    public SimpleShader() {
        diffuse = new Sampler(true, "diffuse_sampler", GL.GL_TEXTURE1);
        
        List<GenericVAttribute> at = collectAttributes();
        GenericVAttribute[] atts = new GenericVAttribute[at.size()];
        at.toArray(atts);
        attributes = new AttributeCollection(atts);
    }

    protected List<GenericVAttribute> collectAttributes(){
        position = new PositionAttribute(-1);
        List<GenericVAttribute> atts = new ArrayList<GenericVAttribute>();
        atts.add(position);
        return atts;
    }

    @Override
    protected void iniShaderProgramm(ShaderProgramm sprog) {

        position.setIndex(sprog.getAttrLocation("in_position"));
//        instances = new Sampler(true, sprog.getUniformLocation("instance_sampler"),
//                                GL.GL_TEXTURE0);
        diffuse.setShader(sprog);
    }

    public Sampler getDiffuse() {
        return diffuse;
    }

    public ShaderMaterial getShaderMaterial(Material mat) {
        return new SimpleShaderMaterial(mat, this);
    }

    public final AttributeCollection getAttributs(){
        return attributes;
    }

    public GenericVAttribute getPositionAttr() {
        return position;
    }

//    public Sampler getInstances() {
//        return instances;
//    }
}
