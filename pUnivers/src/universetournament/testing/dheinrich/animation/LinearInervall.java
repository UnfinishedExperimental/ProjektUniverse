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
public class LinearInervall implements AnimationIntervall
{
    private final Vec3 translation;
    private final Quaternion rotation;
    private final long time;
    private final Matrix4 base;

    public LinearInervall(KeyFrame before, KeyFrame after) {
        time = after.getTime();
        base = before.getMatrixRep();

        Matrix4 a = after.getMatrixRep();
        Matrix4 rot = new Matrix4(base, a);
        rotation = rot.getRotation();
        translation = rot.getTranslation();
    }

    public long getTime(){
        return time;
    }

    public Matrix4 getInterpolated(double percentage) {
        Vec3 pos = translation.mult(percentage);

        Quaternion rot = rotation.getInterpolated(percentage);
        Matrix4 m = base.clone();
        m.translate(pos);
        m.rotate(rot);
        return m;
    }

}
