/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.resmanagment;

import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import universetournament.client.rendering.util.TextureUtil;

/**
 *
 * @author some
 */
public class HeightMapLoadJob extends TextureLoadJob
{
    private static final Logger logger = Logger.getLogger(HeightMapLoadJob.class.
            getName());

    public HeightMapLoadJob(String path) {
        super(path, -1, -1);
    }

    @Override
    public Texture load() {
        File f = new File(getPath());

        Texture re = null;
        try {
            TextureData td =
                        TextureIO.newTextureData(f,
                                                 GL2GL3.GL_LUMINANCE_FLOAT32_ATI,
                                                 GL2GL3.GL_LUMINANCE, true,
                                                 f.getName().split("\\.")[1]);
            re = TextureIO.newTexture(td);
            TextureUtil.setTexturePara(re, GL.GL_NEAREST, GL.GL_CLAMP_TO_EDGE);
            tcontainer.setTexture(re);
        } catch (IOException ex) {
            logger.log(Level.SEVERE,
                       "Heigthmap " + getPath() + " konnte nicht geladen werden.\n("
                    + ex.getLocalizedMessage() + ")");
        }

        return re;
    }
}
