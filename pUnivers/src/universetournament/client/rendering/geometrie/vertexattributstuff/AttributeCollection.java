/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff;

import java.util.EnumSet;
import java.util.Iterator;
import javax.media.opengl.GL2GL3;

/**
 * Eine Liste an Vertex Attributen, die auf einander abgestimmt werden.
 * @author Daniel Heinrich
 */
public class AttributeCollection
{
    private GenericVAttribute[] atributes;
    private int[] size;
    private EnumSet<AttType> accatts = EnumSet.noneOf(AttType.class);

    /**
     * Eine Attribut Collection berechnet die einzelnen Offset der ihr
     * �bergebenen Attribute sowie der einzelnen Gr��en der ben�tigten Buffer je Typ.
     * @param atts
     * Liste von Attributen.
     */
    
    public AttributeCollection(GenericVAttribute... atts)
    {
        ini(atts);
    }

    private void ini(GenericVAttribute... atts){
        this.atributes = atts;
        size = new int[AttType.values().length];
        for (GenericVAttribute a : atributes) {
            a.setOffset(size[a.getType().ordinal()]);
            size[a.getType().ordinal()] += a.getSize();
            accatts.add(a.getType());
        }
        for (GenericVAttribute a : atributes)
            a.setStride(size[a.getType().ordinal()] * a.getType().getBSize());
        
    }

    protected void appendAttributes(GenericVAttribute... atts){
        GenericVAttribute[] a = new GenericVAttribute[atts.length+atributes.length];
        for(int i=0; i<atts.length; i++)
            a[i]=atts[i];
        for(int i=0; i<atributes.length; i++)
            a[i+atts.length]=atributes[i];
        ini(a);
    }

    public void enableAttributArrays(GL2GL3 gl){
        for(GenericVAttribute at: atributes)
            at.enableAttributeArray(gl);
    }

    public void disableAttributArrays(GL2GL3 gl){
        for(GenericVAttribute at: atributes)
            at.disableAttributeArray(gl);
    }

    public void bindAttributPointer(AttType type, GL2GL3 gl){
        for(GenericVAttribute at: atributes){
            if(type == at.getType()){
                at.setPointer(gl);
            }
        }
    }

    /**
     * Gibt an wie oft ein bestimmter Attribut Typ in allen Attribute vorkommt.
     * @param type
     * Der Typ der abgefragt werden soll.
     * @return
     * Gibt die Anzahl des vorkommens des Typs zur�ck.
     */
    public int getTypeSize(AttType type)
    {
        return size[type.ordinal()];
    }

    /**
     * @return
     * Gibt die Attribut der Collection zur�ck.
     */
    public GenericVAttribute[] getAttributes(){
        return atributes;
    }

    /**
     * @return
     * Gibt zur�ck wieviele verschiedene Datentypen die Attribute der Collection besitzten.
     */
    public int getAttTypesCount(){
        return accatts.size();
    }

    /**
     * @return
     * gibt ein Iterator �ber alle verwendeten Attribut Typen zur�ck.
     */
    public Iterator<AttType> getAttTypes(){
        return accatts.iterator();
    }
}
