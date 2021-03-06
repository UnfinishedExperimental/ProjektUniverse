/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.rendering.filters;

import com.sun.opengl.util.texture.Texture;
import universetournament.client.rendering.ressourcen.resmanagment.*;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.client.rendering.geometrie.packed.*;
import universetournament.client.util.io.obj.ObjObjektReader;
import universetournament.shared.logic.entities.ingame.container.SimpleTransformation;

/**
 * Initialisiert ein einzelnes Quad das als Screen Quad genutzt werden kann
 * @author Daniel Heinrich
 */
public class ScreenQuad
{
    private RenderObjekt quad;
    private StandartShader squad;

    public ScreenQuad() {
        squad = new StandartShader();
        RessourcesLoader.getInstance().loadShader(squad, "ScreenQuad.vert",
                                                  "ScreenQuad.frag");
        RenderObjekt ro = new RenderObjekt(squad, new SimpleTransformation());
        RessourcesLoader.getInstance().loadRenderObjekt(ro, new ObjConfig(
                "Models/quad.obj", true, ObjObjektReader.Scale.HEIGHT, 2f));
                  
        quad = ro;
    }

    public void render() {
        quad.render();
    }

    public VBO getQuad() {
        return quad.getModels()[0].getVbo();
    }

    public void bindTexture(Texture tex) {
        squad.getDiffuse().bindTexture(tex);
    }

    public boolean isActive() {
        return true;
    }

    public void setActive(boolean active) {
    }
}
