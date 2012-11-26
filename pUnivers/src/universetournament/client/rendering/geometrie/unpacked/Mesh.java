/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.geometrie.unpacked;

import java.util.Arrays;

/**
 * H�lt VertexBuffern Indicies und Primitiv Typ eines Models
 * @author Daniel Heinrich
 */
public class Mesh {
    private int[] indicies;     // Primitve indicies
    private VertexBuffer vertices;  // Vertexold Data
    private int primitiv_typ;

    public Mesh(int[] indicies, VertexBuffer vb, int primitiv_typ)
    {
        this.indicies = indicies;
        vertices = vb;
        this.primitiv_typ = primitiv_typ;
    }

    public int getVertexCount(){
        return getVertices().getVcount();
    }

    public int getIndexCount(){
        return getIndicies().length;
    }

    public int[] getIndicies()
    {
        return indicies;
    }

    public int getPrimitiv_typ()
    {
        return primitiv_typ;
    }

    public VertexBuffer getVertices()
    {
        return vertices;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 19 * hash + (this.indicies != null ? this.indicies.hashCode() : 0);
        hash = 19 * hash + (this.vertices != null ? this.vertices.hashCode() : 0);
        hash = 19 * hash + this.primitiv_typ;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Mesh other = (Mesh) obj;
        if (!Arrays.equals(this.indicies, other.indicies)) return false;
        if (this.vertices != other.vertices && (this.vertices == null || !this.vertices.equals(other.vertices)))
            return false;
        if (this.primitiv_typ != other.primitiv_typ) return false;
        return true;
    }

}
