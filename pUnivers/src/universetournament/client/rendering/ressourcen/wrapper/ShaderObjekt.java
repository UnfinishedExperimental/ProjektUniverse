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
 * CPU seitige Repr�sentation eines OpenGL Shader Objekts
 * @author Daniel Heinrich
 */
public class ShaderObjekt {
    private static final Logger logger = Logger.getLogger(ShaderObjekt.class.getName());

    private int type;
    private int shaderobjekt;

    public ShaderObjekt(int type, String[] shadertext)
    {
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        this.type = type;

        shaderobjekt = gl.glCreateShader(type);
        gl.glShaderSource(shaderobjekt, shadertext.length, shadertext, (int[]) null, 0);
        gl.glCompileShader(shaderobjekt);
        int[] error = new int[]{-1};
        gl.getGL2().glGetObjectParameterivARB(shaderobjekt,
                                              GL2.GL_OBJECT_COMPILE_STATUS_ARB,
                                              error, 0);
        if (error[0] == 0) {
            int[] len = new int[]{512};
            byte[] errormessage = new byte[512];
            gl.getGL2().glGetShaderInfoLog(shaderobjekt, 512, len,
                                            0, errormessage,
                                            0);
            logger.log(Level.SEVERE, "Shader compile ERROR:\n" +
                    new String(errormessage, 0, len[0]));
        }
    }

    public void delete(){
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        gl.glDeleteShader(getShaderobjekt());
    }

    public int getShaderobjekt()
    {
        return shaderobjekt;
    }

    public int getType()
    {
        return type;
    }
}
