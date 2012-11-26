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
public abstract class Animation
{
    private boolean loop = false;
    protected int totaltime = 0;
    protected int acctime = 0;

    public abstract void reset();

    public boolean isInLoop(){
        return loop;
    }

    public void setLoop(boolean l){
        loop = l;
    }

    public abstract void advance(int delta);

    public abstract Matrix4 getTransformation();

    public abstract void setTime(int time);

    public int getTime(){
        return acctime;
    }

    public void setPercentage(double per){
        setTime((int) (totaltime * per));
    }

    public double getPercentage(){
        return ((double)acctime) / totaltime;
    }

    public int getTotalTime(){
        return totaltime;
    }
}
