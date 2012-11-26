/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.testing.dheinrich;

import java.io.Serializable;


/**
 *
 * @author dheinrich
 */
public class MoveData implements Serializable{

    private short x, y;

    public MoveData(short x, short y) {
        this.x = x;
        this.y = y;
    }

    public short getX() {
        return x;
    }

    public short getY() {
        return y;
    }
}
