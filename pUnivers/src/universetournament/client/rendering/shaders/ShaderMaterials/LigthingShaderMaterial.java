/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders.ShaderMaterials;

import javax.media.opengl.GL;
import universetournament.client.rendering.shaders.LigthingShader;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.ressourcen.resmanagment.*;
import universetournament.client.rendering.shaders.Sampler;

/**
 *
 * @author dheinrich
 */
public class LigthingShaderMaterial extends SimpleShaderMaterial
{
    private TextureContainer specular, alpha, normal;

    public LigthingShaderMaterial(Material material, LigthingShader shader) {
        super(material, shader);

        if (material != null) {
            String path = material.getSpecularTex();
            if (path != null) {
                TextureLoadJob tlj = new TextureLoadJob(path, GL.GL_LINEAR,
                                                        GL.GL_REPEAT);
                specular = RessourcesLoader.getInstance().loadTexture(tlj);
            }

            path = material.getAlphaTex();
            if (path != null) {
                TextureLoadJob tlj = new TextureLoadJob(path, GL.GL_LINEAR,
                                                        GL.GL_REPEAT);
                alpha = RessourcesLoader.getInstance().loadTexture(tlj);
            }

            path = material.getNormalTex();
            if (path != null) {
                TextureLoadJob tlj = new TextureLoadJob(path, GL.GL_LINEAR,
                                                        GL.GL_REPEAT);
                normal = RessourcesLoader.getInstance().loadTexture(tlj);
            }
        }
    }

    @Override
    public void prepareShader() {
        LigthingShader shader = (LigthingShader) getShader();

        boolean diff = false;
        try {
            getDiffuse();
            diff = true;
        } catch (InstantiationException ex) {
        }
        shader.setUsage(diff, specular != null, normal != null,
                        alpha != null);

        Material mat = getMaterial();

        if (mat != null){
            bindTexture(shader.getSpecular(), specular);
            bindTexture(shader.getAlpha(), alpha);
            bindTexture(shader.getNormal(), normal);
        }

        super.prepareShader();
    }

    private void bindTexture(Sampler s, TextureContainer tc) {
        if (tc == null)
            return;
        try {
            s.bindTexture(tc.getTexture());
        } catch (InstantiationException ex) {
        }
    }
}
