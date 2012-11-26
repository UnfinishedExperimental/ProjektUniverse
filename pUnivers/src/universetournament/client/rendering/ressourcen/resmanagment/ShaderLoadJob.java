/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.resmanagment;

import java.util.List;
import universetournament.client.rendering.shaders.ShaderContainer;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.util.ShaderUtil;

/**
 *
 * @author dheinrich
 */
public class ShaderLoadJob implements LoadJob
{
    private final String vertex, frag;
    private List<ShaderContainer> scontainer;

    public ShaderLoadJob(String vertex, String frag) {
        this.vertex = vertex;
        this.frag = frag;
    }

    public String getFrag() {
        return frag;
    }

    public String getVertex() {
        return vertex;
    }

    public void setConList(List<ShaderContainer> scontainer) {
        synchronized (scontainer) {
            this.scontainer = scontainer;
        }
    }

    @Override
    public ShaderProgramm load() {
        ShaderProgramm sp = ShaderUtil.loadShader(vertex, frag);
        if (scontainer != null)
            synchronized (scontainer) {
                for (ShaderContainer sc : scontainer)
                    sc.setShaderProgramm(sp);
            }
        return sp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ShaderLoadJob other = (ShaderLoadJob) obj;
        if ((this.vertex == null) ? (other.vertex != null) : !this.vertex.equals(
                other.vertex))
            return false;
        if ((this.frag == null) ? (other.frag != null) : !this.frag.equals(
                other.frag))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.vertex != null ? this.vertex.hashCode() : 0);
        hash = 37 * hash + (this.frag != null ? this.frag.hashCode() : 0);
        return hash;
    }
}
