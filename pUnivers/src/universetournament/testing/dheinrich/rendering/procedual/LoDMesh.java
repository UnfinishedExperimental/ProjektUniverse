/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.rendering.procedual;

import java.util.LinkedList;
import universetournament.client.rendering.geometrie.unpacked.*;

/**
 *
 * @author dheinrich
 */
public class LoDMesh
{
    private LinkedList<Triangle> triangles = new LinkedList<Triangle>();
    private VertexBuffer vbuffer;

    public LoDMesh(Mesh m) {
        vbuffer = m.getVertices();
        int tricount = m.getIndexCount() / 3;
        Triangle[] trianlges = new Triangle[tricount];
//        for (int i = 0; i < tricount; ++i)
//            trianlges[i] = new Triangle(getLoDVertex(i * 3),
//                                        getLoDVertex(i * 3 + 1),
//                                        getLoDVertex(i * 3 + 2));
        for(Triangle t: trianlges){
            for(int i=0; i<3; ++i){
            }
        }
    }

//    private LoDVertex getLoDVertex(int id) {
//
//        LigthingAttributes sa = (LigthingAttributes) vbuffer.getAttrColl();
//        Vertex v = vbuffer.getVertex(id);
//        Float[] p = (Float[]) v.getAttribute(sa.getPosition());
//        Float[] n = (Float[]) v.getAttribute(sa.getNormal());
//        Float[] t = (Float[]) v.getAttribute(sa.getTexcoord());
//
//        return new LoDVertex(id, new Vec3f(p[0], p[1], p[2]),
//                             new Vec3f(n[0], n[1], n[2]),
//                             new Vector(t[0], t[1]));
//    }
}
