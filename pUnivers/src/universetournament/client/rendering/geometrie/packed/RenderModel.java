/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.packed;

import universetournament.client.rendering.shaders.ShaderMaterials.ShaderMaterial;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.geometrie.unpacked.Model;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeExeption;
import universetournament.client.rendering.shaders.StandartShader;

/**
 * H�lt alle Render relevanten Attribute eines 3D Modelles.
 * Rendert ein Modell nach diesen Attributen
 * @author Daniel Heinrich
 */
public class RenderModel
{
    private Material material;
    private VBO vbo;
    private StandartShader shader;
    private ShaderMaterial smaterial;

    private RenderModel(VBO vbo, StandartShader shader, Material mat) {
        this.vbo = vbo;
        material = mat;
        setShader(shader);
    }

    public RenderModel(Model model, StandartShader shader) {
        material = model.getMat();

        try {
            vbo = new VBO(model.getMesh());
        } catch (AttributeExeption ex) {
            ex.printStackTrace();
        }

        setShader(shader);
    }

    public void render() {
        smaterial.prepareShader();
        vbo.render(shader);
    }

    public Material getMaterial() {
        return material;
    }

    public StandartShader getShader() {
        return shader;
    }

    private void setShader(StandartShader shader) {
        this.shader = shader;
        smaterial = shader.getShaderMaterial(getMaterial());
    }

    public VBO getVbo() {
        return vbo;
    }

    @Override
    public RenderModel clone(){
        return new RenderModel(vbo, getShader(), getMaterial().clone());
    }
}
