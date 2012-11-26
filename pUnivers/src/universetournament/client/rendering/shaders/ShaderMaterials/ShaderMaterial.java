/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.shaders.ShaderMaterials;

import universetournament.client.rendering.shaders.SimpleShader;
import universetournament.client.rendering.geometrie.unpacked.Material;


/**
 *
 * @author dheinrich
 */
public abstract class ShaderMaterial {
    private Material material;
    private SimpleShader shader;

    public ShaderMaterial(Material material, SimpleShader shader) {
        this.material = material;
        this.shader = shader;
    }

    public abstract void prepareShader();

    public Material getMaterial() {
        return material;
    }

    public SimpleShader getShader() {
        return shader;
    }

    
}
