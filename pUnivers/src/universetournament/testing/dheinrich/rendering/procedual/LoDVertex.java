/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.rendering.procedual;

import universetournament.shared.util.math.*;

/**
 *
 * @author dheinrich
 */
public class LoDVertex
{
   private Vec3 pos, nor;
   private Vector texc;
   private int id;

    public LoDVertex(int id, Vec3 pos, Vec3 nor, Vector texc) {
        this.id = id;
        this.pos = pos;
        this.nor = nor;
        this.texc = texc;
    }

    public Vec3 getNor() {
        return nor;
    }

    public Vec3 getPos() {
        return pos;
    }

    public Vector getTexc() {
        return texc;
    }
}
