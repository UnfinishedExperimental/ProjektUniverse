/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

import javax.media.opengl.GLException;

/**
 * 4x4 Matrix mit Projektion Matrix typischen Funktionen
 * @author Daniel Heinrich
 */
public class ProjektionMatrix extends Matrix4
{
    private double fov, znear, zfar, aspect;

    public ProjektionMatrix(double fov, double znear, double zfar)
    {
        this.fov = fov;
        this.znear = znear;
        this.zfar = zfar;
        perspective(1);
    }

    /**
     * �ndert die H�hen Breiten Relation der Projektion
     */
    public void perspective(double aspect)
    {
        perspective(fov, aspect, znear, zfar);
    }

    /**
     * iplementierung der alten gl.perspectiv(...) funktion
     */
    public void perspective(double fovy, double aspect, double zn, double zf)
    {
        fov=fovy;
        znear = zn;
        zfar = zf;
        this.aspect = aspect;
        double top = (double) Math.tan(fovy * ((double) Math.PI) / 360.0f) * znear;
        double bottom = -top;
        double left = aspect * bottom;
        double right = aspect * top;
        frustum(left, right, bottom, top, znear, zfar);
    }

    /**
     * implementierung der alten gl.frustum(...) Funktion
     */
    public void frustum(double left, double right, double bottom, double top, double nearVal, double farVal)
    {
        if (nearVal <= 0.0f || farVal < 0.0f)
            throw new GLException("GL_INVALID_VALUE: zNear and zFar must be positive, and zNear>0");
        if (left == right || top == bottom)
            throw new GLException("GL_INVALID_VALUE: top,bottom and left,right must not be equal");

        loadIdentity();

        // Frustum matrix:
        //  2*zNear/dx   0          A  0
        //  0            2*zNear/dy B  0
        //  0            0          C  D
        //  0            0         -1  0

        double[] matrixFrustum = new double[16];

        double zNear2 = 2.0f * nearVal;
        double dx = right - left;
        double dy = top - bottom;
        double dz = farVal - nearVal;
        double A = (right + left) / dx;
        double B = (top + bottom) / dy;
        double C = -1.0f * (farVal + nearVal) / dz;
        double D = -2.0f * (farVal * nearVal) / dz;

        matrixFrustum[0] = zNear2 / dx;
        matrixFrustum[5] = zNear2 / dy;
        matrixFrustum[10] = C;

        matrixFrustum[8] = A;
        matrixFrustum[9] = B;

        matrixFrustum[14] = D;
        matrixFrustum[11] = -1;
        matrixFrustum[15] = 0;

        mult(new Matrix4(matrixFrustum), this);
    }

    public double getAspect()
    {
        return aspect;
    }

    public double getFov()
    {
        return fov;
    }

    public double getZfar()
    {
        return zfar;
    }

    public double getZnear()
    {
        return znear;
    }

    @Override
    public ProjektionMatrix clone()
    {
        ProjektionMatrix p = new ProjektionMatrix(fov, znear, zfar);
        p.setMat(getMat().clone());
        return p;
    }

    public static void main(String[] args){
        ProjektionMatrix p = new ProjektionMatrix(45, 1, 100);
        p.perspective(1f);
        System.out.println(p.mult(new Vector(2, 0, -21, 1)));
    }
}
