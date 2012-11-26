/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.packed;

import universetournament.client.rendering.Renderable;
import universetournament.client.rendering.geometrie.unpacked.*;
import universetournament.client.rendering.shaders.*;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.*;

/**
 * H�lt mehrere RenderModell Objekte
 * @author Daniel Heinrich
 */
public class RenderObjekt implements Renderable
{
    private RenderModel[] models;
    private StandartShader shader;
    private TransformationContainer transf;

    public RenderObjekt(StandartShader shader, TransformationContainer t,
                        ModelObjekt obj) {
        this(shader, t);

        Model[] mods = obj.getModels();
        models = new RenderModel[mods.length];
        for (int i = 0; i < models.length; i++)
            models[i] = new RenderModel(mods[i], shader);
    }

    public RenderObjekt(StandartShader shader, TransformationContainer t) {
        this.shader = shader;
        transf = t;
    }

    private RenderObjekt(RenderModel[] models, TransformationContainer t) {
        this(models[0].getShader(), t);
        this.models = new RenderModel[models.length];
        for(int i=0; i< models.length; ++i)
            this.models[i] = models[i].clone();
    }

    public RenderObjekt() {
    }

    public void setShader(StandartShader shader) {
        if (models == null)
            this.shader = shader;
    }

    public StandartShader getShader() {
        return shader;
    }

    public TransformationContainer getTransf() {
        return transf;
    }

    public void setTransf(TransformationContainer transf) {
        if (models == null)
            this.transf = transf;
    }

    public void setModels(RenderModel[] models) {
        this.models = models;
    }

    public void render() {
        prepareRendering();
        renderModels();
    }

    public void prepareRendering(){

        ModelMatrix m = transf.getModelMatrix();

        if(shader instanceof StandartShader)
            ((StandartShader)shader).setModelMatrix(m);

        if (shader instanceof LigthingShader) {
            Matrix mn = m.getNormalMatrix();
            ((LigthingShader) shader).setNormalMatrix(mn);
        }

        if (shader instanceof TerrainShader) {
            Matrix mn = m.getNormalMatrix();
            ((TerrainShader) shader).setNormalMatrix(mn);
        }
    }

    public void renderModels() {
        if (models == null)
            return;
        
        for (RenderModel rm : models)
            rm.render();
    }

    public RenderModel[] getModels() {
        return models;
    }

    @Override
    public RenderObjekt clone() {
        RenderObjekt ro = new RenderObjekt(models, transf);
        return ro;
    }
}
