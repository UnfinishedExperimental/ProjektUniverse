/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff.attribute;

import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;



/**
 * Konfiguriert ein Attribute f�r standart gebrauch als Color Attribute.
 * @author Daniel Heinrich
 */
public class ColorAttribute extends GenericVAttribute
{
    @Deprecated
    public ColorAttribute(int attrpos)
    {
        super(attrpos, 3, AttType.FLOAT, false);
    }

    @Override
    protected ColorAttribute clone()
    {
        return new ColorAttribute(getIndex());
    }
}
