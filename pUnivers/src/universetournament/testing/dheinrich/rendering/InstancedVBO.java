/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.rendering;

import universetournament.client.rendering.geometrie.unpacked.Mesh;
import com.sun.opengl.util.texture.Texture;
import java.nio.*;
import javax.media.opengl.*;
import universetournament.client.rendering.geometrie.packed.VBO;
import universetournament.client.rendering.geometrie.vertexattributstuff.*;

/**
 *
 * @author dheinrich
 */
public class InstancedVBO extends VBO
{
    private Texture transtex;
    private int transbuffer, maxinstances, instancecount;

    public InstancedVBO(int maxinstances, Mesh mesh) throws AttributeExeption {
        super(mesh);
        this.maxinstances = maxinstances;
        transtex = new Texture(GL2GL3.GL_TEXTURE_BUFFER);
        GL gl = getGl();
        int[] tmp = new int[1];
        gl.glGenBuffers(1, tmp, 0);
        transbuffer = tmp[0];

        int data_size = maxinstances * 4 * AttType.FLOAT.getBSize();
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, transbuffer);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, data_size, null, GL.GL_STATIC_DRAW);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    public void setInstances(Instance[] instances) {
        setInstances(instances, 0);
    }

    public void setInstances(Instance[] instances, int offset) {
        GL gl = getGl();
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, transbuffer);

        FloatBuffer data = gl.glMapBuffer(GL.GL_ARRAY_BUFFER,
                                               GL.GL_WRITE_ONLY).asFloatBuffer();
        data.position(offset);

        for (Instance i : instances)
            try {
                data.put(i.getX());
                data.put(i.getY());
                data.put(i.getZ());
                data.put(i.getOther());
            } catch (BufferOverflowException ex) {
                break;
            }

        gl.glUnmapBuffer(GL.GL_ARRAY_BUFFER);
    }

    public void render() {
        if (!isActive())
            return;
        prepareVBO();
        getGl().glDrawElementsInstanced(getPrimitivtype(), getIntcount(),
                                        GL2GL3.GL_UNSIGNED_INT,
                                        IntBuffer.wrap(getMesh().getIndicies()),
                                        instancecount);
        doneVBO();
    }

    @Override
    protected void prepareVBO() {
        super.prepareVBO();
        GL2GL3 gl = getGl();
        gl.glActiveTexture(GL.GL_TEXTURE0);
        transtex.bind();
        gl.glTexBuffer(GL2GL3.GL_TEXTURE_BUFFER, GL2GL3.GL_RGBA32F,
                            transbuffer);
    }

    @Override
    protected void doneVBO() {
        super.doneVBO();
    }

    public int getInstancecount() {
        return instancecount;
    }

    public int getMaxinstances() {
        return maxinstances;
    }

    public void setInstancecount(int instancecount) {
        this.instancecount = instancecount;
    }
}
