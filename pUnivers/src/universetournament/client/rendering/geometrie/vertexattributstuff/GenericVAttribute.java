/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.geometrie.vertexattributstuff;

import javax.media.opengl.GL2GL3;

/**
 *
 * @author Daniel Heinrich
 */
public class GenericVAttribute
{
    private boolean normalized;
    private int index, size, offset, stride;
    private AttType type;
    private boolean active;

    /**
     * Erstellt ein Generisches Vertex Attribut.
     * @param index
     * Der Shader Index des Attributs.
     * Mittels "Shader.getAttrIndex(String name)" kann dieser bestimmt werden.
     * @param size
     * Anzahl an verschiedenen Werten, muss zwischen 1-4 liegen.
     * @param type
     * Definiert den Datentyp des Attributs.
     * @param normalized
     * Wenn true werden Attribute beim �bergeben normalisiert.
     * @throws AttributeExeption
     * Wird geworfen wenn size au�erhalb von [1,4] liegt.
     */
    public GenericVAttribute(int index, int size, AttType type,
                             boolean normalized) {
        if (size < 1 || size > 4)
            throw new RuntimeException(
                    "Ein Vertex Attribut muss eine Gr��en zwischen 1 und 4 haben, nicht:" + size);

        this.size = size;
        this.type = type;
        setIndex(index);
        this.normalized = normalized;

    }

    public void setPointer(GL2GL3 gl) {
        if (isActive())
            gl.glVertexAttribPointer(index, size,
                                     type.getGLConst(),
                                     normalized, stride, getBOffset());
    }

    public void enableAttributeArray(GL2GL3 gl) {
        if (isActive())
            gl.glEnableVertexAttribArray(getIndex());

    }

    public void disableAttributeArray(GL2GL3 gl) {
        if (isActive())
            gl.glDisableVertexAttribArray(getIndex());
    }

    public Number[] fillValues(Number[] values) {
        return values;
    }

    void setOffset(int i) {
        offset = i;
    }

    void setStride(int i) {
        stride = i;
    }

    private int getOffset() {
        return offset;
    }

    /**
     * @return
     * Gibt den Byte Offset des Attributs zur�ck.
     */
    public int getBOffset() {
        return offset * type.getBSize();
    }

    /**
     * @return
     * Gibt die Gr��e des Attributes in Bytes zur�ck.
     */
    public int getStride() {
        return stride;
    }

    /**
     * @return
     * Gibt den Datentyp des Attributs zur�ck.
     */
    public AttType getType() {
        return type;
    }

    /**
     * @return
     * Gibt die reine Gr��e des Attributes zur�ck.(anzahl an verschiedenen Werten)
     */
    public int getSize() {
        return size;
    }

    /**
     * @return
     * Gibt den Shader Index des Generischen Attributs zur�ck.
     */
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        active = index != -1;
    }

    /**
     * @return
     * Gibt an ob Attribut Werte normalisiert werden.
     */
    public boolean isNormalized() {
        return normalized;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    protected GenericVAttribute clone() {
        return new GenericVAttribute(getIndex(), getSize(), getType(),
                                     isNormalized());
    }
}
