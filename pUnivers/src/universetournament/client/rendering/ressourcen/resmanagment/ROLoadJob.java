/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.resmanagment;

import java.util.List;
import universetournament.client.rendering.geometrie.packed.RenderObjekt;
import universetournament.client.rendering.geometrie.unpacked.ModelObjekt;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.client.util.io.obj.*;

/**
 *
 * @author dheinrich
 */
public class ROLoadJob implements LoadJob<RenderObjekt>
{
    private final StandartShader shader;
    private final ObjConfig ljob;
    private List<RenderObjekt> mcontainer;

    public ROLoadJob(StandartShader shader, ObjConfig ljob) {
        this.shader = shader;
        this.ljob = ljob;
    }

    public RenderObjekt load() {
        ObjObjektReader oor = new ObjObjektReader(shader);
        ModelObjekt mo = oor.loadObjekt(ljob);
        RenderObjekt ro = new RenderObjekt(shader, null, mo);
        synchronized (mcontainer) {
            for (RenderObjekt ro2 : mcontainer)
                ro2.setModels(ro.getModels());
        }
        return ro;
    }

    public void setConList(List<RenderObjekt> mcontainer) {
        synchronized (mcontainer) {
            this.mcontainer = mcontainer;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ROLoadJob other = (ROLoadJob) obj;
        if (this.shader != other.shader && (this.shader == null || 
                !(shader.getClass().isInstance(other.shader))))
            return false;
        if (this.ljob != other.ljob && (this.ljob == null || !this.ljob.equals(
                other.ljob)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.shader != null ? this.shader.hashCode() : 0);
        hash = 17 * hash + (this.ljob != null ? this.ljob.hashCode() : 0);
        return hash;
    }
}
