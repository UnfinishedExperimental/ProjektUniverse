/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.util.math;

/**
 *
 * @author Daniel Heinrich
 */
public class ViewMatrix extends Matrix4{

    public ViewMatrix() {
    }

    public ViewMatrix(double[] modelview) {
        super(modelview);
    }

    public Vec3 getViewDirection(){
        Vec3 res = new Vec3(0, 0,1);
        res = new Vec3(this.getMinor(3, 3).inverse().mult(res));
        return res;
    }
/**
     * Implementation der aus GLUT bekannten lookAt funktion
     */
    public void lookAt(Vec3 eye, Vec3 center, Vec3 up) {
        Vec3 forward, side, up2;
        Matrix4 matrix = new Matrix4();

        forward = new Vec3();
        center.sub(eye, forward);
        forward.normalize(forward);
        side = new Vec3();
        forward.cross(up, side);
        side.normalize(side);
        up2 = new Vec3();
        side.cross(forward, up2);

        double[] mat = matrix.getMat();

        mat[0] = side.getX();
        mat[1] = up2.getX();
        mat[2] = -forward.getX();
        mat[3] = 0.0f;
        mat[4] = side.getY();
        mat[5] = up2.getY();
        mat[6] = -forward.getY();
        mat[7] = 0.0f;
        mat[8] = side.getZ();
        mat[9] = up2.getZ();
        mat[10] = -forward.getZ();
        mat[11] = 0.0f;
        mat[12] = 0.0f;
        mat[13] = 0.0f;
        mat[14] = 0.0f;
        mat[15] = 1.0f;

        loadIdentity();
        mult(matrix, this);
        translate(eye);
    }

    @Override
    public ViewMatrix clone() {
        ViewMatrix m = new ViewMatrix();
        m.setMat(getMat().clone());
        return m;
    }
}
