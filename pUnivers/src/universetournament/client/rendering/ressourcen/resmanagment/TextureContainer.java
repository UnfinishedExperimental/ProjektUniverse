/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.ressourcen.resmanagment;

import com.sun.opengl.util.texture.Texture;

/**
 *
 * @author dheinrich
 */
public class TextureContainer {
    private Texture texture;

    public Texture getTexture() throws InstantiationException{
        if(texture == null)
                throw new InstantiationException();
        return texture;
    }

    void setTexture(Texture texture) {
        this.texture = texture;
    }
}
