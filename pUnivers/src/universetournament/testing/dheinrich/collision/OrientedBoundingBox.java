/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.collision;

import java.nio.ByteBuffer;
import universetournament.client.rendering.geometrie.unpacked.*;
import universetournament.client.rendering.geometrie.vertexattributstuff.*;
import universetournament.client.rendering.ressourcen.resmanagment.ObjConfig;
import universetournament.client.rendering.shaders.*;
import universetournament.client.util.io.obj.OBJObjektReader;
import universetournament.shared.util.math.*;

/**
 *
 * @author dheinrich
 */
public class OrientedBoundingBox
{

    public OrientedBoundingBox(ModelObjekt model, SimpleShader s) {

        Mesh m = buildOverallMesh(model, s);
        Vec3[][] positions = getPositions(m, s.getPositionAttr());
        calcOBB(positions);
    }

    public Vec3[][] getPositions(Mesh mesh, GenericVAttribute position) {
        Vec3[][] positions = new Vec3[mesh.getIndexCount() / 3][3];
        VertexBuffer vb = mesh.getVertices();
        for (int i = 0; i < positions.length; ++i)
            for (int j = 0; j < 3; ++j) {
                Number[] pos = vb.getVertex(mesh.getIndicies()[i * 3 + j]).
                        getAttribute(
                        position);
                positions[i][j] = new Vec3((Float) pos[0], (Float) pos[1],
                                            (Float) pos[2]);
            }
        return positions;
    }

    public void calcOBB(Vec3[][] positions) {
        
        Vec3 center = new Vec3();

        for (int i = 0; i < positions.length; ++i)
            for (int j = 0; j < 3; ++j)
                center.add(positions[i][j], center);

        center.mult(1f / (positions.length * 3f), center);

        Vec3[][] relative = new Vec3[positions.length][3];
        for (int i = 0; i < positions.length; ++i)
            for (int j = 0; j < 3; ++j)
                relative[i][j] = positions[i][j].sub(center);

        double[] matrix = new double[9];
        for (int x = 0; x < 3; ++x)
            for (int y = 0; y < 3; ++y) {
                double val = 0;
                for (int i = 0; i < positions.length; ++i)
                    for (int j = 0; j < 3; ++j)
                        val += relative[i][j].getCoords()[x] * relative[i][j].
                                getCoords()[y];
                matrix[x + y * 3] = val / (positions.length * 3f);
            }

        Matrix c = new Matrix(matrix);
        System.out.println(c);
    }
    
    private Mesh buildOverallMesh(ModelObjekt model, SimpleShader s){
        int vbsize = 0;
        int indcount = 0;
        for (Model m : model.getModels()) {
            vbsize += m.getMesh().getVertexCount();
            indcount += m.getMesh().getIndexCount();
        }

        VertexBuffer sum = new VertexBuffer(s.getAttributs(), vbsize);
        for (int i = 0; i < vbsize; ++i)
            sum.addVertex();

        AttType att = s.getPositionAttr().getType();
        ByteBuffer dest = sum.getBuffer(att);
        dest.rewind();
        for (Model m : model.getModels()) {
            ByteBuffer bb = m.getMesh().getVertices().getBuffer(att);
            bb.rewind();
            dest.put(bb);
        }

        int[] indicies = new int[indcount];

        int id = 0;
        for (Model m : model.getModels())
            for (int i : m.getMesh().getIndicies())
                indicies[id++] = i;

        return new Mesh(indicies, sum, 0);
    }



    public static void main(String[] args) {
        SimpleShader s = new LigthingShader();
        OBJObjektReader or = new OBJObjektReader(s);
        ModelObjekt model = or.loadObjekt(new ObjConfig("Models/kiste.obj",
                                                     false,
                                                     OBJObjektReader.Scale2.DEPTH,
                                                     8));

        OrientedBoundingBox obb = new OrientedBoundingBox(model, s);
    }
}
