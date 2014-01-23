/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.client.util.io.obj;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import universetournament.client.rendering.ressourcen.resmanagment.ObjConfig;
import universetournament.client.rendering.geometrie.unpacked.Mesh;
import universetournament.client.rendering.geometrie.unpacked.Vertex;
import universetournament.client.rendering.geometrie.unpacked.VertexBuffer;
import universetournament.client.rendering.geometrie.unpacked.Material;
import universetournament.client.rendering.geometrie.unpacked.Model;
import universetournament.client.rendering.geometrie.unpacked.ModelObjekt;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeExeption;
import universetournament.client.rendering.shaders.LigthingShader;
import universetournament.client.rendering.shaders.SimpleShader;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.client.util.io.ObjektReader;

/**
 * Parser f�r das OBJ Modell Format
 * @author Daniel Heinrich
 */
public class ObjObjektReader extends ObjektReader
{
    public enum Scale
    {
        ABSOLUTE
        {
            protected double scalefaktor(ObjFile obj, double scale) {
                return scale;
            }
        },
        HEIGHT
        {
            protected double scalefaktor(ObjFile obj, double scale) {
                return scale / obj.getHeight();
            }
        },
        WIDTH
        {
            protected double scalefaktor(ObjFile obj, double scale) {
                return scale / obj.getWidth();
            }
        },
        DEPTH
        {
            protected double scalefaktor(ObjFile obj, double scale) {
                return scale / obj.getDepth();
            }
        };

        protected abstract double scalefaktor(ObjFile obj, double scale);
    }

    private static final Logger logger = Logger.getLogger(ObjObjektReader.class.getName());
    private SimpleShader simpleshader;

    public ObjObjektReader(SimpleShader attcoll) {
        this.simpleshader = attcoll;
    }

    @Override
    public ModelObjekt loadObjekt(File file) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ModelObjekt loadObjekt(ObjConfig ljob) {
        OBJFormatReader ofr = new OBJFormatReader(ljob.getFile());
        ObjFile obj = ofr.loadOBJ();
        if (ljob.isCentered())
            obj.center();
        obj.rescale(ljob.getScaleType().scalefaktor(obj, ljob.getScale()));
        Model[] models = loadModels(obj);
        ModelObjekt mo = new ModelObjekt(models, ljob.getFile().getName());
        int triscount = 0;
        int vertcount = 0;
        for (Model m : models) {
            triscount += m.getMesh().getIndexCount() / 3;
            vertcount += m.getMesh().getVertexCount();
        }
        logger.log(Level.INFO, ljob.getFile().getName() + "  ...loaded! (triscount: "
                + triscount + ", vertcount: " + vertcount+")");
        return mo;
    }

    // <editor-fold defaultstate="collapsed" desc="Vollst�ndiges OBJFile in Models konvertieren">
    private Model[] loadModels(ObjFile obj) {
        Model[] mo = new Model[obj.getMaterials().size()];

        Iterator<Material> iter = obj.getMaterials().iterator();
        for (int i = 0; iter.hasNext(); ++i) {
            Material mat = iter.next();
            mo[i] = new Model(loadMesh(obj, mat), mat);
        }

        return mo;
    }

    private Mesh loadMesh(ObjFile obj, Material mat) {
        Map<Integer, Integer> vmap = new Hashtable<Integer, Integer>();
        List<Face> faces = obj.getFaces(mat);

        Iterator<Face> iter = faces.iterator();
        int indexcount = 0;
        int vertexcount = 0;
        while (iter.hasNext()) {
            Face face = iter.next();
            vertexcount += face.getVertCount();
            indexcount += face.getTriCount() * 3;
        }

        VertexBuffer vb = new VertexBuffer(simpleshader.getAttributs(),
                                           vertexcount);
        int[] indicies = new int[indexcount];

        iter = faces.iterator();
        for (int j = 0; iter.hasNext();) {
            Face face = iter.next();
            int[] vi = new int[face.getVertCount()];
            for (int i = 0; i < vi.length; i++)
                vi[i] = getVertex(vb, face.getVertice()[i], obj, vmap);
            indicies[j++] = vi[0];
            indicies[j++] = vi[1];
            indicies[j++] = vi[2];
            if (vi.length == 4) {
                indicies[j++] = vi[2];
                indicies[j++] = vi[3];
                indicies[j++] = vi[0];
            }
        }

        return new Mesh(indicies, vb, GL.GL_TRIANGLES);
    }

    private int getVertex(VertexBuffer vb, VertexIDs ids, ObjFile obj,
                          Map<Integer, Integer> vmap) {
        double[] vert = null, nor = null, tex = null;

        int p = ids.getPosition();
        if (p != 0) {
            if (p < 0)
                p = obj.getVerticies().size() + p + 1;
            vert = obj.getVerticies().get(p - 1).getCoords();
        }

        int t = ids.getTexcoord();
        if (t != 0) {
            if (t < 0)
                t = obj.getTexcoords().size() + t + 1;
            tex = obj.getTexcoords().get(t - 1).getCoords();
            tex = new double[]{tex[0], tex[1]};
        }

        int n = ids.getNormal();
        if (n != 0) {
            if (n < 0)
                n = obj.getNormals().size() + n + 1;
            nor = obj.getNormals().get(n - 1).getCoords();
        }

        int hs = ids.hashCode();
        Integer vi = vmap.get(hs);
        if (vi == null) {
            vi = vb.addVertex();
            Vertex v = vb.getVertex(vi);
            try {
                if (vert != null)
                    v.setAttribute(simpleshader.getPositionAttr(), double2Float(
                            vert));
                if (nor != null && simpleshader instanceof LigthingShader)
                    v.setAttribute(
                            ((LigthingShader) simpleshader).getNormalAttr(), double2Float(
                            nor));
                if (tex != null && simpleshader instanceof StandartShader)
                    v.setAttribute(((StandartShader) simpleshader).
                            getTexCoordAttr(), double2Float(tex));
            } catch (AttributeExeption ex) {
                ex.printStackTrace();
            }
            vmap.put(hs, vi);
        }

        return vi;
    }

    private Float[] double2Float(double[] fs) {
        Float[] i = new Float[fs.length];
        for (int j = 0; j < i.length; j++)
            i[j] = (float)fs[j];
        return i;
    }// </editor-fold>
}
