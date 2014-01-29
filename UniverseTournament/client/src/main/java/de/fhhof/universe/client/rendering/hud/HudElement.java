/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.client.rendering.hud;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author dheinrich
 */
public class HudElement implements Serializable
{
    private String name;
    private float[] position, size;
    private TextureAtlasElement taelement;

    public HudElement(String name, float[] position, float[] size,
                      TextureAtlasElement taelement) {
        if(taelement == null)
            throw new NullPointerException("Es muss ein TexAtlass Element übergeben werden.");
        ini(position, size);
        this.taelement = taelement;
        this.name = name;
    }

    public HudElement(HudElement he) {
        taelement = he.taelement;
        name = he.name;
        ini(he.position, he.size);
    }

    private void ini(float[] position, float[] size){
        this.position = position;
        this.size = new float[]{Math.abs(size[0]), Math.abs(size[1])};
    }

    public float[] getSize() {
        return size;
    }

    public int getVertCount(){
        return 6;
    }

    public float[] getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TextureAtlasElement getTaElement() {
        return taelement;
    }

    public float[] convertTexCoord(float s, float t){
        return taelement.getTexCoord(s, t);
    }

    public HudVertex[] getVertice() {
        HudVertex[] vertice = new HudVertex[6];

        vertice[0] = new HudVertex(position, convertTexCoord(0, 1));
        vertice[1] = new HudVertex(new float[]{position[0] + getSize()[0],
                                             position[1] + getSize()[1]},
                                convertTexCoord(1, 0));
        vertice[2] = new HudVertex(new float[]{position[0],
                                             position[1] + getSize()[1]},
                                 convertTexCoord(0, 0));
        vertice[3] = vertice[0];
        vertice[4] = new HudVertex(new float[]{position[0] + getSize()[0],
                                             position[1]},
                                 convertTexCoord(1, 1));
        vertice[5] = vertice[1];

        return vertice;
    }

    public void setTaElement(TextureAtlasElement taelement) {
        this.taelement = taelement;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
       out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        ini(position, size);
    }

    @Override
    public String toString() {
        return name;
    }
}
