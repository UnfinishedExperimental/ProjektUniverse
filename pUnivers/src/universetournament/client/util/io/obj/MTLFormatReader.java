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
import universetournament.client.rendering.geometrie.unpacked.Material;

/**
 * Parser f�r das MTL Material Format
 * @author Daniel Heinrich
 */
public class MTLFormatReader
{
    private Hashtable<String, Material> materials;
    private Material accmat;
    private FileReader fr;

    public MTLFormatReader(String path) throws FileNotFoundException {
        fr = new FileReader(new File(path));
    }

    public Hashtable<String, Material> loadMaterials() {
        if (materials == null) {
            materials = new Hashtable<String, Material>();
            try {
                BufferedReader br = new BufferedReader(fr);
                while (br.ready()) {
                    String[] s = br.readLine().split(" ", 2);
                    if (s.length == 2)
                        parseValue(s[0], s[1].trim().split(" "));
                }
                // <editor-fold defaultstate="collapsed" desc="catching stuff">
            } catch (IOException ex) {
            } finally {
                try {
                    fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }// </editor-fold>
            }

        }
        return materials;
    }

    public void parseValue(String type, String[] values) {
        type = type.trim();

        if (type.equals("Ns"))
            accmat.setSpecular_exponent(parseFloats(values)[0]);
        else if (type.equals("Ka"))
            accmat.setAmbient(parseFloats(values));
        else if (type.equals("Kd"))
            accmat.setDiffuse(parseFloats(values));
        else if (type.equals("Ks"))
            accmat.setSepcular(parseFloats(values));
        else if (type.equals("Ke"))
            accmat.setEmission(parseFloats(values));
        else if (type.equals("map_Ka"))
            accmat.setAmbientTex(mergestrings(values));
        else if (type.equals("map_Kd"))
            accmat.setDiffuseTex(mergestrings(values));
        else if (type.equals("map_Ks"))
            accmat.setSpecularTex(mergestrings(values));
        else if (type.equals("map_bump"))
            accmat.setNormalTex(mergestrings(values));
        else if (type.equals("map_d"))
            accmat.setAlphaTex(mergestrings(values));
        else if (type.equals("bump"))
            accmat.setBumbTex(values[0]);
        else if (type.equals("newmtl"))
            newMaterial(values[0]);
    }

    private String mergestrings(String[] values){
        String s = values[0];
        for(int i=1; i<values.length; i++)
            s += " " + values[i];
        return s;
    }

    private void newMaterial(String name) {
        Material mat;
        mat = materials.get(name);
        if (mat == null) {
            mat = new Material(name);
            materials.put(name, mat);
        }
        accmat = mat;
    }

    private float[] parseFloats(String[] values) {
        float[] vals = new float[values.length];
        for (int i = 0; i < values.length; i++)
            if (!values[i].isEmpty())
                vals[i] = Float.parseFloat(values[i]);
        return vals;
    }
}
