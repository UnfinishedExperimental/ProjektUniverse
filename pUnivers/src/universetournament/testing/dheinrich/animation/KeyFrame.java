/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.animation;

import universetournament.shared.util.math.Matrix4;
import universetournament.shared.util.math.Quaternion;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author some
 */
public class KeyFrame
{
    private Matrix4 mat;
    private int time;

    public KeyFrame(Matrix4 m, int time) {
        mat = m;
        this.time = time;
    }

    public KeyFrame(Vec3 pos, Quaternion rot, int time) {
        mat = new Matrix4();
        mat.loadIdentity();
        mat.translate(pos);
        mat.rotate(rot);

        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public Matrix4 getMatrixRep() {
        return mat;
    }
}
