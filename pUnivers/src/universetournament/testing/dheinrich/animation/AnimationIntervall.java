/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.animation;

import universetournament.shared.util.math.Matrix4;

/**
 *
 * @author some
 */
public interface AnimationIntervall
{
    public long getTime();

    public Matrix4 getInterpolated(double percentage);
}
