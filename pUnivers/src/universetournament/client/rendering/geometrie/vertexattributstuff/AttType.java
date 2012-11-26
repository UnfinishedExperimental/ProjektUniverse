/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff;

import com.sun.opengl.util.BufferUtil;
import java.nio.ByteBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2GL3;

/**
 * Definiert DatenTypen die in einem Vertex Attribut Array genutzt werden k�nnen.
 * @author Daniel Heinrich
 */
public enum AttType
{

    SHORT(GL.GL_SHORT, BufferUtil.SIZEOF_SHORT)
    {

        public void put(ByteBuffer buf, Number[] values) throws AttributeExeption
        {
            if (values[0] instanceof Short)
                for (int i = 0; i < values.length; i++)
                    buf.putShort((Short) values[i]);
            else
                throw new AttributeExeption("Dieses Attribute ist vom Datentyp Short nicht " + values[0].getClass().getName());
        }

        public void get(ByteBuffer buf, Number[] values)
        {
                for (int i = 0; i < values.length; i++)
                    values[i] = buf.getShort();
        }
    },
    INT(GL2GL3.GL_INT, BufferUtil.SIZEOF_INT)
    {

        public void put(ByteBuffer buf, Number[] values) throws AttributeExeption
        {
            if (values[0] instanceof Integer)
                for (int i = 0; i < values.length; i++)
                    buf.putInt((Integer) values[i]);
            else
                throw new AttributeExeption("Dieses Attribute ist vom Datentyp Integer nicht " + values[0].getClass().getName());
        }

        public void get(ByteBuffer buf, Number[] values)
        {
                for (int i = 0; i < values.length; i++)
                    values[i] = buf.getInt();
        }
    },
    FLOAT(GL.GL_FLOAT, BufferUtil.SIZEOF_FLOAT)
    {

        public void put(ByteBuffer buf, Number[] values) throws AttributeExeption
        {
            if (values[0] instanceof Float)
                for (int i = 0; i < values.length; i++)
                    buf.putFloat((Float) values[i]);
            else
                throw new AttributeExeption("Dieses Attribute ist vom Datentyp Float nicht " + values[0].getClass().getName());
        }

        public void get(ByteBuffer buf, Number[] values)
        {
                for (int i = 0; i < values.length; i++)
                    values[i] = buf.getFloat();
        }
    },
    DOUBLE(GL2GL3.GL_DOUBLE, BufferUtil.SIZEOF_DOUBLE)
    {

        public void put(ByteBuffer buf, Number[] values) throws AttributeExeption
        {
            if (values[0] instanceof Double)
                for (int i = 0; i < values.length; i++)
                    buf.putDouble((Double) values[i]);
            else
                throw new AttributeExeption("Dieses Attribute ist vom Datentyp Double nicht " + values[0].getClass().getName());
        }

        public void get(ByteBuffer buf, Number[] values)
        {
            for (int i = 0; i < values.length; i++)
                values[i] = buf.getDouble();
        }
    },;
    private int gl_const, size_of;

    private AttType(int cst, int so)
    {
        gl_const = cst;
        size_of = so;
    }

    /**
     * @return
     * Gibt die OpenGL constante des Types zur�ck.
     */
    public int getGLConst()
    {
        return gl_const;
    }

    /**
     * @return
     * Gibt die Gr��e des Types in der Anzahl der ben�tigten Bytes zur�ck.
     */
    public int getBSize()
    {
        return size_of;
    }

    /**
     * Erstellt einen Bytbuffer.
     * @param cap
     * Legt die capazit�t des Bytebuffers abh�ngig der Byte Gr��e des Typs fest.
     * @return
     * Gibt den nue erstellten Bytebuffer zur�ck.
     */
    public ByteBuffer createBuffer(int cap)
    {
        return BufferUtil.newByteBuffer(cap * getBSize());
    }

    /**
     * Legt ein Array von neuen Werten abh�ngig vom Typ im angegebenen Buffer ab.
     * @param buf
     * Der ByteBuffer in den die neuen Werte abgelegt werden sollen.
     * @param values
     * Werte die im Buffer abgelegt werden sollen.
     * @throws AttributeExeption
     * wird geworfen falls die Werte nicht vom selben Typ sind.
     */
    public abstract void put(ByteBuffer buf, Number[] values) throws AttributeExeption;

    public abstract void get(ByteBuffer buf, Number[] values);
}
