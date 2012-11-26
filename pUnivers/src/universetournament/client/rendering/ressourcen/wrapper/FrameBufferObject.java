/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.wrapper;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

/**
 *
 * @author Daniel Heinrich
 */
public class FrameBufferObject
{

    private int[] fb = new int[1];
    private int width, height;
    private Object[] color_attachment = new Object[GL2GL3.GL_MAX_COLOR_ATTACHMENTS];
    private Object depth_attachment, stencil_attachment;

    int[] viewport = new int[4];

    /**
     * erstellt ein neues FrameBufferObjekt.
     * @param gl
     * der GL Context in dem das FBO erstellt werden soll.
     */
    public FrameBufferObject()
    {
        GL gl = GLContext.getCurrentGL();
        gl.glGenFramebuffers(1, fb, 0);
    }

    // <editor-fold defaultstate="collapsed" desc="Color-Attachment Setter/Getter">
    /**
     * Binded ein Renderbuffer auf ein bestimmtes Color Attachment des FBO.
     * <br>
     * @param nummber
     * <br>
     * Nummer des Color Attachments(0 bis GL_MAX_COLOR_ATTACHMENTS_EXT-1).
     * <br>
     * @param rb
     * <br>
     * Der Renderbuffer der gebunden werden soll.
     */
    public void setColor_Attachment(int nummber, RenderBuffer rb)
    {
        checkSize(rb.getWidth(), rb.getHeight());

        if (nummber < GL2GL3.GL_MAX_COLOR_ATTACHMENTS) {
            bind();
            GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
            gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
                                            GL.GL_COLOR_ATTACHMENT0 + nummber,
                                            GL.GL_RENDERBUFFER, rb.getRenderBuffer());
            color_attachment[nummber] = rb;
            disable();
        }
    }
    /**
     * Binded eine Textur auf ein bestimmtes Color Attachment des FBO.
     * <br>
     * @param nummber
     * <br>
     * Nummer des Color Attachments(0 bis GL_MAX_COLOR_ATTACHMENTS_EXT-1).
     * <br>
     * @param tex
     * <br>
     * Die Textur die gebunden werden soll.
     */
    public void setColor_Attachment(int nummber, Texture tex)
    {
        checkSize(tex.getWidth(), tex.getHeight());

        if (tex.getTarget() != GL.GL_TEXTURE_2D)
            return;
        if (nummber < GL2GL3.GL_MAX_COLOR_ATTACHMENTS) {
            bind();
            GL gl = GLContext.getCurrentGL();
            gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                                         GL.GL_COLOR_ATTACHMENT0 + nummber,
                                         GL.GL_TEXTURE_2D, tex.getTextureObject(), 0);
            color_attachment[nummber] = tex;
            disable();
        }
    }

    /**
     * Gibt die Textur zur�ck die an ein bestimmtes Color Attechment gebunden ist.
     * <br>
     * @param nummber
     * <br>
     * Nummer des Color Attachments(0 bis GL_MAX_COLOR_ATTACHMENTS_EXT-1).
     * <br>
     * @return
     * Gibt die an das Color Attachtment gebundene Textur zur�ck.
     * NULL wenn anstatt einer Textur ein Renderbuffer gebunden ist
     * oder das Attachment noch frei ist.
     */
    public Texture getColor_AttachmentTexture(int nummber)
    {
        try {
            return getColor_Attachment(nummber, Texture.class);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Das ColorAttachment "+nummber+" ist keine Textur!");
        }
    }

    /**
     * Gibt den Renderbuffer zur�ck der an ein bestimmtes Color Attechment gebunden ist.
     * <br>
     * @param nummber
     * <br>
     * Nummer des Color Attachments(0 bis GL_MAX_COLOR_ATTACHMENTS_EXT-1).
     * <br>
     * @return
     * Gibt den an das Color Attachtment gebundenen Renderbuffer zur�ck.
     * NULL wenn anstatt eines Renderbuffers eine Textur gebunden ist
     * oder das Attachment noch frei ist.
     */
    public RenderBuffer getColor_AttachmentBuffer(int nummber)
    {
        try {
            return getColor_Attachment(nummber, RenderBuffer.class);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException("Das ColorAttachment "+nummber+" ist kein Renderbuffer!");
        }
    }

    private <T> T getColor_Attachment(int nummber, Class<T> cl) throws NoSuchFieldException
    {
        if (nummber < GL2GL3.GL_MAX_COLOR_ATTACHMENTS)
            if (cl.isInstance(color_attachment[nummber]))
                return (T) color_attachment[nummber];
        throw new NoSuchFieldException();
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Depth-Attachment Setter/Getter">
    /**
     * Binded ein Renderbuffer als Depth Attachment des FBOs.
     * <br>
     * @param rb
     * <br>
     * Der Renderbuffer der gebunden werden soll.
     */
    public void setDepth_Attachment(RenderBuffer rb)
    {
        checkSize(rb.getWidth(), rb.getHeight());

        bind();
        GL gl = GLContext.getCurrentGL();
        gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
                                        GL.GL_DEPTH_ATTACHMENT,
                                        GL.GL_RENDERBUFFER, rb.getRenderBuffer());
        depth_attachment = rb;
        disable();
    }

    /**
     * Binded eine Textur als Depth Attachment des FBOs.
     * <br>
     * @param tex
     * <br>
     * Die Textur die gebunden werden soll.
     */
    public void setDepth_Attachment(Texture tex)
    {
        checkSize(tex.getWidth(), tex.getHeight());

        bind();
        if (tex.getTarget() != GL.GL_TEXTURE_2D)
            return;
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                                     GL.GL_DEPTH_ATTACHMENT,
                                     GL.GL_TEXTURE_2D, tex.getTextureObject(), 0);
        depth_attachment = tex;
        disable();
    }

    /**
     * Gibt die Textur zur�ck die als Depth Attachment gebunden ist.
     * <br>
     * @return
     * Gibt die als Depth Attachment gebundene Textur zur�ck.
     * NULL wenn anstatt einer Textur ein Renderbuffer gebunden ist
     * oder das Attachment noch frei ist.
     */
    public Texture getDepth_AttachmentTexture()
    {
        return getDepth_Attachment(Texture.class);
    }

    /**
     * Gibt den RenderBuffer zur�ck der als Depth Attachment gebunden ist.
     * <br>
     * @return
     * Gibt den als Depth Attachment gebundenen RenderBuffer zur�ck.
     * NULL wenn anstatt einer Textur ein Renderbuffer gebunden ist
     * oder das Attachment noch frei ist.
     */
    public RenderBuffer getDepth_AttachmentBuffer()
    {
        return getDepth_Attachment(RenderBuffer.class);
    }

    private <T> T getDepth_Attachment(Class<T> cl)
    {
        if (cl.isInstance(depth_attachment))
            return (T) depth_attachment;
        return null;
    }// </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Stencil-Attachment Setter/Getter">
     
    /**
     * Binded ein Renderbuffer als Stencil Attachment des FBOs.
     * <br>
     * @param rb
     * <br>
     * Der Renderbuffer der gebunden werden soll.
     */
    public void setStencil_Attachment(RenderBuffer rb)
    {

        checkSize(rb.getWidth(), rb.getHeight());

        bind();
        GL gl = GLContext.getCurrentGL();
        gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER,
                                        GL.GL_STENCIL_ATTACHMENT,
                                        GL.GL_RENDERBUFFER, rb.getRenderBuffer());
        depth_attachment = rb;
        disable();
    }

    /**
     * Binded eine Textur als Stencil Attachment des FBOs.
     * <br>
     * @param tex
     * <br>
     * Die Textur die gebunden werden soll.
     */
    public void setStencil_Attachment(Texture tex)
    {
        checkSize(tex.getWidth(), tex.getHeight());

        bind();
        if (tex.getTarget() != GL.GL_TEXTURE_2D)
            return;
        GL gl = GLContext.getCurrentGL();
        gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                                     GL.GL_STENCIL_ATTACHMENT,
                                     GL.GL_TEXTURE_2D, tex.getTextureObject(), 0);
        depth_attachment = tex;
        disable();
    }

    /**
     * Gibt die Textur zur�ck die als Stencil Attachment gebunden ist.
     * <br>
     * @return
     * Gibt die als Depth Attachment gebundene Textur zur�ck.
     * NULL wenn anstatt einer Textur ein Renderbuffer gebunden ist
     * oder das Attachment noch frei ist.
     */
    public Texture getStencil_AttachmentTexture()
    {
        return getStencil_Attachment(Texture.class);
    }


    /**
     * Gibt den RenderBuffer zur�ck der als Stencil Attachment gebunden ist.
     * <br>
     * @return
     * Gibt den als Depth Attachment gebundenen RenderBuffer zur�ck.
     * NULL wenn anstatt einer Textur ein Renderbuffer gebunden ist
     * oder das Attachment noch frei ist.
     */
    public RenderBuffer getStencil_AttachmentBuffer()
    {
        return getStencil_Attachment(RenderBuffer.class);
    }

    private <T> T getStencil_Attachment(Class<T> cl)
    {
        if (cl.isInstance(stencil_attachment))
            return (T) stencil_attachment;
        return null;
    }// </editor-fold>

    /**
     * bindet das FBO an den GL Context.
     */
    public void bind()
    {
        GL gl = GLContext.getCurrentGL();
        gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, fb[0]);
    }

    /**
     * Falls das Color_Attachment0 eine Textur nutzt wird diese
     * an den GL Context gebunden.
     */
    public void bindColorAtt0Texture()
    {
        Texture tex = getColor_AttachmentTexture(0);
        if (tex != null)
            tex.bind();
    }

    /**
     * Binded an den GL Context wieder das Standart RenderTarget (0).
     */
    public void disable()
    {
        GL gl = GLContext.getCurrentGL();
        gl.glBindFramebuffer(gl.GL_FRAMEBUFFER, 0);
    }

    /**
     * L�scht das  FBO aus dem Grafikspeicher.
     */
    public void delete()
    {
        GL gl = GLContext.getCurrentGL();
        gl.glDeleteFramebuffers(1, fb, 0);
    }

    /**
     * @return
     * gibt die H�he des FBO in pixel zur�ck
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * @return
     * gibt die Breite des FBO in pixel zur�ck
     */
    public int getWidth()
    {
        return width;
    }

    private void setHeight(int height)
    {
        this.height = height;
    }

    private void setWidth(int width)
    {
        this.width = width;
    }

    private void checkSize(int width, int height)
    {

        if (getWidth() == 0)
            setWidth(width);
        else if (getWidth() != width)
            throw new RuntimeException("Attachment size differs from FBO's");

        if (getHeight() == 0)
            setHeight(height);
        else if (getHeight() != height)
            throw new RuntimeException("Attachment size differs from FBO's");
    }

    /**
     * Setzt das ViewPort Attribut des GL Contextes auf die gr��e des FBO und
     * legt gleichzeitig die vorherige Einstellung auf den Attribut Stack ab.
     */
    public void pushViewPort()
    {
        GL gl = GLContext.getCurrentGL();
        gl.glGetIntegerv(gl.GL_VIEWPORT, viewport, 0);
        gl.glViewport(0, 0, getWidth(), getHeight());
    }

    /**
     * l�d die letzten Viewport einstellung.
     */
    public void popViewPort(){
        GL gl = GLContext.getCurrentGL();
        gl.glViewport(0, 0, viewport[2], viewport[3]);
    }
}
