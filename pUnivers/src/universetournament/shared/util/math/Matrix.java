/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Quadratische Matrix beliebiger Dimension
 * @author Daniel Heinrich
 */
public class Matrix implements Serializable
{
    private double[] matrix;
    private int dimension;

    public Matrix(int dim) {
        dimension = dim;
        matrix = new double[dim * dim];
    }

    public Matrix(double[] mat) {
        dimension = (int) Math.sqrt(mat.length);
        if (dimension * dimension != mat.length)
            throw new ArrayIndexOutOfBoundsException(
                    "Es werden nur quadratische Matrizen akzeptiert!");
        matrix = mat;
    }

    /**
     * Laed die Einheits Matrix
     */
    public Matrix loadIdentity() {
        int mod = dimension + 1;
        for (int i = 0; i < matrix.length; i++)
            matrix[i] = i % mod == 0 ? 1 : 0;
        return this;
    }

    /**
     * Multipliziert die Matrix mit einem Scalaren Faktor
     */
    public Matrix mult(double v) {
        return mult(v, new Matrix(dimension));
    }

    /**
     * Multipliziert die Matrix mit einem Scalaren Faktor
     */
    public Matrix mult(double v, Matrix result) {
        int qdim = dimension * dimension;

        for (int i = 0; i < qdim; i++)
            result.matrix[i] = matrix[i] * v;

        return result;
    }

    public Vector mult(Vector mul) {
        double[] result = new double[dimension];
        double[] a = mul.getCoords();
        double[] b;
        if (a.length == 3)
            b = new double[]{a[0], a[1], a[2], 1};
        else
            b = a;

        for (int i = 0; i < dimension; ++i)
            for (int j = 0; j < dimension; ++j)
                result[i] += b[j] * matrix[i + j * dimension];

        return new Vector(result);
    }

    public Matrix mult(Matrix multi) {
        return mult(multi, new Matrix(dimension));
    }

    /**
     * Einfache Matrizen Multiplikation
     */
    public Matrix mult(Matrix multi, Matrix result) {
        double[] temp = new double[dimension * dimension];
        double[] mult = multi.matrix;

        int jd, ijd;
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                jd = j * dimension;
                ijd = i + jd;
                for (int k = 0; k < dimension; k++)
                    temp[ijd] += matrix[i + k * dimension] * mult[k + jd];
            }

        result.setMat(temp);

        return result;
    }

    public Matrix add(Matrix sum) {
        return add(sum, new Matrix(dimension));
    }

    public Matrix add(Matrix sum, Matrix result) {
        double[] res = result.matrix;
        double[] su = sum.matrix;

        for (int i = 0; i < dimension; ++i)
            res[i] = matrix[i] + su[i];

        return result;
    }

    public Matrix sub(Matrix subt) {
        return sub(subt, new Matrix(dimension));
    }

    public Matrix sub(Matrix subt, Matrix result) {
        double[] res = result.matrix;
        double[] su = subt.matrix;

        for (int i = 0; i < dimension; ++i)
            res[i] = matrix[i] - su[i];

        return result;
    }

    public Matrix pow(int power) {
        Matrix res = new Matrix(power);
        res.loadIdentity();
        Matrix m;

        if(power < 0){
            power *= -1;
            m = this.inverse();
        }else
            m = this.clone();

        while (power > 0) {
            if (power % 2 == 1)
                res.mult(m, res);
            power /= 2;
            if(power==0)
                break;
            m.mult(m, m);
        }

        return res;
    }

    public Matrix transpose() {
        return transpose(new Matrix(dimension));
    }

    /**
     * Transponiert die Matrix
     */
    public Matrix transpose(Matrix result) {
        double[] f = new double[dimension * dimension];
        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++)
                f[i + j * dimension] = matrix[j + i * dimension];

        result.setMat(f);

        return result;
    }

    public Matrix adjunkt() {
        return adjunkt(new Matrix(dimension));
    }

    /**
     * Ersetzt die Matrix mit ihrer Adjunkten
     */
    public Matrix adjunkt(Matrix result) {
        double[] f = new double[dimension * dimension];
        for (int i = 0, n = 1; i < dimension; i++) {
            for (int j = 0; j < dimension; j++, n *= -1)
                f[j + i * dimension] =
                getMinor(i, j).getDeterminate() * n;
            if (dimension % 2 == 0)
                n *= -1;
        }

        result.setMat(f);
        result.transpose(result);

        return result;
    }

    public Matrix inverse() {
        return inverse(new Matrix(dimension));
    }

    /**
     * Ersetzt die Matrix mit ihrer Inversen
     */
    public Matrix inverse(Matrix result) {
        double det = getDeterminate();
        adjunkt(result);
        result.mult(1f / det, result);

        return result;
    }

    public double getDeterminate() {
        if (dimension > 1) {
            double det = 0;
            for (int j = 0, n = 1; j < dimension; j++, n *= -1)
                det += getMinor(0, j).getDeterminate() * matrix[j] * n;
            return det;
        } else
            return matrix[0];
    }

    /**
     * Der Matrix wird jeweils eine Spalte und Zeile gestrichen und die
     * resultierende Matrix zur�ckgegeben
     */
    public Matrix getMinor(int skipx, int skipy) {
        int dim = dimension - 1;
        double[] minor = new double[dim * dim];
        for (int i = 0; i < dimension - 1; i++)
            for (int j = 0; j < dimension - 1; j++) {
                int y = j + (j >= skipy ? 1 : 0);
                int x = i + (i >= skipx ? 1 : 0);
                minor[j + i * dim] = matrix[y + x * dimension];
            }
        return new Matrix(minor);
    }

    public final double[] getMat() {
        return matrix;
    }

    public float[] getFloatArray(){
        float[] ret = new float[matrix.length];
        for(int i=0; i<ret.length; ++i)
            ret[i] = (float) matrix[i];
        return ret;
    }

    public Vector[] getRows() {
        Vector[] rows = new Vector[dimension];
        for (int i = 0; i < dimension; ++i)
            rows[i] = getRow(i);
        return rows;
    }

    public Vector[] getColumns() {
        Vector[] columns = new Vector[dimension];
        for (int i = 0; i < dimension; ++i)
            columns[i] = getColumn(i);
        return columns;
    }

    public Vector getRow(int index) {
        Vector res = new Vector(dimension);
        double[] coor = res.getCoords();
        for (int i = 0; i < dimension; ++i)
            coor[i] = matrix[index + dimension * i];
        return res;
    }

    public Vector getColumn(int index) {
        Vector res = new Vector(dimension);
        double[] coor = res.getCoords();
        for (int i = 0; i < dimension; ++i)
            coor[i] = matrix[i + dimension * index];
        return res;
    }

    public void setMat(double[] mat) {
        if (mat.length != dimension * dimension)
            throw new ArrayIndexOutOfBoundsException(
                    "Die Matrize kann nur mit einem Array mit der selben Elementen anzahl gesetzt werden!");
        this.matrix = mat;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (int j = 0; j < dimension; j++) {
            for (int i = 0; i < dimension; i++)
                buf.append(matrix[j + dimension * i] + "\t");
            buf.append("\n");
        }
        return buf.toString();
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public Matrix clone() {
        return new Matrix(matrix.clone());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Matrix other = (Matrix) obj;
        if (!Arrays.equals(this.matrix, other.matrix))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Arrays.hashCode(this.matrix);
        return hash;
    }
}
