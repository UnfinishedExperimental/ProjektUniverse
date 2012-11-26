/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;
import universetournament.client.rendering.ressourcen.wrapper.ShaderObjekt;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 *
 * @author Daniel Heinrich
 */
public class ShaderUtil
{
    private static final Logger logger = Logger.getLogger(ShaderUtil.class.
            getName());
    private static int GLSL_VERISON = -1;

    /**
     * Erstellt eines ShaderProgramm Object.
     * <br>
     * @param gl
     * <br>
     * Der GL Context in dem das Programm erstellt werden soll.
     * <br>
     * @param vs
     * <br>
     * Ein File Objekt welches auf eine Vertex ShaderProgramm Datei zeigt.
     * <br>
     * @param fs
     * <br>
     * Ein File Objekt welches auf eine Fragment ShaderProgramm Datei zeigt.
     * <br>
     * @param uni
     * <br>
     * Eine Liste von Uniform variable der ShaderProgramm deren positionen abgefragt werden sollen.
     */
    public static ShaderProgramm loadShader(File vs, File fs,
                                            String... uni) {
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        if (GLSL_VERISON < 0)
            GLSL_VERISON = (int) Double.parseDouble(
                    gl.glGetString(GL2GL3.GL_SHADING_LANGUAGE_VERSION)) * 100;
        ShaderObjekt vso = null, fso = null;
        if (vs != null)
            vso = new ShaderObjekt(GL2GL3.GL_VERTEX_SHADER, getData(vs));

        if (fs != null)
            fso = new ShaderObjekt(GL2GL3.GL_FRAGMENT_SHADER,
                                   getData(fs));
        logger.log(Level.INFO,
                   "Shader(" + vs.getName() + "/" + fs.getName() + ")  ...loaded!");
        return new ShaderProgramm(vso, fso, uni);
    }

    /**
     * Erstellt eines ShaderProgramm Object.
     * <br>
     * @param gl
     * <br>
     * Der GL Context in dem das Programm erstellt werden soll.
     * <br>
     * @param vs
     * <br>
     * Pfad der auf eine Vertex ShaderProgramm Datei zeigt (relativ zu "./resources/Shaders/").
     * <br>
     * @param fs
     * <br>
     * Pfad der auf eine Fragment ShaderProgramm Datei zeigt (relativ zu "./resources/Shaders/").
     * <br>
     * @param uni
     * <br>
     * Eine Liste von Uniform variable der ShaderProgramm deren positionen abgefragt werden sollen.
     */
    public static ShaderProgramm loadShader(String vs, String fs,
                                            String... uni) {
        File fvs = getShaderFile(vs);
        File ffs = getShaderFile(fs);

        return loadShader(fvs, ffs, uni);
    }

    private static File getShaderFile(String path) {
        URL url;
        File shader = null;
//        if (path != null)
//            shader = new File("Shaders/" + path);
        try {
            if (path != null) {
                url = path.getClass().getResource("/resources/Shaders/" + path);
                if (url == null)
                    throw new FileNotFoundException(
                            "Couldn't find Shader at: " + "/resources/Shaders/" + path);
                shader = new File(URLDecoder.decode(url.getPath(), "UTF-8"));
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        return shader;
    }

    private static String[] getData(File file) {
        String[] out = new String[1];
        StringBuffer sb = new StringBuffer();
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("#"))
                    if (line.startsWith("include", 1)) {
                        File shader = getShaderFile(line.split(" ")[1]);
                        String[] src = getData(shader);
                        sb.append(src[0]);
                        continue;
                    } else if (line.startsWith("version", 1))
                        line = "#version " + GLSL_VERISON;
                sb.append(line + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        out[0] = sb.toString();
        return out;
    }

    public static void main(String[] args) {
        String[] data = getData(getShaderFile("Phong.vert"));
        for (String s : data) {
            System.out.println("##################################");
            System.out.println(s);
            System.out.println("\n\n");
        }
    }
}
