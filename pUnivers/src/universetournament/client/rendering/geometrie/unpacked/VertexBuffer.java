/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.unpacked;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeCollection;

/**
 * DatenModel um Vertex Attribute einse Modelles zu halten
 * @author Daniel Heinrich
 */
public class VertexBuffer
{
    private ByteBuffer[] buffers = new ByteBuffer[AttType.values().length];
    private AttributeCollection attributes;
    private int size;
    private int vcount = 0;

    public VertexBuffer(AttributeCollection atts, int size) {
        this.size = size;
        attributes = atts;
        for (AttType t : AttType.values())
            if (attributes.getTypeSize(t) > 0) {
                buffers[t.ordinal()] = t.createBuffer(
                        attributes.getTypeSize(t) * size);
                buffers[t.ordinal()].limit(0);
            }
    }

    public Vertex getVertex(int ind) {
        if (ind < getVcount())
            return new Vertex(this, ind);
        else
            throw new ArrayIndexOutOfBoundsException(
                    "Vertexcount: " + getVcount() + ", requested Vertex: " + ind);
    }

    public int addVertex() {
        if (vcount < size) {
            for (AttType t : AttType.values())
                if (attributes.getTypeSize(t) > 0)
                    buffers[t.ordinal()].limit(buffers[t.ordinal()].limit()
                            + attributes.getTypeSize(t) * t.getBSize());
            return vcount++;
        } else
            throw new BufferOverflowException();
    }

    public ByteBuffer getBuffer(AttType at) {
        if (buffers[at.ordinal()] == null)
            buffers[at.ordinal()] = null;
        buffers[at.ordinal()].rewind();
        return buffers[at.ordinal()];
    }

    public AttributeCollection getAttrColl() {
        return attributes;
    }

    public int getSize() {
        return size;
    }

    public int getVcount() {
        return vcount;
    }

    public int remaining() {
        return getSize() - getVcount();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VertexBuffer other = (VertexBuffer) obj;
        if (!Arrays.deepEquals(this.buffers, other.buffers))
            return false;
        if (this.attributes != other.attributes && (this.attributes == null || !this.attributes.
                equals(other.attributes)))
            return false;
        if (this.size != other.size)
            return false;
        if (this.vcount != other.vcount)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 29 * hash + Arrays.deepHashCode(this.buffers);
        hash =
        29 * hash + (this.attributes != null ? this.attributes.hashCode() : 0);
        hash = 29 * hash + this.size;
        hash = 29 * hash + this.vcount;
        return hash;
    }
}
