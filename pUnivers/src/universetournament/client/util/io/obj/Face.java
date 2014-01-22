/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.util.io.obj;

import java.util.Arrays;

/**
 *
 * @author Daniel Heinrich
 */
public class Face {
    private VertexIDs[] vertice;

    public Face(VertexIDs... vertice) {
        this.vertice = vertice;
    }

    public VertexIDs[] getVertice() {
        return vertice;
    }

    public int getVertCount(){
        return vertice.length;
    }

    public int getTriCount(){
        return 1+getVertCount()-3;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Face other = (Face) obj;
        if (!Arrays.deepEquals(this.vertice, other.vertice))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Arrays.deepHashCode(this.vertice);
        return hash;
    }
}
