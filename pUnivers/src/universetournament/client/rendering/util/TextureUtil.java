/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.util;

import com.sun.opengl.util.texture.*;
import java.io.*;
import java.util.logging.*;
import javax.media.opengl.*;

/**
 *
 * @author dheinrich
 */
public class TextureUtil
{
    private static final Logger logger = Logger.getLogger(TextureUtil.class.
            getName());
    private static final String[] postfixes = new String[]{"_RT", "_LT", "_UP",
                                                           "_DN", "_FT", "_BK"};
    private static final String filetyp = "dds";

    /**
     * Eine Factory methode um schnell eine leere 2D Textur im Graphic Speicher
     * zu allocieren.
     *
     * @param internalFormat
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
     * @param border
     * <br>
     * Breite des Rahmens || 0 oder 1
     * <br>
     * @param pixelFormat
     * <br>
     * Bestimmt das Format der Pixeldaten.
     * <br>
     * Folgende symbolische Werte werden akzeptiert:
     * <br>
     * GL_COLOR_INDEX, GL_RED, GL_GREEN, GL_BLUE, GL_ALPHA, GL_RGB, GL_BGR,
     * GL_RGBA, GL_BGRA, GL_LUMINANCE, und GL_LUMINANCE_ALPHA
     * <br>
     * @param pixelType
     * <br>
     * Bestimmt den Pixeltyp f�r den Inhalt von pixels.
     * <br>
     * Folgende Typen werden unterst�tzt:
     * <br>
     * GL_UNSIGNED_BYTE, GL_BYTE, GL_BITMAP, GL_UNSIGNED_SHORT, GL_SHORT,
     * GL_UNSIGNED_INT, GL_INT, GL_FLOAT, GL_UNSIGNED_BYTE_3_3_2,
     * GL_UNSIGNED_BYTE_2_3_3_REV, GL_UNSIGNED_SHORT_5_6_5,
     * GL_UNSIGNED_SHORT_5_6_5_REV, GL_UNSIGNED_SHORT_4_4_4_4,
     * GL_UNSIGNED_SHORT_4_4_4_4_REV, GL_UNSIGNED_SHORT_5_5_5_1,
     * GL_UNSIGNED_SHORT_1_5_5_5_REV, GL_UNSIGNED_INT_8_8_8_8,
     * GL_UNSIGNED_INT_8_8_8_8_REV, GL_UNSIGNED_INT_10_10_10_2 und
     * GL_UNSIGNED_INT_2_10_10_10_REV.
     * <br>
     * @param mipmap
     * <br>
     * Boolischer Wert ob Mipmaps der Textur mittel GLU erstellt werden sollen.
     * <br>
     *
     * @return
     * Die erstellte Textur wird zur�ckgegeben.
     */
    public static Texture newTexture(int internalFormat,
                                     int width, int height, int border,
                                     int pixelFormat, int pixelType,
                                     boolean mipmap) {

        TextureData a = new TextureData(internalFormat, width, height, border,
                                        pixelFormat, pixelType,
                                        mipmap, false, false,
                                        null, null);
        Texture tex = TextureIO.newTexture(GL.GL_TEXTURE_2D);
        tex.updateImage(a);

        return tex;
    }

    /**
     *Setzt die MIN und MAG Filterung der Textur sowie wie sie gewrapt werden soll.
     * @param tex
     * <br>
     * Die Texture die bearbeitet werden soll.
     * <br>
     * @param filtering
     * <br>
     * Es k�nnen nur folgende werte genutzt werden:
     * <br><br>
     * GL_NEAREST
     * <br>
     * Liefert den Wert des Texturelements, welches am n�chsten (in Manhattandistanz)
     * zum Zentrum des zu texturierenden Pixels liegt.
     * <br><br>
     * GL_LINEAR
     * <br>
     * Liefert den gewichteten Mittelwert der vier Teexturenelemente zur�ck,
     * die dem Zentrum des zu texturierenden Pixels am n�chsten sind.
     * Dies kann Randpixel-Elemente einschliessen, je nachdem wie
     * GL_TEXTURE_WRAP_S und GL_TEXTURE_WRAP_T eingestellt sind.
     * <br>
     * @param wrapstyle
     * <br>
     * Setzt die wrap funktion die f�r die Texture Coordinaten S und T genutzt werden.
     * Es k�nnen folgende Symbole genutzt werden:
     * <br><br>
     * GL_CLAMP, GL_CLAMP_TO_BORDER, GL_CLAMP_TO_EDGE, GL_REPEAT, GL_MIRRORED_REPEAT
     */
    public static void setTexturePara(Texture tex, int filtering, int wrapstyle) {
        tex.setTexParameteri(GL.GL_TEXTURE_MIN_FILTER, filtering);
        tex.setTexParameteri(GL.GL_TEXTURE_MAG_FILTER, filtering);
        tex.setTexParameteri(GL.GL_TEXTURE_WRAP_S, wrapstyle);
        tex.setTexParameteri(GL.GL_TEXTURE_WRAP_T, wrapstyle);
    }

    /**
     * L�d eine Texture mit angegebener Filterung und Warp Funktion.
     */
    public static Texture loadTexture(String path, int filtering, int wrapstyle) throws IOException {
        Texture tex = loadTexture(path);
        setTexturePara(tex, filtering, wrapstyle);
        return tex;
    }

    /**
     * L�d eine Textur ohne eine Filterung oder Warp Funktion zu setzten
     */
    public static Texture loadTexture(String path) throws IOException {
        try {
            Texture tex = TextureIO.newTexture(new File(path), false);
            logger.log(Level.INFO, "Texture: " + path + " ...loaded!");
            return tex;
        } catch (GLException ex) {
            logger.log(Level.SEVERE, "GL Fehler beim laden der Texture " + path + "\n(" + ex.
                    getLocalizedMessage() + ")");
            throw new IOException("Error loading file " + path);
        }
    }

    public static Texture loadCubeMap(String path) throws IOException {
        Texture cubemap = TextureIO.newTexture(GL.GL_TEXTURE_CUBE_MAP);
        cubemap.bind();

        GL gl = GLContext.getCurrentGL();
        for (int i = 0; i < 6; ++i) {
            File file = new File(path + postfixes[i] + "." + filetyp);
            TextureData data = TextureIO.newTextureData(file, false, filetyp);
            System.out.println(i);
            cubemap.updateImage(data, GL.GL_TEXTURE_CUBE_MAP + 2 + i);
//            gl.glTexImage2D(GL.GL_TEXTURE_CUBE_MAP + 2 + i, 0,
//                            data.getInternalFormat(), data.getWidth(),
//                            data.getHeight(), data.getBorder(),
//                            data.getPixelFormat(), data.getPixelType(),
//                            data.getBuffer());
        }
        setTexturePara(cubemap, GL.GL_LINEAR, GL.GL_CLAMP_TO_EDGE);
        return cubemap;
    }
}
