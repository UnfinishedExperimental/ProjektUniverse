/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.shared.util.math;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author some
 */
public class Vec3fTest {

    /**
     * Test of cross method, of class Vec3f.
     */
    @Test
    public void testCross_Vec3f() {
        System.out.println("cross");
        for (int i = 0; i < 1000; ++i) {
            double x = Math.random() * 100 - 50;
            double y = Math.random() * 100 - 50;
            double z = Math.random() * 100 - 50;
            Vec3 a = new Vec3(x, y, z);

            x = Math.random() * 100 - 50;
            y = Math.random() * 100 - 50;
            z = Math.random() * 100 - 50;
            Vec3 b = new Vec3(x, y, z);
            a.normalize(a);
            b.normalize(b);
            Vec3 c = a.cross(b);
//            c.normalize(c);
            assertEquals(0., c.dot(a) , 0.0000001);
            assertEquals(0., c.dot(b) , 0.0000001);
        }
    }

}