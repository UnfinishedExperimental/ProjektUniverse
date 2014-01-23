/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.ressourcen.resmanagment;

import java.io.File;
import universetournament.client.util.io.obj.ObjObjektReader.Scale;

/**
 *
 * @author dheinrich
 */
public class ObjConfig
{
    private File file;
    private boolean centered;
    private Scale scale_type;
    private float scale;

    public ObjConfig() {
    }

    public ObjConfig(String file) {
        this(new File(file));
    }

    public ObjConfig(File file) {
        this(file, false, Scale.ABSOLUTE, 1f);
    }

    public ObjConfig(String file, boolean centered, Scale s2, float scale) {
        this(new File(file), centered, s2, scale);
    }

    public ObjConfig(File obj, boolean centered, Scale s2, float scale) {
        this.file = obj;
        this.centered = centered;
        this.scale_type = s2;
        this.scale = scale;
    }

    public boolean isCentered() {
        return centered;
    }

    public File getFile() {
        return file;
    }

    public Scale getScaleType() {
        return scale_type;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ObjConfig other = (ObjConfig) obj;
        if (this.file != other.file && (this.file == null || !this.file.equals(other.file)))
            return false;
        if (this.centered != other.centered)
            return false;
        if (this.scale_type != other.scale_type && (this.scale_type == null || !this.scale_type.equals(other.scale_type)))
            return false;
        if (this.scale != other.scale)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + (this.file != null ? this.file.hashCode() : 0);
        hash = 71 * hash + (this.centered ? 1 : 0);
        hash = 71 * hash + (this.scale_type != null ? this.scale_type.hashCode() : 0);
        hash = 71 * hash + Float.floatToIntBits(this.scale);
        return hash;
    }
}
