/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff.attribute;

import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;


/**
 * Konfiguriert ein Attribute f�r standart gebrauch als Normalen Attribute.
 * @author Daniel Heinrich
 */
public class NormalAttribute extends GenericVAttribute
{
    
    public NormalAttribute(int attrpos)
    {
        super(attrpos, 3, AttType.FLOAT, false);
    }

    @Override
    protected NormalAttribute clone()
    {
        return new NormalAttribute(getIndex());
    }
}
