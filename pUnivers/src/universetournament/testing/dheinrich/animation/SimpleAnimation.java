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
public class SimpleAnimation extends Animation
{
    private AnimationIntervall[] intervalls;
    private int[] timings;
    protected int accintervall = 0;

    public SimpleAnimation(KeyFrame[] frames) {
        if (frames.length < 2)
            throw new InstantiationError();

        intervalls = new AnimationIntervall[frames.length - 1];
        timings = new int[intervalls.length];

        KeyFrame last = frames[0];
        for (int i = 1; i < frames.length; ++i) {
            intervalls[i - 1] = new LinearInervall(last, frames[i]);
            totaltime += frames[i].getTime();
            timings[i - 1] = totaltime;
            last = frames[i];
        }
    }

    @Override
    public void reset() {
        acctime = 0;
        accintervall = 0;
    }

    @Override
    public void advance(int delta) {
        setAccTime(acctime + delta);
        findAccIntervall(accintervall);
    }

    @Override
    public void setTime(int time) {
        setAccTime(time);
        findAccIntervall(0);
    }

    private void setAccTime(int t) {
        acctime = t;
        if (acctime > totaltime)
            if (isInLoop())
                acctime -= totaltime;
            else
                acctime = totaltime;
    }

    private void findAccIntervall(int from) {
        int i = from;
        while (timings[i] < acctime)
            i++;
        accintervall = i;
    }

    @Override
    public Matrix4 getTransformation() {
        AnimationIntervall i = intervalls[accintervall];
        double p = ((double) acctime - timings[accintervall])
                / i.getTime();
        return i.getInterpolated(p);
    }
}
