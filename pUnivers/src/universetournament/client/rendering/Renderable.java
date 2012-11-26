/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering;

import universetournament.client.rendering.shaders.StandartShader;

/**
 *
 * @author dheinrich
 */
public interface Renderable {
    public void render();
    public StandartShader getShader();
}
