/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

/**
 * Konfiguriert eine 4x4 Matrix
 * @author Daniel Heinrich
 */
public class Matrix4 extends Matrix
{
    public enum Axis
    {
        X(0), Y(4), Z(8);
        private int offset;

        private Axis(int offset) {
            this.offset = offset;
        }
    }
    public static final double GRAD2RAD = Math.PI / 180.0;
    public static final double RAD2GRAD = 1 / GRAD2RAD;

    public Matrix4() {
        super(4);
    }

    public Matrix4(double[] mat) {
        super(mat);
        if (mat.length != 16)
            throw new ArrayIndexOutOfBoundsException(
                    "Es kann nur ein Array mit 16 Elementen �bergeben werden.");
    }

    /**
     * Builds a orthogonal Coordinate System, where the given Vector
     * represents the given Axis. Both other Axis are generated in a way that
     * no gurantee can be given how their orientation are.
     * @param dir
     * direction Vector of the given Axis
     * @param a
     * Axis of the given Vektor
     */
    public Matrix4(Vec3 dir, Axis a) {
        this();
        loadIdentity();

        Vec3 di = dir.normalize();
        setAxis(di, a);

        double[] d = di.getCoords();
        Vec3 dir2 = new Vec3(d[1], d[2], -d[0]);
        Vec3 dir3 = dir.cross(dir2);

        setAxis(dir2, Axis.values()[(a.ordinal() + 1) % 3]);
        setAxis(dir3, Axis.values()[(a.ordinal() + 2) % 3]);
    }

    /**
     * Initialize a Matrix which will transform a Source Space to a
     * Destination Space.
     * @param src
     * Matrix which describes the Source Coordinate Space
     * @param dst
     * Matrix which describes the Destination Coordinate Space
     */
    public Matrix4(Matrix4 src, Matrix4 dst) {
        this();
        Matrix4 tmp = new Matrix4();
        src.inverse(tmp);
        tmp.mult(dst, this);
    }

    public void setAxis(Vec3 x, Vec3 y, Vec3 z) {
        setAxis(x, Axis.X);
        setAxis(y, Axis.Y);
        setAxis(z, Axis.Z);
    }

    /**
     * translate the matrix in world space
     * @param vec
     * translation vector
     * @return
     * the manipulated matrix
     */
    public Matrix4 worldTranslate(Vec3 vec) {
        double[] coords = vec.getCoords();
        return worldTranslate(coords[0], coords[1], coords[2]);
    }

    /**
     * translate the matrix in world space
     * @param x
     * x-axis translation
     * @param y
     * y-axis translation
     * @param z
     * z-axis translation
     * @return
     * the manipulated matrix
     */
    public Matrix4 worldTranslate(double x, double y, double z) {
        double[] m = getMat();
        m[12] += x;
        m[13] += y;
        m[14] += z;

        return this;
    }

    public Matrix4 setWorldTranslate(Vec3 vec) {
        double[] coords = vec.getCoords();
        return setWorldTranslate(coords[0], coords[1], coords[2]);
    }

    public Matrix4 setWorldTranslate(double x, double y, double z) {
        double[] m = getMat();
        m[12] = x;
        m[13] = y;
        m[14] = z;

        return this;
    }

    /**
     * translate the matrix in matrix space
     * @param vec
     * translation vector
     * @return
     * the manipulated matrix
     */
    public Matrix4 translate(Vec3 vec) {
        double[] coords = vec.getCoords();
        return translate(coords[0], coords[1], coords[2]);
    }

    /**
     * translate the matrix in matrix space
     * @param x
     * x-axis translation
     * @param y
     * y-axis translation
     * @param z
     * z-axis translation
     * @return
     * the manipulated matrix
     */
    public Matrix4 translate(double x, double y, double z) {
        double[] m = getMat();
        m[12] += m[0] * x + m[4] * y + m[8] * z;
        m[13] += m[1] * x + m[5] * y + m[9] * z;
        m[14] += m[2] * x + m[6] * y + m[10] * z;
        m[15] += m[3] * x + m[7] * y + m[11] * z;
        return this;
    }

    /**
     * Rotates the Matrix with the given Eular Angles.
     * Rotation order is x,y,z.
     * @param angles
     * 3D Vector which holds the rotation angles for each axis
     * @return the same Matrix
     */
    public Matrix4 rotateEuler(Vec3 angles) {
        double[] c = angles.getCoords();
        return rotateEuler(c[0], c[1], c[2]);
    }

    /**
     * Rotates the Matrix with the given Eular Angles.
     * Rotation order is x,y,z.
     * @param x
     * @param y
     * @param z
     * @return the same Matrix
     */
    public Matrix4 rotateEuler(double x, double y, double z) {
        double xx = x * GRAD2RAD;
        double yy = y * GRAD2RAD;
        double zz = z * GRAD2RAD;

        double A = Math.cos(xx);
        double B = Math.sin(xx);
        double C = Math.cos(yy);
        double D = Math.sin(yy);
        double E = Math.cos(zz);
        double F = Math.sin(zz);
        double AD = A * D;
        double BD = B * D;

        double[] mat = new double[16];
        Matrix4 res = new Matrix4(mat);
        mat[0] = C * E;
        mat[4] = -C * F;
        mat[8] = D;

        mat[1] = BD * E + A * F;
        mat[5] = -BD * F + A * E;
        mat[9] = -B * C;
        mat[2] = -AD * E + B * F;
        mat[6] = AD * F + B * E;
        mat[10] = A * C;
        mat[15] = 1;

        this.mult(res, this);

        return this;
    }

    /**
     * Rotates the Matrix with the rotationen the given Quaternion describes
     * @param quat
     * @return the same Matrix
     */
    public Matrix4 rotate(Quaternion quat) {
        Matrix4 rot = quat.getRotationMatrix();
        this.mult(rot, this);
        return this;
    }

    /**
     * scales the matrix space
     * @param scale
     * scale factor vector
     * @return
     * the manipulated matrix
     */
    public Matrix4 scale(Vec3 scale) {
        double[] coords = scale.getCoords();
        return scale(coords[0], coords[1], coords[2]);
    }

    /**
     * scales the matrix space
     * @param scalex
     * x-axis scale factor
     * @param scaley
     * y-axis scale factor
     * @param scalez
     * z-axis scale factor
     * @return
     * the manipulated matrix
     */
    public Matrix4 scale(double scalex, double scaley, double scalez) {

        if (scalex == scaley && scalex == scalez)
            return scale(scalex);

        double[] m = getMat();

        if (scalex != 1)
            for (int i = 0; i < 3; ++i)
                m[i] *= scalex;
        if (scaley != 1)
            for (int i = 4; i < 7; ++i)
                m[i] *= scaley;
        if (scalez != 1)
            for (int i = 8; i < 11; ++i)
                m[i] *= scalez;

        return this;
    }

    /**
     * scales the matrix space evenly
     * @param scale
     * scale factor
     * @return
     * the manipulated matrix
     */
    public Matrix4 scale(double scale) {
        if (scale == 1)
            return this;

        double[] m = getMat();
        for (int i = 12; i < 16; ++i)
            m[i] /= scale;

        return this;
    }

    /**
     * @return total translation of matrix space relativ to world space.
     * Can be used as position of the matrix in world space.
     */
    public Vec3 getTranslation() {
        double[] mat = getMat();
        return new Vec3(mat[12], mat[13], mat[14]);
    }

    /**
     * @return a axis vector of the object space represented by this matrix (not normalized).
     */
    public Vec3 getAxis(Axis a) {
        double[] mat = getMat();
        return new Vec3(mat[a.offset], mat[a.offset + 1], mat[a.offset + 2]);
    }

    public void setAxis(Vec3 axis, Axis a) {
        copyTo(getMat(), axis.getCoords(), a.offset);
    }

    private void copyTo(double[] to, double[] from, int offset) {
        to[offset] = from[0];
        to[offset + 1] = from[1];
        to[offset + 2] = from[2];
    }

    public Vec3 getEularAngles() {
        double[] mat = getMat();
        double[] angles = new double[3];

        angles[1] = Math.asin(mat[8]);        /* Calculate Y-axis angle */

        double C = Math.cos(angles[1]);
        if (Math.abs(C) > 0.005) /* Gimball lock? */ {
            double trx = mat[10] / C;           /* No, so get X-axis angle */
            double tryy = -mat[9] / C;
            angles[0] = Math.atan2(tryy, trx);
            trx = mat[0] / C;            /* Get Z-axis angle */
            tryy = -mat[4] / C;
            angles[2] = Math.atan2(tryy, trx);
        } else /* Gimball lock has occurred */ {
            angles[0] = 0;                      /* Set X-axis angle to zero */
            double trx = mat[5];                 /* And calculate Z-axis angle */
            double tryy = mat[1];
            angles[2] = Math.atan2(tryy, trx);
        }

        /* return only positive angles in [0,360] */
        for (int i = 0; i < 3; ++i) {
            angles[i] *= RAD2GRAD;
            if (angles[i] < 0)
                angles[i] += 360;
        }

        return new Vec3(angles);
    }

    public Quaternion getRotation() {
        // from http://www.euclideanspace.com/maths/geometry/rotations
        //           /conversions/matrixToQuaternion/index.htm

        /* The max( 0, ... ) is just a safeguard against rounding error.
         * copysign takes the sign from the second term and sets the sign
         * of the first without altering the magnitude,
         * I don't know of a java equivalent.
         *
         * quaternion.w = sqrt( max( 0, 1 + m00 + m11 + m22 ) ) / 2;
         * quaternion.x = sqrt( max( 0, 1 + m00 - m11 - m22 ) ) / 2;
         * quaternion.y = sqrt( max( 0, 1 - m00 + m11 - m22 ) ) / 2;
         * quaternion.z = sqrt( max( 0, 1 - m00 - m11 + m22 ) ) / 2;
         * Q.x = _copysign( Q.x, m21 - m12 )
         * Q.y = _copysign( Q.y, m02 - m20 )
         * Q.z = _copysign( Q.z, m10 - m01 )
         */

        double[] mat = getMat();
//
//        double w = Math.sqrt(Math.max(0, 1 + mat[0] + mat[5] + mat[10])) * 0.5;
//        double x = Math.sqrt(Math.max(0, 1 + mat[0] - mat[5] - mat[10])) * 0.5;
//        double y = Math.sqrt(Math.max(0, 1 - mat[0] + mat[5] - mat[10])) * 0.5;
//        double z = Math.sqrt(Math.max(0, 1 - mat[0] - mat[0] + mat[10])) * 0.5;
//        x = Math.copySign(x, mat[6] - mat[9]);
//        y = Math.copySign(y, mat[8] - mat[2]);
//        z = Math.copySign(z, mat[1] - mat[4]);
//

        double tr = mat[0] + mat[5] + mat[10];
        double w, x, y, z;

        if (tr > 0) {
            double S = Math.sqrt(tr + 1.0) * 2; // S=4*w
            w = 0.25 * S;
            x = (mat[9] - mat[6]) / S;
            y = (mat[8] - mat[2]) / S;
            z = (mat[1] - mat[4]) / S;
        } else if ((mat[0] > mat[5]) & (mat[0] > mat[10])) {
            double S = Math.sqrt(1.0 + mat[0] - mat[5] - mat[10]) * 2; // S=4*x
            w = (mat[9] - mat[6]) / S;
            x = 0.25 * S;
            y = (mat[4] + mat[1]) / S;
            z = (mat[8] + mat[2]) / S;
        } else if (mat[5] > mat[10]) {
            double S = Math.sqrt(1.0 + mat[5] - mat[0] - mat[10]) * 2; // S=4*y
            w = (mat[8] - mat[2]) / S;
            x = (mat[4] + mat[1]) / S;
            y = 0.25 * S;
            z = (mat[6] + mat[9]) / S;
        } else {
            double S = Math.sqrt(1.0 + mat[10] - mat[0] - mat[5]) * 2; // S=4*z
            w = (mat[1] - mat[4]) / S;
            x = (mat[8] + mat[2]) / S;
            y = (mat[6] + mat[9]) / S;
            z = 0.25 * S;
        }

        return new Quaternion(w, new Vec3(x, y, z));
    }

    public void setRotation(Matrix4 rot) {
        double[] r = rot.getMat();
        double[] m = getMat();

        m[0] = r[0];
        m[4] = r[4];
        m[8] = r[8];
        m[1] = r[1];
        m[5] = r[5];
        m[9] = r[9];
        m[2] = r[2];
        m[6] = r[6];
        m[10] = r[10];
    }

    @Override
    public Matrix4 clone() {
        return new Matrix4(getMat().clone());
    }

    public static void main(String[] args) {
        Matrix4 m1 = new Matrix4();
        m1.loadIdentity();
        m1.translate(0, 1, 0);
        Matrix4 m2 = new Matrix4();
        m2.loadIdentity();
        m2.translate(1, 1, 0);

        Matrix4 m3 = new Matrix4(m1, m2);


        System.out.println(m3.getTranslation());
    }
}
