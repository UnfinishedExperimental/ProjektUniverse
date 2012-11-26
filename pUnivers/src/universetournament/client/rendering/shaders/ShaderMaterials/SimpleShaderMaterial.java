/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders.ShaderMaterials;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.ressourcen.resmanagment.*;
import universetournament.client.rendering.shaders.SimpleShader;

/**
 *
 * @author dheinrich
 */
public class SimpleShaderMaterial extends ShaderMaterial
{
    private TextureContainer diffuse;

    public SimpleShaderMaterial(Material material, SimpleShader shader) {
        super(material, shader);

        if (material != null) {
            String path = material.getDiffuseTex();
            if (path != null) {
                TextureLoadJob tlj = new TextureLoadJob(path, GL.GL_LINEAR,
                                                        GL.GL_REPEAT);
                diffuse = RessourcesLoader.getInstance().loadTexture(tlj);
            }

        }
    }

    @Override
    public void prepareShader() {
        Material mat = getMaterial();
        if (mat != null)
            try {
                getShader().getDiffuse().bindTexture(getDiffuse());
            } catch (InstantiationException ex) {
            }
    }

    public Texture getDiffuse() throws InstantiationException {
        if(diffuse == null)
            throw new InstantiationException();
        return diffuse.getTexture();
    }
}
