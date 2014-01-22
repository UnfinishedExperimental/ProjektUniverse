/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.util.io.obj;

/**
 *
 * @author Daniel Heinrich
 */
public class VertexIDs {
    private int position, normal, texcoord;

    public VertexIDs(int[] vd) {
        position = vd[0];
        texcoord = vd[1];
        normal = vd[2];
    }

    public VertexIDs(int position, int texcoord, int normal) {
        this.position = position;
        this.normal = normal;
        this.texcoord = texcoord;
    }

    public int getNormal() {
        return normal;
    }

    public int getPosition() {
        return position;
    }

    public int getTexcoord() {
        return texcoord;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final VertexIDs other = (VertexIDs) obj;
        if (this.position != other.position)
            return false;
        if (this.normal != other.normal)
            return false;
        if (this.texcoord != other.texcoord)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.position;
        hash = 89 * hash + this.normal;
        hash = 89 * hash + this.texcoord;
        return hash;
    }

}
