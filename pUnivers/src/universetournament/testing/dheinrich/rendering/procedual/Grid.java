/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.rendering.procedual;

import javax.media.opengl.GL;
import universetournament.client.rendering.geometrie.unpacked.Mesh;
import universetournament.client.rendering.geometrie.unpacked.Vertex;
import universetournament.client.rendering.geometrie.unpacked.VertexBuffer;
import universetournament.client.rendering.geometrie.vertexattributstuff.AttributeExeption;
import universetournament.client.rendering.geometrie.vertexattributstuff.GenericVAttribute;
import universetournament.client.rendering.shaders.SimpleShader;

/**
 *
 * @author some
 */
public class Grid extends Mesh
{
    private int rows, columns;
    private GenericVAttribute pos;

    public Grid(SimpleShader shader, int rows, int columns) {
        super(new int[rows * columns * 6], new VertexBuffer(
                shader.getAttributs(), 4 * rows * columns),
              GL.GL_TRIANGLES);

        this.rows = rows;
        this.columns = columns;
        pos = shader.getPositionAttr();

        ini();
    }

    private void ini(){
        VertexBuffer vb = getVertices();
        int[] indicies = getIndicies();
        int offset = 0;
        for (int x = 0; x < columns; ++x)
            for (int y = 0; y < rows; ++y) {
                int[] ind = addQuad(x, y, vb);
                copyArray(ind, indicies, offset);
                offset += ind.length;
            }
    }

    private void copyArray(int[] from, int[] to, int offset) {
        for (int i = 0; i < from.length; ++i)
            to[offset + i] = from[i];
    }

    private int[] addQuad(int x, int y, VertexBuffer vb) {
        float width = 1f / columns;
        float heigth = 1f / rows;
        float xpos = x * width;
        float ypos = y * heigth;

        int[] ind = new int[6];

        for (int i = 0; i < 4; ++i) {
            ind[i] = vb.addVertex();
            Vertex v = vb.getVertex(ind[i]);
            try {
                v.setAttribute(pos, xpos + width * (i % 3 != 0 ? 1 : 0),
                               ypos + heigth * (i > 1 ? 1 : 0), 0f, 1f);
            } catch (AttributeExeption ex) {
                ex.printStackTrace();
            }
        }

        ind[4] = ind[0];
        ind[5] = ind[2];

        return ind;
    }
}
