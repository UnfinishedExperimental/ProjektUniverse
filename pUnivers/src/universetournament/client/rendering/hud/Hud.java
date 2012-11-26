/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.rendering.hud;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import javax.media.opengl.*;
import universetournament.client.rendering.geometrie.unpacked.Vertex;
import universetournament.client.rendering.geometrie.unpacked.VertexBuffer;
import universetournament.client.rendering.geometrie.vertexattributstuff.*;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.ressourcen.wrapper.ShaderProgramm;
import universetournament.client.rendering.shaders.HudShader;
import universetournament.shared.util.io.UTXMLWriter;

/**
 *
 * @author dheinrich
 */
public class Hud implements Serializable
{
    private final static Logger logger = Logger.getLogger(Hud.class.getName());
    private final List<HudElement> elements = new LinkedList<HudElement>();
    private TextureAtlas texatlas;
    transient private int buffer = -1;
    transient private HudShader hudshader;

    public Hud(TextureAtlas texatlas) {
        this.texatlas = texatlas;
        hudshader = new HudShader();
        RessourcesLoader.getInstance().loadShader(hudshader, "Hud.vert",
                                                  "Hud.frag");
    }

    public void addElement(HudElement el) {
//        if (texatlas.getElement(el.getTaElement().getFile()) != null)
        elements.add(el);
    }

    public void switchElement(HudElement old, HudElement newe) {
        int index = elements.lastIndexOf(old);
        removeElement(old);
        elements.add(index, newe);
    }

    public void removeElement(HudElement el) {
        elements.remove(el);
    }

    public List<HudElement> getElements() {
        return elements;
    }

    public HudElement getElementByName(String name){
        for(HudElement he:elements)
            if(he.getName().equals(name))
                return he;
        logger.log(Level.WARNING, "Couldn't find Hud Element by Name: "+name);
        return null;
    }

    public TextureAtlas getTexatlas() {
        return texatlas;
    }

    private void createBuffer() {
        int[] tmp = new int[1];
        GL gl = GLContext.getCurrentGL().getGL2GL3();
        gl.glGenBuffers(1, tmp, 0);
        buffer = tmp[0];
    }

    private void buildBuffer(GL2GL3 gl) {
        int size = 0;
        for (HudElement he : elements)
            size += he.getVertCount();

        VertexBuffer vb = new VertexBuffer(hudshader.getAttributs(), size);
        for (HudElement he : elements)
            try {
                for (HudVertex vert : he.getVertice()) {
                    Vertex v = vb.getVertex(vb.addVertex());
                    v.setAttribute(hudshader.getPositionAttr(),
                                   float2Float(vert.getPosition()));
                    v.setAttribute(hudshader.getTexCoordAttr(), float2Float(
                            vert.getTexcoord()));
                }
            } catch (AttributeExeption ex) {
                ex.printStackTrace();
            }
//        float[] arr = new float[elements.size() * 6*4];
//        vb.getBuffer(AttType.FLOAT).asFloatBuffer().get(arr);
//        vb.getBuffer(AttType.FLOAT).rewind();
//        for (int i = 0; i < arr.length; i += 4) {
//            for (int j = 0; j < 4; ++j)
//                System.out.print(arr[i + j] + ", ");
//            System.out.println();
//        }
//        vb.getBuffer(AttType.FLOAT).rewind();
//            System.out.println("--------");

        gl.glBufferData(GL.GL_ARRAY_BUFFER, vb.getVcount() * hudshader.
                getAttributs().getTypeSize(AttType.FLOAT) * AttType.FLOAT.
                getBSize(),
                        vb.getBuffer(AttType.FLOAT), GL.GL_STATIC_DRAW);


    }

    public void render() {
        ShaderProgramm sp = hudshader.getShaderprogramm();
        if (sp == null)
            return;
        sp.use();
        try {
            hudshader.getDiffuse().
                    bindTexture(texatlas.getTexContainer().getTexture());
        } catch (InstantiationException ex) {
            logger.log(Level.SEVERE, "HUD konnte nicht gerendert werden."
                    + "(TextureAtlass Texture nicht vorhanden", ex);
            return;
        }

        if (buffer < 0)
            createBuffer();
        GL2GL3 gl = GLContext.getCurrentGL().getGL2GL3();
        AttributeCollection pattr = hudshader.getAttributs();
        pattr.enableAttributArrays(gl);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, buffer);
        synchronized (elements) {
            buildBuffer(gl);

            int size = 0;
            for (HudElement he : elements)
                size += he.getVertCount();

            pattr.bindAttributPointer(AttType.FLOAT, gl);

            gl.glDepthMask(false);
            gl.glEnable(gl.GL_BLEND);
            gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
            gl.glDrawArrays(gl.GL_TRIANGLES, 0, size);
            gl.glDisable(gl.GL_BLEND);
            gl.glDepthMask(true);
        }

        pattr.disableAttributArrays(gl);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
    }

    public HudShader getShader() {
        return hudshader;
    }

    private Float[] float2Float(float[] fs) {
        Float[] i = new Float[fs.length];
        for (int j = 0; j < i.length; j++)
            i[j] = fs[j];
        return i;
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        buffer = -1;
        hudshader = new HudShader();
        RessourcesLoader.getInstance().loadShader(hudshader, "Hud.vert",
                                                  "Hud.frag");
        for (HudElement he : elements)
            he.setTaElement(texatlas.getElement(he.getTaElement().getName()));
    }

    public static void main(String[] args) {
        TextureAtlas ta = new TextureAtlas("Textures/hud/hud00.xml");
        Hud hud = new Hud(ta);
        hud.addElement(new HudElement("asd", new float[]{0, 0},
                                      new float[]{0, 0}, new TextureAtlasElement(
                1, 2, 3, 4, "a")));
        hud.addElement(new DynamicHudElement(DynamicHudElement.Direction.BOTH,
                                             DynamicHudElement.Alligment.LEFT_BOTTOM,
                                             true, "asd2", new float[]{0, 0},
                                             new float[]{0, 0}, new TextureAtlasElement(
                1, 2, 3, 4, "b")));
        UTXMLWriter w = new UTXMLWriter();
        w.write(new File("testhud.xml"), hud);
//        UTXMLReader r = new UTXMLReader();
//        r.read(Hud.class, new File("data/hud.xml"));
    }
}
