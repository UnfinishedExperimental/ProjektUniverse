/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

/**
 * Repr�sentation eines 3 Dimensionalen Vektors
 * @author Daniel Heinrich
 */
public class Vec3 extends Vector
{
    public Vec3() {
        super(3);
    }

    public Vec3(double[] coords) {
        this(coords[0], coords[1], coords[2]);
    }

    public Vec3(Vector v){
        this(v.getCoords());
    }

    public Vec3(double x, double y, double z) {
        super(x, y, z);
    }

    public Vec3 cross(Vec3 a) {
        return cross(a, new Vec3());
    }

    public Vec3 cross(Vec3 a, Vec3 result) {
        double[] mult = getCoords();
        double[] res = result.getCoords();
        double[] prod = a.getCoords();

        res[0] = mult[1] * prod[2] - mult[2] * prod[1];
        res[1] = mult[2] * prod[0] - mult[0] * prod[2];
        res[2] = mult[0] * prod[1] - mult[1] * prod[0];

        return result;
    }

    @Override
    public Vec3 add(Vector a) {
        return (Vec3) super.add(a, new Vec3());
    }

    @Override
    public Vec3 sub(Vector a) {
        return (Vec3) super.sub(a, new Vec3());
    }

    @Override
    public Vec3 mult(double a) {
        return (Vec3) super.mult(a, new Vec3());
    }

    public Vec3 reflect(Vec3 normal) {
        return (Vec3) reflect(normal, new Vec3());
    }

    @Override
    public Vec3 normalize() {
        return (Vec3) normalize(new Vec3());
    }

    public double getX() {
        return getCoords()[0];
    }

    public double getY() {
        return getCoords()[1];
    }

    public double getZ() {
        return getCoords()[2];
    }

    public void setCoords(double x, double y, double z){
        double[] c = getCoords();
        c[0] = x;
        c[1] = y;
        c[2] = z;
    }

    @Override
    public Vec3 clone() {
        double[] c = getCoords();
        return new Vec3(c[0], c[1], c[2]);
    }
}
