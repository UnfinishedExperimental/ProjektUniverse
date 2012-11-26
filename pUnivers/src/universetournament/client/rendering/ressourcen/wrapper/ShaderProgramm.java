/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.wrapper;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;

/**
 * CPU seitige Repr�sentation eines OpenGL Shader Programmes
 * @author Daniel Heinrich
 */
public class ShaderProgramm
{
    private static final Logger logger = Logger.getLogger(ShaderProgramm.class.getName());

    static ShaderProgramm shaderinuse = null;
    private int programObject;
    private int[] uniforms;
    private final GL2GL3 gl;

    /**
     * Erstellt eines ShaderProgramm Object.
     * <br>
     * @param gl
     * <br>
     * Der GL Context in dem das Programm erstellt werden soll.
     * <br>
     * @param vs
     * <br>
     * Vertex Shader Objekt das gelinked werden soll.
     * <br>
     * Falls NULL wird kein Vertex Shader gelinked und die Fixed-Function Pipeline wird verwendet.
     * <br>
     * Vorsicht!! Die Fixed-Function Pipeline ist Depricated und ist ab OpenGL 3.2 nicht mehr vorhanden.
     * <br>
     * @param fs
     * <br>
     * Fragment Shader Objekt das gelinked werden soll.
     * <br>
     * Falls NULL wird kein Fragment Shader gelinked und die Fixed-Function Pipeline wird verwendet.
     * <br>
     * Vorsicht!! Die Fixed-Function Pipeline ist Depricated und ist ab OpenGL 3.2 nicht mehr vorhanden.
     * <br>
     * @param uni
     * <br>
     * Eine Liste von Uniform variable der ShaderProgramm deren positionen abgefragt werden sollen.
     */
    public ShaderProgramm(ShaderObjekt vs, ShaderObjekt fs, String... uni) {
        gl = GLContext.getCurrentGL().getGL2GL3();
        programObject = gl.glCreateProgram();
        if (vs != null)
            gl.glAttachShader(programObject, vs.getShaderobjekt());
        if (fs != null)
            gl.glAttachShader(programObject, fs.getShaderobjekt());

        gl.glLinkProgram(programObject);
        gl.glValidateProgram(programObject);
        int[] error = new int[]{-1};
        gl.getGL2().glGetObjectParameterivARB(programObject,
                                              GL2.GL_OBJECT_LINK_STATUS_ARB,
                                              error, 0);
        if (error[0] == 0) {
            int[] len = new int[]{512};
            byte[] errormessage = new byte[512];
            gl.getGL2().glGetProgramInfoLog(programObject, 512, len,
                                            0, errormessage,
                                            0);
            logger.log(Level.SEVERE, "Shaderprogramm link ERROR:\n" +
                    new String(errormessage, 0, len[0]));
        }


        uniforms = new int[uni.length];
        for (int i = 0; i < uni.length; ++i)
            uniforms[i] = getUniformLocation(uni[i]);
    }

    /**
     * @return
     * Gibt den Index des Programm Objekt im Grafikspeicher zur�ck.
     */
    public int getPObject() {
        return programObject;
    }

    public int getUniformLocation(String s) {
        return gl.glGetUniformLocation(getPObject(), s);
    }

    /**
     * @param index
     * <br>
     * Index der Uniform Variable abh�ngig von der Reihenfolge der Uniform Namen im Constructor.
     * <br>
     * @return
     * <br>
     * Gibt den Index der spezifierten Uniform Varibale im Grafikspeicher zur�ck.
     */
    public int getUnifLoc(int index) {
        return uniforms[index];
    }

    /**
     * @param name
     * Name der Attribut Variable(wie im Vertex ShaderProgramm definiert).
     * @return
     * Gibt den Index der Attribut Varibale im Grafikspeicher zur�ck.
     */
    public int getAttrLocation(String name) {
        return gl.glGetAttribLocation(programObject, name);
    }

    /**
     * Aktiviert das ShaderProgramm Programm im GL Context.
     */
    public void use() {
        if (shaderinuse != this) {
            gl.glUseProgram(getPObject());
            shaderinuse = this;
        }
    }

    /**
     * Deaktiviert ShaderProgramm Programme im GL Context.
     */
    public void disable() {
        gl.glUseProgram(0);
        shaderinuse = null;
    }

    public void delete() {
        gl.glDeleteProgram(getPObject());
        if (shaderinuse == this)
            disable();
    }

    public GL2GL3 getGl() {
        return gl;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ShaderProgramm other = (ShaderProgramm) obj;
        if (this.programObject != other.programObject)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + this.programObject;
        return hash;
    }
}
