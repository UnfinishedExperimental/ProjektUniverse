/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.rendering.geometrie.unpacked;


/**
 * H�lt ein Mesh und das dazugeh�rige Material
 * @author Daniel Heinrich
 */
public class Model {
    private Mesh mesh;
    private Material mat;

    public Model(Mesh mesh, Material mat)
    {
        this.mesh = mesh;
        this.mat = mat;
    }

    public Material getMat()
    {
        return mat;
    }

    public Mesh getMesh()
    {
        return mesh;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Model other = (Model) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.mesh != null ? this.mesh.hashCode() : 0);
        hash = 31 * hash + (this.mat != null ? this.mat.hashCode() : 0);
        return hash;
    }
}
