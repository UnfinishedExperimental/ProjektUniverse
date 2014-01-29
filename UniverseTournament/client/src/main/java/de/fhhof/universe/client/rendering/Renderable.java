/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.client.rendering;

import de.fhhof.universe.client.rendering.shaders.StandartShader;

/**
 *
 * @author dheinrich
 */
public interface Renderable {
    public void render();
    public StandartShader getShader();
}
