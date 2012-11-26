/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.resmanagment;

import com.sun.opengl.util.texture.Texture;
import java.io.*;
import java.util.logging.*;
import javax.media.opengl.GL;
import universetournament.client.rendering.util.TextureUtil;

/**
 *
 * @author dheinrich
 */
public class TextureLoadJob implements LoadJob<Texture>
{
    private static final Logger logger = Logger.getLogger(TextureLoadJob.class.
            getName());
    private static final String texturepath = "Textures/";

    private final String path;
    private final int filtering, wrapping;
    protected TextureContainer tcontainer;

    public TextureLoadJob(String path, int filtering, int wrapping) {
        this.path = texturepath + path;
        this.filtering = filtering;
        this.wrapping = wrapping;
    }

    public TextureLoadJob(File file, int filtering, int wrapping) {
        this.path = file.getAbsolutePath();
        this.filtering = filtering;
        this.wrapping = wrapping;
    }

    public int getFiltering() {
        return filtering;
    }

    public String getPath() {
        return path;
    }

    public int getWrapping() {
        return wrapping;
    }

    public void setCon(TextureContainer tcontainer) {
        this.tcontainer = tcontainer;
    }

    public Texture load() {
        Texture re = null;
        try {
            re = TextureUtil.loadTexture(path, filtering,
                                         wrapping);
        } catch (IOException ex) {
            logger.log(Level.WARNING,
                       "Texture " + path + " konnte nicht geladen werden.\n("+
                       ex.getLocalizedMessage()+")");
            try {
                re = TextureUtil.loadTexture("Textures/error.dds",
                                             GL.GL_NEAREST,
                                             GL.GL_REPEAT);
            } catch (IOException ex1) {
                logger.log(Level.SEVERE,
                           "keine standart Texturen gefunden");
            }
        }
        tcontainer.setTexture(re);
        return re;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TextureLoadJob other = (TextureLoadJob) obj;
        if ((this.path == null) ? (other.path != null) : !this.path.equals(
                other.path))
            return false;
        if (this.filtering != other.filtering)
            return false;
        if (this.wrapping != other.wrapping)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 53 * hash + this.filtering;
        hash = 53 * hash + this.wrapping;
        return hash;
    }
}
