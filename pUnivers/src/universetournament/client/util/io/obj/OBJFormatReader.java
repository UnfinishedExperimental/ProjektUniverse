/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.util.io.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.shared.util.math.Vec3;
import universetournament.shared.util.math.Vector;

/**
 *
 * @author Daniel Heinrich
 */
public class OBJFormatReader
{
    private static final Logger logger = Logger.getLogger(OBJFormatReader.class.
            getName());
    private File file;
    private Pattern leer, slash;
    private Hashtable<String, Material> materials;

    public OBJFormatReader(String path) {
        this(new File(path));
    }

    public OBJFormatReader(File file) {
        this.file = file;
        materials = new Hashtable<String, Material>();
        leer = Pattern.compile(" ");
        slash = Pattern.compile("/");
    }

    public  ObjFile loadOBJ() {
        ObjFile obj = new ObjFile();
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                String[] s = leer.split(br.readLine(), 2);
                if (s.length == 2)
                    parseValue(obj, s[0], leer.split(s[1].trim()));
            }
            // <editor-fold defaultstate="collapsed" desc="catching stuff">
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }// </editor-fold>

        return obj;
    }

    public void parseValue(ObjFile obj, String type, String[] values) {
        if (type.equals("v"))
            obj.addVertex(new Vec3(parseDoubless(values)));
        else if (type.equals("vn"))
            obj.getNormals().add(new Vec3(parseDoubless(values)));
        else if (type.equals("vt"))
            obj.getTexcoords().add(new Vector(parseDoubless(values)));
        else if (type.equals("f"))
            obj.addFace(parseFace(values));
        else if (type.equals("usemtl"))
            useMaterial(obj, values);
        else if (type.equals("mtllib"))
            parseMtlLib(values);
    }

    private double[] parseDoubless(String[] values) {
        double[] vals = new double[values.length];
        for (int i = 0; i < values.length; i++)
            if (!values[i].isEmpty())
                vals[i] = Double.parseDouble(values[i]);
        return vals;
    }

    private Face parseFace(String[] values) {
        VertexIDs[] vertice = new VertexIDs[values.length];

        for (int i = 0; i < vertice.length; i++) {
            String[] ids = slash.split(values[i]);
            int[] vertex = new int[3];
            for (int j = 0; j < ids.length; j++)
                if (!ids[j].isEmpty())
                    vertex[j] = Integer.parseInt(ids[j]);
                else
                    vertex[j] = 0;
            vertice[i] = new VertexIDs(vertex);
        }

        return new Face(vertice);
    }

    private void parseMtlLib(String[] mtllib) {
        try {
            MTLFormatReader mtl = new MTLFormatReader(
                    file.getParent() + "/" + mtllib[0]);
            materials = mtl.loadMaterials();
        } catch (FileNotFoundException ex) {
            logger.log(Level.WARNING, "Eine Material Bibilothek konnte"
                    + " nicht gefunden werde."
                    + "\n" + ex.getLocalizedMessage());
        }
    }

    private void useMaterial(ObjFile obj, String[] values) {
        String name = values[0];
        Material mat = materials.get(name);
        if (mat == null) {
            mat = new Material(name);
            materials.put(name, mat);
        }

        obj.setAccMaterial(mat);
    }
}
