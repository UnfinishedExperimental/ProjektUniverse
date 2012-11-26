/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import universetournament.client.rendering.ressourcen.wrapper.FrameBufferObject;
import universetournament.client.rendering.ressourcen.wrapper.RenderBuffer;
import universetournament.client.rendering.util.TextureUtil;

/**
 *
 * @author some
 */
public class DeferredScene extends BasicScene implements GLEventListener
{
    private FrameBufferObject fbo;

    public DeferredScene() {
    }

    public void customRender() {
        GL2GL3 gl = getGl();

        fbo.bind();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        int[] dbs = new int[]{GL2GL3.GL_COLOR_ATTACHMENT0,
                              GL2GL3.GL_COLOR_ATTACHMENT1,
                              GL2GL3.GL_COLOR_ATTACHMENT2};

        gl.glDrawBuffers(3, dbs, 0);

        for (Renderable re : getRobjekts())
            re.render();

        fbo.disable();
//
//        gl.glDrawBuffer(GL2GL3.GL_FRONT);
//        squad.bindTexture(fbo.getColor_AttachmentTexture(2));
//        squad.render();

//        Vec3f light = new Vec3f(view.getMinor(3, 3).
//                mult(new Vec3f(0, 1, 1)));
//        light.normalize(light);
////        phong.setLight_dir(light);
//
//        Vec3f halfvector = new Vec3f(0, 0, 1);
//        halfvector.add(light, halfvector);
//        halfvector.normalize(halfvector);

//        phong.setHalfVector(halfvector);
    }

   

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);

        fbo = new FrameBufferObject();
        int width = 800;
        int heigth = 600;

        Texture mat = TextureUtil.newTexture(GL.GL_RGBA8, width, heigth, 0,
                                             GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,
                                             false);
//        Texture depth = TextureUtil.newTexture(GL.GL_DEPTH_COMPONENT24, width, heigth, 0,
//                                 GL2GL3.GL_RED, GL.GL_UNSIGNED_BYTE,
//                                 false);
        RenderBuffer depth = new RenderBuffer(width, heigth,
                                              GL2GL3.GL_DEPTH_COMPONENT24);
        Texture normalSpecEx = TextureUtil.newTexture(GL2GL3.GL_RGB16F, width,
                                                      heigth, 0,
                                                      GL2GL3.GL_RGBA,
                                                      GL.GL_UNSIGNED_BYTE,
                                                      false);

        Texture compostion = TextureUtil.newTexture(GL.GL_RGB8, width, heigth, 0,
                                                    GL.GL_RGB,
                                                    GL.GL_UNSIGNED_BYTE,
                                                    false);

        fbo.setColor_Attachment(0, mat);
        fbo.setColor_Attachment(1, normalSpecEx);
        fbo.setColor_Attachment(2, compostion);
        fbo.setDepth_Attachment(depth);
    }
    
}
