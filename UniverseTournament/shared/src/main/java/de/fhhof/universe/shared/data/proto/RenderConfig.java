/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.shared.data.proto;

/**
 *
 * @author Daniel Heinrich
 */
public class RenderConfig {
    private final String shader_vertex, shader_frag;
    private final String objconf;

    public RenderConfig() {
        shader_vertex = "unnamed.vert";
        shader_frag = "unnamed.frag";
        objconf = "Models/unnamed.obj";
    }

    public String getObjConf() {
        return objconf;
    }

    public String getShader_frag() {
        return shader_frag;
    }

    public String getShader_vertex() {
        return shader_vertex;
    }
}
