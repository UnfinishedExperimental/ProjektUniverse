/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

/**
 * 4x4 Matrix mit ModelView speziellen Funktionen
 * @author Daniel Heinrich
 */
public class ModelMatrix extends Matrix4
{
    private boolean homogeneous = true;

    public ModelMatrix(Matrix m) {
        super(m.getMat());
    }

    public ModelMatrix() {
    }

    @Override
    public ModelMatrix scale(Vec3 scale) {
        homogeneous = false;
        return (ModelMatrix) super.scale(scale);
    }

    @Override
    public ModelMatrix scale(double scalex, double scaley, double scalez) {
        homogeneous = false;
        return (ModelMatrix) super.scale(scalex, scaley, scalez);
    }

    /**
     * @return matrix which transforms normal vectors correctly to the modelmatrix transformation
     */
    public Matrix getNormalMatrix(){
        Matrix m = getMinor(3, 3);
        if(!homogeneous)
            m.transpose(m).inverse(m);
        return m;
    }

    /**
     * @return if this matrix is homogeneous
     */
    public boolean isHomogeneous() {
        return homogeneous;
    }

    public void setHomogeneous(boolean homogeneous) {
        this.homogeneous = homogeneous;
    }

    @Override
    public ModelMatrix clone() {
        ModelMatrix m = new ModelMatrix();
        m.setMat(getMat().clone());
        return m;
    }
}
