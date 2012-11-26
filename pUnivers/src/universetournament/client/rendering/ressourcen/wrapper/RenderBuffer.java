/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.wrapper;

import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

/**
 *
 * @author Daniel Heinrich
 */
public class RenderBuffer
{
    private int samples, width, height, texformat;
    private int[] render_buf = new int[1];

    /**
     * erstellt einen normalen RenderBuffer
     * <br>
     * @param gl
     * <br>
     * Der GL Context in dem der RenderBuffer erzeugt wird
     * <br>
     * @param width
     * <br>
     * Breite = Anzahl der Pixel pro Zeile
     * <br>
     * muss als Wert 2^n + 2 * border f�r n Integerwerte haben.
     * <br>
     * @param height
     * <br>
     * H�he = Anzahl der Zeilen
     * <br>
     * muss als Wert 2^n + 2 * border f�r n Integerwerte haben
     * <br>
     * @param texformat
     * <br>
     * eine der folgenden symbolischen Konstanten :
     * <br>
     * GL_ALPHA, GL_ALPHA4, GL_ALPHA8, GL_ALPHA12, GL_ALPHA16,
     * GL_COMPRESSED_ALPHA, GL_COMPRESSED_LUMINANCE,
     * GL_COMPRESSED_LUMINANCE_ALPHA, GL_COMPRESSED_INTENSITY,
     * GL_COMPRESSED_RGB, GL_COMPRESSED_RGBA, GL_DEPTH_COMPONENT,
     * GL_DEPTH_COMPONENT16, GL_DEPTH_COMPONENT24, GL_DEPTH_COMPONENT32,
     * GL_LUMINANCE, GL_LUMINANCE4, GL_LUMINANCE8, GL_LUMINANCE12,
     * GL_LUMINANCE16, GL_LUMINANCE_ALPHA, GL_LUMINANCE4_ALPHA4,
     * GL_LUMINANCE6_ALPHA2, GL_LUMINANCE8_ALPHA8, GL_LUMINANCE12_ALPHA4,
     * GL_LUMINANCE12_ALPHA12, GL_LUMINANCE16_ALPHA16, GL_INTENSITY,
     * GL_INTENSITY4, GL_INTENSITY8, GL_INTENSITY12, GL_INTENSITY16,
     * GL_R3_G3_B2, GL_RGB, GL_RGB4, GL_RGB5, GL_RGB8, GL_RGB10, GL_RGB12,
     * GL_RGB16, GL_RGBA, GL_RGBA2, GL_RGBA4, GL_RGB5_A1, GL_RGBA8, GL_RGB10_A2,
     * GL_RGBA12, GL_RGBA16, GL_SLUMINANCE, GL_SLUMINANCE8, GL_SLUMINANCE_ALPHA,
     * GL_SLUMINANCE8_ALPHA8, GL_SRGB, GL_SRGB8, GL_SRGB_ALPHA oder GL_SRGB8_ALPHA8.
     *
     */
    public RenderBuffer(int width, int height, int texformat)
    {
        ini(0, width, height, texformat);
    }
    
    /**
     * erstellt einen MultiSampled RenderBuffer
     * <br>
     * @param gl
     * <br>
     * Der GL Context in dem der RenderBuffer erzeugt wird
     * <br>
     * @param samples
     * <br>
     * anzahl der Sample mit der der Renderbuffer Multisampled werden soll
     * <br>
     * @param width
     * <br>
     * Breite = Anzahl der Pixel pro Zeile
     * <br>
     * muss als Wert 2^n + 2 * border f�r n Integerwerte haben.
     * <br>
     * @param height
     * <br>
     * H�he = Anzahl der Zeilen
     * <br>
     * muss als Wert 2^n + 2 * border f�r n Integerwerte haben
     * <br>
     * @param texformat
     * <br>
     * eine der folgenden symbolischen Konstanten :
     * <br>
     * GL_ALPHA, GL_ALPHA4, GL_ALPHA8, GL_ALPHA12, GL_ALPHA16,
     * GL_COMPRESSED_ALPHA, GL_COMPRESSED_LUMINANCE,
     * GL_COMPRESSED_LUMINANCE_ALPHA, GL_COMPRESSED_INTENSITY,
     * GL_COMPRESSED_RGB, GL_COMPRESSED_RGBA, GL_DEPTH_COMPONENT,
     * GL_DEPTH_COMPONENT16, GL_DEPTH_COMPONENT24, GL_DEPTH_COMPONENT32,
     * GL_LUMINANCE, GL_LUMINANCE4, GL_LUMINANCE8, GL_LUMINANCE12,
     * GL_LUMINANCE16, GL_LUMINANCE_ALPHA, GL_LUMINANCE4_ALPHA4,
     * GL_LUMINANCE6_ALPHA2, GL_LUMINANCE8_ALPHA8, GL_LUMINANCE12_ALPHA4,
     * GL_LUMINANCE12_ALPHA12, GL_LUMINANCE16_ALPHA16, GL_INTENSITY,
     * GL_INTENSITY4, GL_INTENSITY8, GL_INTENSITY12, GL_INTENSITY16,
     * GL_R3_G3_B2, GL_RGB, GL_RGB4, GL_RGB5, GL_RGB8, GL_RGB10, GL_RGB12,
     * GL_RGB16, GL_RGBA, GL_RGBA2, GL_RGBA4, GL_RGB5_A1, GL_RGBA8, GL_RGB10_A2,
     * GL_RGBA12, GL_RGBA16, GL_SLUMINANCE, GL_SLUMINANCE8, GL_SLUMINANCE_ALPHA,
     * GL_SLUMINANCE8_ALPHA8, GL_SRGB, GL_SRGB8, GL_SRGB_ALPHA oder GL_SRGB8_ALPHA8.
     *
     */
    public RenderBuffer(GL2GL3 gl, int samples, int width, int height, int texformat)
    {
        ini(samples, width, height, texformat);
    }

    private void ini(int samples, int width, int height, int texformat){
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        this.samples = samples;
        this.width = width;
        this.height = height;
        this.texformat = texformat;

        gl.glGenRenderbuffers(1, render_buf, 0);
        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, render_buf[0]);
        if(samples > 0)
            gl.glRenderbufferStorageMultisample(GL2GL3.GL_RENDERBUFFER, samples, texformat,
                    width, height);
        else
            gl.glRenderbufferStorage(GL2GL3.GL_RENDERBUFFER, texformat, width, height);
    }

    /**
     * @return
     * Gibt die H�he des Renderbuffers in pixel zur�ck.
     */
    public int getHeight()
    {
        return height;
    }

    /**
     * @return
     * Gibt den Index des Renderbuffers im Grafikspeicher zur�ck.
     */
    public int getRenderBuffer()
    {
        return render_buf[0];
    }

    /**
     * @return
     * Gibt die Anzahl der Samples zur�ck die genutzt werden.
     */
    public int getSamples()
    {
        return samples;
    }

    /**
     * @return
     * Gibt das interne Textureformat des Renderbuffers zur�ck.
     */
    public int getTexformat()
    {
        return texformat;
    }

    /**
     * @return
     * Gibt die Breite des Renderbuffers in pixel zur�ck
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * L�scht den Renderbuffer aus den Grafikspeicher.
     */
    public void delete(){
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        gl.glDeleteRenderbuffers(1, render_buf, 0);
    }

}
