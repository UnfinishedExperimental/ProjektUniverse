/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.shaders.ShaderMaterials;

import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.shaders.ShieldShader;

/**
 *
 * @author dheinrich
 */
public class ShieldMateriall extends ShaderMaterial
{
    public ShieldMateriall(Material material, ShieldShader shader) {
        super(material, shader);
    }

    @Override
    public void prepareShader() {
        ShieldShader shader = (ShieldShader) getShader();
        shader.getShaderprogramm().use();
        shader.setColor(getMaterial().getDiffuse());
    }

}
