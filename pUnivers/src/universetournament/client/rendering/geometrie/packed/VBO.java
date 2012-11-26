/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.packed;

import universetournament.client.rendering.geometrie.unpacked.VertexBuffer;
import universetournament.client.rendering.geometrie.unpacked.Mesh;
import com.sun.opengl.util.BufferUtil;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
import universetournament.client.rendering.shaders.SimpleShader;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeCollection;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeExeption;

/**
 * CPU seitige repr�sentation eines Vertex Buffer Objekts.
 * Es h�lt die f�r jeden Datentyp wichtigen Vertex Attribut Buffer, sowie
 * den Element(Indicie) Buffer
 * @author Daniel Heinrich
 */
public class VBO
{
    private GL2GL3 gl;
    private int[] buffers;
    private Mesh mesh;
    private int primitivtype, intcount;
    private AttributeCollection attcoll;
    private boolean active = true;

    /**
     * Erstellt ein neuse Vertexold Buffer Objekt
     * @param mesh
     * Das Mesh enth�lt die Verticies und Indicies die genutzt werden sollen.
     * @param gl
     * Der genutzte GL Context
     * @throws AttributeExeption
     * Wird geworfen wenn nicht alle Verticies die selbe Attribute Collection benutzten.
     */
    public VBO(Mesh mesh) throws AttributeExeption {
        this.mesh = mesh;
        ini();
    }

    public Mesh getMesh() {
        return mesh;
    }

    private void ini() {
        try{
            this.gl = GLContext.getCurrentGL().getGL2GL3();
        }catch(GLException ex){
            return;
        }
        primitivtype = mesh.getPrimitiv_typ();
        intcount = mesh.getIndexCount();
        attcoll = mesh.getVertices().getAttrColl();

        // Bind The VertexBuffer
        ByteBuffer[] vert_buffers = buildVertBuffer(mesh.getVertices());
        int bcount = 1 + attcoll.getAttTypesCount();
        buffers = new int[bcount];
        gl.glGenBuffers(bcount, buffers, 0);  // Get A Valid Name

        Iterator<AttType> iter = attcoll.getAttTypes();
        for (int i = 0; iter.hasNext(); i++) {
            AttType at = iter.next();
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[1 + i]);  // Bind The Buffer
            gl.glBufferData(GL.GL_ARRAY_BUFFER, vert_buffers[i].limit(),
                    vert_buffers[i], GL.GL_STATIC_DRAW);
        }
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER,
                        intcount * BufferUtil.SIZEOF_INT,
                        IntBuffer.wrap(mesh.getIndicies()), GL.GL_STATIC_DRAW);
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        mesh = null;
    }

    /**
     * Rendert das VBO im GL Context des VBOs.
     */
    public void render(SimpleShader shader) {
        if (!isActive())// ||  !shader.getAttributs().getClass().isInstance(attcoll)
            return;
       
        shader.getShaderprogramm().use();
        
        prepareVBO();
        gl.glDrawElements(primitivtype, intcount, GL2GL3.GL_UNSIGNED_INT, 0);
        doneVBO();
    }

    /**
     * Initialisiert das VBO komplett neu mit dem angegebenen Mesh.
     * @param mesh
     * Das Mesh Objekt das die neuen Vertexold und Index Daten enth�lt.
     * @throws AttributeExeption
     * Wird geworfen wenn nicht alle Verticies die selbe Attribute Collection benutzten.
     */
    public void updateMesh(Mesh mesh) throws AttributeExeption {
        this.mesh = mesh;
        delete();
        ini();
    }

    protected void prepareVBO() {
        attcoll.enableAttributArrays(gl);

        Iterator<AttType> iter = attcoll.getAttTypes();
        for (int i = 0; iter.hasNext(); i++) {
            AttType at = iter.next();
            gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffers[1 + i]);
            attcoll.bindAttributPointer(at, gl);
        }

        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
    }

    protected void doneVBO() {
        attcoll.disableAttributArrays(gl);
        
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    private ByteBuffer[] buildVertBuffer(VertexBuffer verticies) {
        ByteBuffer[] buffer = new ByteBuffer[attcoll.getAttTypesCount()];

        Iterator<AttType> iter = attcoll.getAttTypes();
        for (int i = 0; iter.hasNext(); i++) {
            AttType at = iter.next();
            buffer[i] = verticies.getBuffer(at);
        }
        return buffer;
    }

    /**
     * L�scht das VBO aus den Grafikspeicher.
     */
    public void delete() {
        gl.glDeleteBuffers(buffers.length, buffers, 0);
        buffers = null;
    }

    public GL2GL3 getGl() {
        return gl;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        if(buffers == null)
            ini();

        return active && buffers!= null;
    }

    public int getIntcount() {
        return intcount;
    }

    public int getPrimitivtype() {
        return primitivtype;
    }

    
}
