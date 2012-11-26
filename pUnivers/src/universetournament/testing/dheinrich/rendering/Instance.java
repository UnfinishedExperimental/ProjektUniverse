/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.testing.dheinrich.rendering;

/**
 *
 * @author dheinrich
 */
public class Instance {
    private int x, y , z, other;

    public Instance(int x, int y, int z, int other) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.other = other;
    }

    public int getOther() {
        return other;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

}
