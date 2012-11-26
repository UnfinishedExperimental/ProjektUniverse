/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.unpacked;

import java.nio.ByteBuffer;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeExeption;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;

/**
 * Proxy Klasse um ein einzelens Vertex aus einem VertexBuffer editieren zu k�nnen
 * @author Daniel Heinrich
 */
public class Vertex
{

    private final VertexBuffer vb;
    private final int ind;

    Vertex(VertexBuffer vb, int ind)
    {
        this.vb = vb;
        this.ind = ind;
    }

    public void setAttribute(GenericVAttribute att, Number... values) throws AttributeExeption
    {        
        AttType t = att.getType();
        if (values.length < att.getSize())
            values = att.fillValues(values);
        
        if (values.length == att.getSize()) {
            ByteBuffer bb = vb.getBuffer(t);
            bb.position(att.getBOffset() + ind * vb.getAttrColl().getTypeSize(t) * t.getBSize());
            t.put(bb, values);
        } else
            throw new AttributeExeption("The Attribute demands " + att.getSize() + " values not " + values.length + "!");
    }

    public Number[] getAttribute(GenericVAttribute att)
    {
//        if(!att.isActive())
//            throw new AttributeExeption("Dieses Attribute ist im Shader nicht verf�gbar und deswegen nicht Aktiviert");

        Number[] values = new Number[att.getSize()];
        AttType t = att.getType();
        ByteBuffer bb = vb.getBuffer(t);
        bb.position(att.getBOffset() + ind * vb.getAttrColl().getTypeSize(t) * t.getBSize());
        t.get(bb, values);

        return values;
    }

    public int getInd() {
        return ind;
    }
}
