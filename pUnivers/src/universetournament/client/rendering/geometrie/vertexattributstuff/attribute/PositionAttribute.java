/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff.attribute;

import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;


/**
 * Konfiguriert ein Attribute f�r standart gebrauch als Positions Attribute.
 * @author Daniel Heinrich
 */

public class PositionAttribute extends GenericVAttribute
{
    public PositionAttribute(int attrpos)
    {
        super(attrpos, 4, AttType.FLOAT, false);
    }

    @Override
    public Number[] fillValues(Number[] values)
    {
        if(values.length >= getSize())
            return values;

        Number[] n = new Number[getSize()];
        for(int i=0; i<values.length; i++)
            n[i] = values[i];

        n[3] = 1.0f;

        return n;
    }

    @Override
    protected PositionAttribute clone()
    {
        return new PositionAttribute(getIndex());
    }
}
