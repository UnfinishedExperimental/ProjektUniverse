/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.util;

import com.sun.opengl.util.texture.Texture;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import universetournament.client.rendering.ressourcen.wrapper.FrameBufferObject;
import universetournament.client.rendering.ressourcen.wrapper.RenderBuffer;

/**
 *
 * @author Daniel Heinrich
 */
public class FboUtil
{
    /**
     * Ein Factory methode um ein Frambuffer Objekt(FBO) zu erstellen.
     * Dem FBO wird Als Color Attachment eine standart R8G8B8A8 Textur zugewiesen
     * und als Depth Attachment ein simpler Renderbuffer.
     *
     * @param gl
     * Der GL context in dem das FBO erstellt werden soll
     * @param texsize
     * Die seiten l�nge der dem FBO hinterlegten Attachments. texsize muss eine
     * potentz von 2 sein (2^n). Wichtig alle Attachments m�ssen die selbe gr��e haben.
     *
     * @return
     * das erstellte FBO wird zur�ckgegeben.
     */
    public static FrameBufferObject newStandartFBO(int texsize)
    {
        FrameBufferObject fbo = new FrameBufferObject();

        Texture tex = TextureUtil.newTexture(GL.GL_RGBA, texsize, texsize, 0,
                                 GL.GL_RGBA, GL.GL_UNSIGNED_BYTE,
                                 true);
        TextureUtil.setTexturePara(tex, GL.GL_LINEAR, GL.GL_CLAMP_TO_EDGE);

        RenderBuffer rb = new RenderBuffer(texsize, texsize, GL2GL3.GL_DEPTH_COMPONENT);

        fbo.setColor_Attachment(0, tex);
        fbo.setDepth_Attachment(rb);
        return fbo;
    }

}
