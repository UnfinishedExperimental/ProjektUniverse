/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.util.io.obj;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.shared.util.math.Vec3;
import universetournament.shared.util.math.Vector;

/**
 * DatenModell einer OBJ Datei
 * @author Daniel Heinrich
 */
public class ObjFile
{
    private List<Vec3> verticies = new ArrayList<Vec3>();
    private List<Vec3> normals = new ArrayList<Vec3>();
    private List<Vector> texcoords = new ArrayList<Vector>();
    private Vec3 min, max;
    private Hashtable<Material, List<Face>> subobjekts = new Hashtable<Material, List<Face>>();
    private List<Face> accfaces;

    public void addVertex(Vec3 pos) {
        if (verticies.size() == 0) {
            min = pos.clone();
            max = pos.clone();
        } else
            for (int i = 0; i < 3; i++)
                if (pos.getCoords()[i] < min.getCoords()[i])
                    min.getCoords()[i] = pos.getCoords()[i];
                else if (pos.getCoords()[i] > max.getCoords()[i])
                    max.getCoords()[i] = pos.getCoords()[i];

        verticies.add(pos);
    }

    public void addFace(Face face) {
        if(accfaces == null){
            accfaces = new LinkedList<Face>();
            subobjekts.put(new Material("_empty"), accfaces);
        }

        accfaces.add(face);
    }

    public void setAccMaterial(Material mat) {
        accfaces = subobjekts.get(mat);
        if(accfaces == null){
            accfaces = new LinkedList<Face>();
            subobjekts.put(mat, accfaces);
        }
    }

    public void center() {
        if(verticies.size() == 0)
            return;
        
        Vec3 shift = min.add(max);
        shift.mult(-0.5f, shift);
        
        for (Vec3 v : verticies)
            v.add(shift, v);
    }

    public void rescale(double scale) {
        if (scale == 1)
            return;

        for (Vec3 v : verticies)
            v.mult(scale, v);


        max.mult(scale, max);
        min.mult(scale, min);
    }

    public List<Face> getFaces(Material mat){
        return subobjekts.get(mat);
    }

    public Set<Material> getMaterials(){
        return subobjekts.keySet();
    }

    public List<Vec3> getNormals() {
        return normals;
    }

    public List<Vector> getTexcoords() {
        return texcoords;
    }

    public List<Vec3> getVerticies() {
        return verticies;
    }

    public double getWidth() {
        return max.getX() - min.getX();
    }

    public double getHeight() {
        return max.getY() - min.getY();
    }

    public double getDepth() {
        return max.getZ() - min.getZ();
    }

    public Vec3 getMin(){
        return min;
    }

    public Vec3 getMax(){
        return max;
    }
}
