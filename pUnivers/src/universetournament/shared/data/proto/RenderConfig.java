/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.data.proto;

import universetournament.client.rendering.geometrie.packed.RenderObjekt;
import universetournament.client.rendering.ressourcen.resmanagment.ObjConfig;
import universetournament.client.rendering.shaders.PhongShader;
import universetournament.client.rendering.shaders.StandartShader;

/**
 *
 * @author Daniel Heinrich
 */
public class RenderConfig {
    private final String shader_vertex, shader_frag;
    private final ObjConfig objconf;
    private final Class<? extends RenderObjekt> roclass;
    private final Class<? extends StandartShader> shaderclass;

    public RenderConfig() {
        shader_vertex = "unnamed.vert";
        shader_frag = "unnamed.frag";
        objconf = new ObjConfig("Models/unnamed.obj");
        roclass = RenderObjekt.class;
        shaderclass = PhongShader.class;
    }

    public ObjConfig getObjConf() {
        return objconf;
    }

    public String getShader_frag() {
        return shader_frag;
    }

    public String getShader_vertex() {
        return shader_vertex;
    }

    public Class<? extends RenderObjekt> getROClass() {
        return roclass;
    }

    public Class<? extends StandartShader> getShaderClass() {
        return shaderclass;
    }
}
