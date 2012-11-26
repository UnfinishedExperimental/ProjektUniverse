/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.filters;

import java.util.*;
import javax.media.opengl.*;
import universetournament.client.rendering.ressourcen.wrapper.FrameBufferObject;
import universetournament.client.rendering.shaders.FilterShader;
import universetournament.client.rendering.util.FboUtil;

/**
 * Enth�lt eine Liste von Filtern die auf ein Frame Buffer Objekt angewendet werden k�nnen.
 * @author Daniel Heinrich
 */
public class SSFilterList
{

    private FilterShader[] shaders;
    private final GL2GL3 gl;
    private FrameBufferObject utilfbo;

    private static final Map<GL, FrameBufferObject> map = new HashMap<GL, FrameBufferObject>();

    /**
     * ERstellt eine Screen Space Filter Liste aus FilterSahdern.
     * @param gl
     * Der genutzte GL Context.
     * @param texsize
     * Die Texure gr��e des Tempor�r genutzten FBOs.
     * TexSize muss eine l�nge von 2^n haben.
     * @param shader
     * Eine Liste von Filter Shadern die wenn die FilterListe angewandt wird 
     * der reihe nach auf ein FBO angewandt werden.
     */
    public SSFilterList(int texsize, FilterShader... shader)
    {
        this.shaders = shader;
        this.gl = GLContext.getCurrentGL().getGL2GL3();
        utilfbo = getFBO(texsize);
    }

    /**
     * Wendet die Liste an Filtern auf ein FBO an.
     * Das Ergebniss auf das angegebene FBO gerendert.
     * @param inout
     * Das FBO auf das das ergebniss gerendert wird.
     * Au�erdem muss das FBO als Color_Attachment_0 eine Textur gebunden haben.
     */
    public void applyFilters(FrameBufferObject inout, ScreenQuad squad){
        FrameBufferObject[] fbos = new FrameBufferObject[]{inout, utilfbo};

        fbos[0].pushViewPort();

        if(applyFilters(fbos, squad) == 1){
            squad.bindTexture(fbos[1].getColor_AttachmentTexture(0));
            fbos[0].bind();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            squad.render();
        }

        fbos[0].popViewPort();
    }

    private int applyFilters(FrameBufferObject[] fbos, ScreenQuad squad)
    {
        int ind = 0;

        for (FilterShader s : shaders) {
            if(!s.isActive())
                continue;
            FrameBufferObject last = fbos[ind];
            ind = 1 - ind;
            FrameBufferObject acc = fbos[ind];
            acc.bind();
            gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
            s.getDiffuse().bindTexture(last.getColor_AttachmentTexture(0));
            s.use();
            s.setTSize(acc.getWidth());
            squad.getQuad().render(s);
        }

        return ind;
    }

    private FrameBufferObject getFBO(int texsize)
    {
        FrameBufferObject q = map.get(gl);
        if (q == null) {
            q = FboUtil.newStandartFBO(texsize);
            map.put(gl, q);
        }
        return q;
    }
}
