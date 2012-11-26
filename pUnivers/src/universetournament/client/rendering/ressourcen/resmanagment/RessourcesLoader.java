/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.resmanagment;

import com.sun.opengl.util.texture.Texture;
import java.util.*;
import universetournament.client.rendering.geometrie.packed.RenderObjekt;
import universetournament.client.rendering.geometrie.unpacked.ModelObjekt;
import universetournament.client.rendering.shaders.ShaderContainer;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.util.io.obj.OBJObjektReader;

/**
 *
 * @author dheinrich
 */
public class RessourcesLoader
{
    private static final RessourcesLoader instance = new RessourcesLoader();

    public static RessourcesLoader getInstance() {
        return instance;
    }
    private final Queue<LoadJob> jobs = new LinkedList<LoadJob>();
    private final Queue<LoadJob> oldjobs = new LinkedList<LoadJob>();

    private final HashMap<LoadJob, Object> ressourcen =
                                           new HashMap<LoadJob, Object>();
    //shader stuff
    private final HashMap<ShaderLoadJob, List<ShaderContainer>> scontainer =
                                                                new HashMap<ShaderLoadJob, List<ShaderContainer>>();
    private Stack<ThreadSafeShaderLoading> shadertoset = new Stack<ThreadSafeShaderLoading>();
    
    //texture stuff
    private final HashMap<TextureLoadJob, TextureContainer> tcontainer =
                                                                new HashMap<TextureLoadJob, TextureContainer>();

    private RessourcesLoader() {
    }

    synchronized public void loadShader(ShaderContainer scont, String vertex, String frag) {
        ShaderLoadJob sljob = new ShaderLoadJob(vertex, frag);
        ShaderProgramm sprog = (ShaderProgramm) ressourcen.get(sljob);
        if (sprog == null) {
            List<ShaderContainer> l = scontainer.get(sljob);
            if (l == null) {
                l = new LinkedList<ShaderContainer>();
                scontainer.put(sljob, l);
                sljob.setConList(l);
                jobs.add(sljob);
            }
            l.add(scont);
        } else{
            scontainer.get(sljob).add(scont);
            shadertoset.add(new ThreadSafeShaderLoading(scont, sprog));
        }

    }

    synchronized public void loadRenderObjekt(RenderObjekt ro, ObjConfig ljob){
        loadRenderObjekt(ro, ljob, false);
    }

    synchronized public void loadRenderObjekt(RenderObjekt ro, ObjConfig ljob, boolean newro){
        ROLoadJob rol = new ROLoadJob(ro.getShader(), ljob);
        RenderObjekt res = (RenderObjekt) ressourcen.get(rol);
        if (res == null) {
            OBJObjektReader oor = new OBJObjektReader(ro.getShader());
            ModelObjekt mo = oor.loadObjekt(ljob);
            RenderObjekt r = new RenderObjekt(ro.getShader(), null, mo);
            ressourcen.put(rol, r);
            if(newro)
                r = r.clone();
            ro.setModels(r.getModels());
        } else{
            if(newro)
                res = res.clone();
            ro.setModels(res.getModels());
        }
    }

    synchronized public TextureContainer loadTexture(TextureLoadJob ljob){
        Texture res = (Texture) ressourcen.get(ljob);
        if (res == null) {
            TextureContainer tc = tcontainer.get(ljob);
            if (tc == null) {
                tc = new TextureContainer();
                tcontainer.put(ljob, tc);
                ljob.setCon(tc);
                jobs.add(ljob);
            }
            return tc;
        } else{
            return tcontainer.get(ljob);
        }
    }

    synchronized public void workAllJobs() {
        while (!jobs.isEmpty())
            loadJob(jobs.remove());
        while(!shadertoset.empty())
            shadertoset.pop().load();
    }

    synchronized public void workJob() {
        if (!jobs.isEmpty())
            loadJob(jobs.remove());
    }

    private void loadJob(LoadJob j) {
        ressourcen.put(j, j.load());
        oldjobs.add(j);
    }

    public int jobsLeft() {
        return jobs.size();
    }

    synchronized public void reloadRessources(){
        while(!oldjobs.isEmpty()){
            oldjobs.remove().load();
        }
    }
}
