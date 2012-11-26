/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.hud;

/**
 *
 * @author dheinrich
 */
public class TextureAtlasElement
{
    transient private float woffset, hoffset, width, heigth;
    private String name;

    public TextureAtlasElement(float woffset, float hoffset, float width,
                               float heigth, String name) {
        this.woffset = woffset;
        this.hoffset = hoffset;
        this.width = width;
        this.heigth = heigth;
        this.name = name;
    }

    public float[] getTexCoord(float s, float t) {
        return new float[]{woffset + s * width, hoffset + t * heigth};
    }

    public float[] getRelativeTexCoord(float s, float t) {
        return new float[]{s * width, t * heigth};
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TextureAtlasElement other = (TextureAtlasElement) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 19 * hash + (this.name != null ? this.name.hashCode() : 0);
        return hash;
    }
}
