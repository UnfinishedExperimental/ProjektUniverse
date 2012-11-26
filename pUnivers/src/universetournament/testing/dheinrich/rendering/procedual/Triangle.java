/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.rendering.procedual;

/**
 *
 * @author dheinrich
 */
public class Triangle
{
    private LoDVertex[] points; //3
    private Triangle[] edge; //3
    private Triangle parent;
    private Triangle[] child; //4

    public Triangle(LoDVertex... points) {
        child = new Triangle[4];
        edge = new Triangle[3];
        this.points = points;
    }
//
//    //http://www.gamedev.net/reference/articles/article2074.asp
//    public Triangle[] split() {
//        if (parent != null)
//            for (int i = 0; i < 3; i++)
//                if (edge[i] == null) {
//                    Triangle t = parent.edge[i];
//                    if (t != null)
//                        t.split();
//                }
//
//        LoDVertex[] np = new LoDVertex[3];
//        for (int i = 0; i < 3; i++) {
//            Vec3f pos = points[(i + 1) % 3].getPos().add(points[i].getPos());
//            pos.mul(0.5f, pos);
//            Vector tex = points[(i + 1) % 3].getTexc().add(points[i].getTexc());
//            tex.mul(0.5f, tex);
//            Vec3f n = points[(i + 1) % 3].getNor().add(points[i].getNor());
//            n.mul(0.5f, n);
//            np[i] = new LoDVertex(pos, n, tex);
//        }
//
//        for (int i = 0; i < 3; i++)
//            //here, even if were not raising the point to the radius+detail,
//            //prepare the texturing, colouring etc as if we were
//            //...
//            if (edge[i].hasChildren()) {
//                np[i].getPos().normalize();
//                // np[i].mul(radius, np[i]);
//
//                //and tell neighboring children sharing point they can raise from midpoint
//                int it0 = edge[i].sharedEdge(this);
//                int it1 = (it0 + 1) % 3;
//                int it3 = (it1 + 1) % 3;
//                edge[i].child[it0].points[it1] = np[i];
//                edge[i].child[it1].points[it0] = np[i];
//                edge[i].child[3].points[it3] = np[i];
//            }
//
//        child[0] = new Triangle(points[0], np[0], np[2]);
//        child[1] = new Triangle(np[0], points[1], np[1]);
//        child[2] = new Triangle(np[2], np[1], points[2]);
//        child[3] = new Triangle(np[1], np[2], np[0]);
//
//        for (int i = 0; i < 4; i++)
//            child[i].parent = this;
//
//        child[0].edge[1] = child[3];
//        child[1].edge[2] = child[3];
//        child[2].edge[0] = child[3];
//
//        child[3].setEdges(child[2], child[0], child[1]);
//
//        for (int i = 0; i < 3; i++)
//            if (edge[i] != null) //non-closed surface trap
//                if (edge[i].hasChildren()) {
//                    //triangle has children we can point our children too.
//                    int j = (i + 1) % 3;
//                    int sharedEdge = edge[i].sharedEdge(this);
//                    if (sharedEdge != -1) {
//                        int k = (sharedEdge + 1) % 3;
//                        child[i].edge[i] = edge[i].child[k];
//                        child[j].edge[i] = edge[i].child[sharedEdge];
//                        edge[i].child[sharedEdge].edge[sharedEdge] = child[j];
//                        edge[i].child[k].edge[sharedEdge] = child[i];
//                    }
//                }
//
//        return child;
//    }
//
//    private void setEdges(Triangle e1, Triangle e2, Triangle e3) {
//        edge[0] = e1;
//        edge[1] = e2;
//        edge[2] = e3;
//    }
//
//    public boolean hasChildren() {
//        return child[0] != null;
//    }
//
//    public boolean check4Edges(Triangle t){
//        for(int i=0; i<3; ++i)
//
//    }
//
//    private int hasVertex(LoDVertex v){
//        for(int i=0; i<3; ++i)
//            if(points[i].g)
//    }
//
//    public int sharedEdge(Triangle t) {
//        for (int i = 0; i < 3; ++i)
//            if (edge[i] == t)
//                return i;
//        return -1;
//    }
//
//    public Triangle[] getEdges() {
//        return edge;
//    }
}
