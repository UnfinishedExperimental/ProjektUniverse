/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.shaders.ShaderMaterials;

import universetournament.client.rendering.shaders.PhongShader;
import universetournament.client.rendering.geometrie.unpacked.Material;


/**
 *
 * @author dheinrich
 */
public class PhongShaderMaterial extends LigthingShaderMaterial{

    public PhongShaderMaterial(Material material, PhongShader shader) {
        super(material, shader);
    }


    @Override
    public void prepareShader() {
        super.prepareShader();
        
        PhongShader shader = (PhongShader) getShader();
        Material mat = getMaterial();

        if (mat != null) {
            shader.setMat_ambient(mat.getAmbient());
            shader.setMat_diffuse(mat.getDiffuse());
            shader.setMat_specular(mat.getSepcular());

            shader.setMat_spec_exp(mat.getSpecular_exponent());
        }
    }

}
