/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

/**
 *
 * @author some
 */
public class Quaternion
{
    private double w;
    private Vec3 vec;

    public Quaternion() {
        w = 1;
        vec = new Vec3();
    }

    public Quaternion(double w, Vec3 vec) {
        this.w = w;
        this.vec = vec.normalize();
    }

    /**
     * initializes a Quaternion with a rotation axis and rotation angle.
     * @param xyz
     * rotation vektor
     * @param phi
     * rotation angle in GRAD
     */
    public void setAxisAngle(Vec3 xyz, double phi) {
        ini(xyz, phi * Matrix4.GRAD2RAD);
    }

    /**
     * initializes a Quaternion with spherical rotation angles
     * @param latitude
     * @param longitude
     * @param angle
     */
    public void setSphericalAngles(double latitude, double longitude,
                                   double angle) {
        angle *= Matrix4.GRAD2RAD;
        double sin_a = Math.sin(angle / 2);
        double cos_a = Math.cos(angle / 2);
        double sin_lat = Math.sin(latitude);
        double cos_lat = Math.cos(latitude);
        double sin_long = Math.sin(longitude);
        double cos_long = Math.cos(longitude);

        double x = sin_a * cos_lat * sin_long;
        double y = sin_a * sin_lat;
        double z = sin_a * sin_lat * cos_long;

        vec = new Vec3(x, y, z);
        w = cos_a;
    }

    /**
     * initializes the quaternion wiht Eular Angles
     * @param pitch
     * X axis angle in GRAD
     * @param yaw
     * Y axis angle in GRAD
     * @param roll
     * Z axis angle in GRAD
     */
    public void setEularAngles(double pitch, double yaw, double roll) {
        //from http://etclab.mie.utoronto.ca/people/david_dir/GEMS/GEMS.html
        double pi = pitch * Matrix4.GRAD2RAD;
        double ya = yaw * Matrix4.GRAD2RAD;
        double ro = roll * Matrix4.GRAD2RAD;

        double sinPitch = Math.sin(pi * 0.5);
        double cosPitch = Math.cos(pi * 0.5F);
        double sinYaw = Math.sin(ya * 0.5F);
        double cosYaw = Math.cos(ya * 0.5F);
        double sinRoll = Math.sin(ro * 0.5F);
        double cosRoll = Math.cos(ro * 0.5F);
        double cosPitchCosYaw = cosPitch * cosYaw;
        double sinPitchSinYaw = sinPitch * sinYaw;

        double x = sinRoll * cosPitchCosYaw - cosRoll * sinPitchSinYaw;
        double y = cosRoll * sinPitch * cosYaw + sinRoll * cosPitch * sinYaw;
        double z = cosRoll * cosPitch * sinYaw - sinRoll * sinPitch * cosYaw;

        vec = new Vec3(x, y, z);
        vec.normalize(vec);
        w = cosRoll * cosPitchCosYaw + sinRoll * sinPitchSinYaw;
    }

    /**
     * initializes a Quaternion which maps a Vector on another.
     * @param old
     * Source Vector which gets mapped.
     * @param map2
     * Destination Vector which the source Vector gets mapped to.
     */
    public void mapVector(Vec3 old, Vec3 map2) {
        Vec3 o = old.normalize();
        Vec3 m = map2.normalize();
        
        double p = Math.acos(o.dot(m));
        ini(o.cross(m), p);
    }

    private void ini(Vec3 axis, double angle) {

        double[] c = axis.normalize().getCoords();

        double half = angle * 0.5;
        double sin_a = Math.sin(half);

        double x = c[0] * sin_a;
        double y = c[1] * sin_a;
        double z = c[2] * sin_a;

        vec = new Vec3(x, y, z);
        w = Math.cos(half);
    }

    public Quaternion conjugate() {
        return conjugate(new Quaternion());
    }

    public Quaternion conjugate(Quaternion res) {
        vec.mult(-1, res.vec);
        res.w = w;

        return res;
    }

    public double magnitute() {
        return Math.sqrt(magnituteSqr());//normaly conjungate . quaternion, but shortened version
    }

    public double magnituteSqr() {
        return vec.dot(vec) + w * w;//normaly conjungate . quaternion, but shortened version
    }

    public Quaternion normalize() {
        return normalize(new Quaternion());
    }

    public Quaternion normalize(Quaternion res) {
        double mag = 1. / magnitute();
        return mult(mag, res);
    }

    public Quaternion inverse() {
        return inverse(new Quaternion());
    }

    public Quaternion inverse(Quaternion res) {
        return conjugate(res);
//        return res.mul(1. / magnituteSqr(), res);
    }

    public Quaternion mult(double a) {
        return mult(a, new Quaternion());
    }

    public Quaternion mult(double a, Quaternion res) {
        vec.mult(a, res.vec);
        res.w = w * a;
        return res;
    }

    public Quaternion mult(Quaternion a) {
        return mult(a, new Quaternion());
    }

    public Quaternion mult(Quaternion q, Quaternion res) {
        //Qr = Q1.Q2 = ( w1.w2 - v1.v2, w1.v2 + w2.v1 + v1 x v2)
        /* w = w1w2 - x1x2 - y1y2 - z1z2
        x = w1x2 + x1w2 + y1z2 - z1y2
        y = w1y2 + y1w2 + z1x2 - x1z2
        z = w1z2 + z1w2 + x1y2 - y1x2*/

        double[] q1 = vec.getCoords();
        double[] q2 = q.vec.getCoords();

        double ww = w * q.w - q1[0] * q.w - q1[1] * q2[1] - q1[2] * q2[2];
        double x = w * q2[0] + q1[0] * q.w + q1[1] * q2[2] - q1[2] * q2[1];
        double y = w * q2[1] + q1[1] * q.w + q1[2] * q2[0] - q1[0] * q2[2];
        double z = w * q2[2] + q1[2] * q.w + q1[0] * q2[1] - q1[1] * q2[0];

        res.w = ww;
        res.vec.setCoords(x, y, z);
        return res;
    }

    public Vec3 mult(Vec3 a){
        Quaternion b = new Quaternion(0, a);
        mult(b, b);
        b.mult(inverse(), b);

        return b.vec;
    }

    public Quaternion getInterpolated(double delta){
        Quaternion res = clone();
        res.w *= delta;
        return res;
    }

    public double getPhi() {
        return Math.acos(w) * 2;
    }

    public Vec3 getRotationAxis() {
        Quaternion q = normalize();

        double sin_a = Math.sqrt(1. - w * w);
        if (Math.abs(sin_a) < 0.0005)
            sin_a = 1;

        return q.vec.mult(1. / sin_a);
    }

    public Matrix4 getRotationMatrix() {

        double[] mat = new double[16];
        Matrix4 res = new Matrix4(mat);
        double negw = -w;

        double[] co = vec.getCoords();
        double xx = co[0] * co[0];
        double xy = co[0] * co[2];
        double xz = co[0] * co[1];
        double xw = co[0] * negw;
        double yy = co[1] * co[2];
        double yz = co[1] * co[2];
        double yw = co[1] * negw;
        double zz = co[2] * co[2];
        double zw = co[2] * negw;

        mat[0] = 1 - 2 * (yy + zz);
        mat[1] = 2 * (xy - zw);
        mat[2] = 2 * (xz + yw);
        mat[4] = 2 * (xy + zw);
        mat[5] = 1 - 2 * (xx + zz);
        mat[6] = 2 * (yz - xw);
        mat[8] = 2 * (xz - yw);
        mat[9] = 2 * (yz + xw);
        mat[10] = 1 - 2 * (xx + yy);
        mat[15] = 1;

        return res;
    }

    @Override
    protected Quaternion clone() {
        Quaternion q = new Quaternion();
        q.vec = vec.clone();
        q.w = w;
        return q;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Quaternion other = (Quaternion) obj;
        if (this.w != other.w)
            return false;
        if (this.vec != other.vec && (this.vec == null || !this.vec.equals(
                other.vec)))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash =
        37 * hash + (int) (Double.doubleToLongBits(this.w) ^ (Double.
                doubleToLongBits(this.w) >>> 32));
        hash = 37 * hash + (this.vec != null ? this.vec.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Quaternion( Phi: "+getPhi()*Matrix4.RAD2GRAD+"°,  "+getRotationAxis()+')';
    }

    public static void main(String[] args) {
    }
}
