/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.filters;


import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import darwin.renderer.GraphicContext;
import darwin.renderer.geometrie.factorys.ScreenQuad;
import darwin.renderer.geometrie.packed.RenderMesh;
import darwin.renderer.opengl.FrameBuffer.FrameBufferObject;
import darwin.renderer.shader.Sampler;
import darwin.renderer.shader.Shader;
import darwin.resourcehandling.dependencies.annotation.InjectBundle;
import darwin.resourcehandling.shader.ShaderLoader;
import darwin.util.misc.SaveClosable;

import javax.media.opengl.GL;
import java.lang.annotation.RetentionPolicy;

/**
 * Enth�lt eine Liste von Filtern die auf ein Frame Buffer Objekt angewendet werden k�nnen.
 *
 * @author Daniel Heinrich
 */
public class SSFilterList {
    private final Shader[] shaders;
    private final RenderMesh[] meshes;
    private final FrameBufferObject utilfbo;
    private final GraphicContext gc;
    private final float texsize;
    private final RenderMesh copyMesh;
    @InjectBundle(files = {"ScreenQuad.vert", "ScreenQuad.frag"}, prefix = ShaderLoader.SHADER_PATH_PREFIX)
    private Shader copy;

    /**
     * ERstellt eine Screen Space Filter Liste aus FilterSahdern.
     *
     * @param texsize Die Texure gr��e des Tempor�r genutzten FBOs.
     *                TexSize muss eine l�nge von 2^n haben.
     * @param shader  Eine Liste von Filter Shadern die wenn die FilterListe angewandt wird
     *                der reihe nach auf ein FBO angewandt werden.
     */
    @AssistedInject
    public SSFilterList(@Filter FrameBufferObject utilfbo, GraphicContext gc, ScreenQuad squad,
                        @Assisted int texsize, @Assisted Shader... shader) {
        this.shaders = shader;
        this.utilfbo = utilfbo;
        this.gc = gc;
        this.texsize = texsize;

        meshes = new RenderMesh[shader.length];
        for (int i = 0; i < meshes.length; ++i) {
            meshes[i] = squad.buildRenderable(shader[i]);
        }

        copyMesh = squad.buildRenderable(copy);
    }

    /**
     * Wendet die Liste an Filtern auf ein FBO an.
     * Das Ergebniss auf das angegebene FBO gerendert.
     *
     * @param inout Das FBO auf das das ergebniss gerendert wird.
     *              Au�erdem muss das FBO als Color_Attachment_0 eine Textur gebunden haben.
     */
    public void applyFilters(FrameBufferObject inout, ScreenQuad squad) {
        FrameBufferObject[] fbos = new FrameBufferObject[]{inout, utilfbo};

        try (SaveClosable sc = fbos[0].use()) {
            if (applyFilters(fbos, squad) == 1) {
                gc.getGL().glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

                Sampler diff = copy.getSampler("diffuse_sampler").get();
                diff.bindTexture(fbos[1].getColorAttachmentTexture(0));

                fbos[0].use();
                copyMesh.render();
            }
        }
    }

    private int applyFilters(FrameBufferObject[] fbos, ScreenQuad squad) {
        int ind = 0;

        for (int i = 0; i < shaders.length; ++i) {
            Shader s = shaders[i];
            if (!s.isInitialized())
                continue;
            FrameBufferObject last = fbos[ind];
            ind = 1 - ind;
            FrameBufferObject acc = fbos[ind];
            try (SaveClosable sc = acc.use()) {
                gc.getGL().glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

                Sampler diff = s.getSampler("diffuse_sampler").get();
                diff.bindTexture(last.getColorAttachmentTexture(0));

                s.getUniform("texstep").get().setData(acc.getWidth() / texsize);

                meshes[i].render();
            }
        }

        return ind;
    }

    @java.lang.annotation.Documented
    @java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
    @javax.inject.Qualifier
    public @interface Filter {
    }

    public interface SSFilterListFactory {
        public SSFilterList create(int texsize, Shader... shader);
    }
}
