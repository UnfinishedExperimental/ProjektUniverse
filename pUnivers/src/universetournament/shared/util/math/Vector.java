/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Repr�sentation eines 3 Dimensionalen Vektors
 * @author Daniel Heinrich
 */
public class Vector implements Serializable
{
    private double[] coords;

    public Vector(int dimension) {
        coords = new double[dimension];
    }

    public Vector(double... coord) {
        coords = coord;
    }

    public double[] getCoords() {
        return coords;
    }

    public int getDimension() {
        return coords.length;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("Vektor(");
        for (int i = 0; i < coords.length; ++i) {
            sb.append(coords[i]);
            sb.append(i != coords.length - 1 ? ", " : ')');

        }
        return sb.toString();
    }

    /**
     * normale Vektor addition
     * @param a
     * vektor der aufadiert werden soll
     * @param result
     * vektor in dem das ergebniss gespeichert werden soll
     * @return
     * zurückgabe des "result" vektors
     */
    public Vector add(Vector a, Vector result) {
        double[] summant = a.getCoords();
        double[] res = result.getCoords();

        for (int i = 0; i < coords.length; ++i)
            res[i] = coords[i] + summant[i];

        return result;
    }

    /**
     * normale Vektor addition
     * @param a
     * vektor der aufadiert werden soll
     * @return
     * gibt einen neuen Vektor zurück der das ergebniss hält
     */
    public Vector add(Vector a) {
        return add(a, new Vector(coords.length));
    }

    /**
     * skalare vektor addition
     * @param a
     * summant
     * @param result
     * vektor in dem das ergebniss gespeichert werden soll
     * @return
     * zurückgabe des "result" vektors
     */
    public Vector add(double a, Vector result) {
        double[] res = result.getCoords();

        for (int i = 0; i < coords.length; ++i)
            res[i] = coords[i] + a;

        return result;
    }

    public Vector sub(Vector a) {
        return sub(a, new Vector(coords.length));
    }

    /**
     * normale Vektor subtraktion
     * @param a
     * vektor der subtraiert werden soll
     * @param result
     * vektor in dem das ergebniss gespeichert werden soll
     * @return
     * zurückgabe des "result" vektors
     */
    public Vector sub(Vector a, Vector result) {
        double[] summant = a.getCoords();
        double[] res = result.getCoords();

        for (int i = 0; i < coords.length; ++i)
            res[i] = coords[i] - summant[i];

        return result;
    }

    public double dot(Vector a) {

        double[] summant = a.getCoords();
        double res = 0;
        for (int i = 0; i < getDimension(); ++i)
            res += coords[i] * summant[i];

        return res;
    }

    public Vector mult(double a) {
        return mult(a, new Vector(coords.length));
    }

    public Vector mult(double a, Vector result) {
        double[] res = result.getCoords();

        for (int i = 0; i < coords.length; ++i)
            res[i] = coords[i] * a;

        return result;
    }

    public Vector reflect(Vector normal) {
        return reflect(normal, new Vector(coords.length));
    }

    public Vector reflect(Vector normal, Vector result) {
        Vector sn = normal.mult(dot(normal) * 2);
        return sub(sn, result);
    }

    public Vector normalize() {
        return normalize(new Vector(coords.length));
    }

    public Vector normalize(Vector result) {
        double len = lenght();
        if (len != 0)
            mult(1f / len, result);
        return result;
    }

    public double lenght() {
        return (double) Math.sqrt(lenquad());
    }

    public double lenquad() {
        double sum = 0f;
        for (double f : coords)
            sum += f * f;
        return sum;
    }

    public double dist(Vector a) {
        return sub(a).lenght();
    }

    public double getAngle(Vector b) {
        Vector a = this.normalize();
        b = b.normalize();

        return a.getAngleBothNormalized(b);
    }

    public double getAngleBothNormalized(Vector b){
        return Math.acos(dot(b));
    }

    @Override
    public Vector clone() {
        return new Vector(coords.clone());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Vector other = (Vector) obj;
        if (!Arrays.equals(this.coords, other.coords))
            return false;
        return true;
    }
}

