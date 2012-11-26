/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff.attribute;

import universetournament.client.rendering.geometrie.vertexattributstuff.AttType;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;


/**
 * Konfiguriert ein Attribute f�r standart gebrauch als Texture Koordinaten Attribute.
 * @author Daniel Heinrich
 */
public class TexCoordAttribute extends GenericVAttribute
{

    public TexCoordAttribute(int attrpos)
    {
        super(attrpos, 2, AttType.FLOAT, false);
    }

    @Override
    protected TexCoordAttribute clone()
    {
        return new TexCoordAttribute(getIndex());
    }
}
