/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.cameras;

import darwin.util.math.composits.ViewMatrix;


/**
 *
 * @author dheinrich
 */
public interface Camera
{
    public ViewMatrix getViewMatrix();
}
