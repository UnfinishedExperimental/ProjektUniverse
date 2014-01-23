/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.util;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;

import com.sun.naming.internal.*;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;
import universetournament.client.rendering.ressourcen.resmanagment.*;
import universetournament.client.rendering.ressourcen.wrapper.ShaderObjekt;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;

/**
 *
 * @author Daniel Heinrich
 */
public class ShaderUtil {

    private static final Logger logger = Logger.getLogger(ShaderUtil.class.
            getName());
    private static int GLSL_VERISON = 120;
    
    private static final String PATH = "/resources/Shaders/" ;

    /**
     * Erstellt eines ShaderProgramm Object.
     * <br>
     *
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
     * Eine Liste von Uniform variable der ShaderProgramm deren positionen
     * abgefragt werden sollen.
     */
    public static ShaderProgramm loadShader(String vs, String fs,
                                            String... uni) {
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        if (GLSL_VERISON < 0) {
            String name = gl.glGetString(GL2GL3.GL_SHADING_LANGUAGE_VERSION);
            Matcher matcher = Pattern.compile("(\\d+(:?\\.\\d+)?).*").matcher(name);
            if (matcher.matches()) {
                double val = Double.parseDouble(matcher.group(1));
                if (val < 10) {
                    val *= 100;
                }
                GLSL_VERISON = (int) val;
            } else {
                GLSL_VERISON = 100;
            }
        }
        ShaderObjekt vso = null, fso = null;
        if (vs != null) {
            vso = new ShaderObjekt(GL2GL3.GL_VERTEX_SHADER, getData(vs));
        }

        if (fs != null) {
            fso = new ShaderObjekt(GL2GL3.GL_FRAGMENT_SHADER,
                                   getData(fs));
        }
        logger.log(Level.INFO,
                   "Shader(" + vs + "/" + fs + ")  ...loaded!");
        return new ShaderProgramm(vso, fso, uni);
    }

    private static String[] getData(String file) {
        String[] out = new String[1];
        StringBuffer sb = new StringBuffer();
        try {
            InputStream fr = RessourcesLoader.getInstance().getRessource(PATH + file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fr));
            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("#")) {
                    if (line.startsWith("include", 1)) {
                        String shader = line.split(" ")[1];
                        String[] src = getData(shader);
                        sb.append(src[0]);
                        continue;
                    } else if (line.startsWith("version", 1)) {
                        line = "#version " + GLSL_VERISON;
                    }
                }
                sb.append(line + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        out[0] = sb.toString();
        return out;
    }

    public static void main(String[] args) {
        String[] data = getData(PATH+"Phong.vert");
        for (String s : data) {
            System.out.println("##################################");
            System.out.println(s);
            System.out.println("\n\n");
        }
    }
}
