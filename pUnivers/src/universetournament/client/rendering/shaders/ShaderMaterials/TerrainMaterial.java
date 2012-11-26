/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders.ShaderMaterials;

import com.sun.opengl.util.texture.Texture;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.ressourcen.resmanagment.HeightMapLoadJob;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.ressourcen.resmanagment.TextureContainer;
import universetournament.client.rendering.ressourcen.resmanagment.TextureLoadJob;
import universetournament.client.rendering.shaders.SimpleShader;
import universetournament.client.rendering.shaders.TerrainShader;

/**
 *
 * @author some
 */
public class TerrainMaterial extends SimpleShaderMaterial
{
    private TextureContainer heightmap;

    public TerrainMaterial(Material material, SimpleShader shader) {
        super(material, shader);

        if (material != null) {
            String path = material.getBumbTex();
            if (path != null) {

                TextureLoadJob t = new HeightMapLoadJob(path);
                heightmap = RessourcesLoader.getInstance().loadTexture(t);
            }
        }
    }

    @Override
    public void prepareShader() {
        try {
            super.prepareShader();
            Material mat = getMaterial();
            if (mat != null)
                ((TerrainShader) getShader()).getHeightMap().
                        bindTexture(getHeight());
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }
    }

    public Texture getHeight() throws InstantiationException {
        if(heightmap == null)
            throw new InstantiationException();
        return heightmap.getTexture();
    }

}
