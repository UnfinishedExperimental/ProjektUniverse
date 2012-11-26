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
public class VectorTest
{

    /**
     * Test of getDimension method, of class Vector.
     */
    @Test
    public void testGetDimension() {
        System.out.println("getDimension");
        int dim = (int) (Math.random() * 100);
        Vector instance = new Vector(dim);
        int expResult = dim;
        int result = instance.getDimension();
        assertEquals(expResult, result);
    }

    /**
     * Test of reflect method, of class Vector.
     */
    @Test
    public void testReflect_Vector() {
        System.out.println("reflect");
        for (int i = 0; i < 1000; ++i) {
            double aa = Math.random() * 100 - 50;
            double bb = Math.random() * 100 - 50;
            Vector instance = new Vector(aa, bb);
            Vector normal = new Vector(0, 1);
            Vector result = instance.reflect(normal);
            result = result.reflect(normal);
            assertEquals(instance, result);
        }
    }

    /**
     * Test of normalize method, of class Vector.
     */
    @Test
    public void testNormalize_Vector() {
        System.out.println("normalize");
        for (int i = 0; i < 1000; ++i) {
            double aa = Math.random() * 100 - 50;
            double bb = Math.random() * 100 - 50;
            Vector instance = new Vector(aa, bb);
            Vector result = instance.normalize(instance);
            assertEquals(1, result.lenght(), 0.0000002);
        }
    }

    /**
     * Test of lenquad method, of class Vector.
     */
    @Test
    public void testLenquad() {
        System.out.println("lenquad");
        for (int i = 0; i < 1000; ++i) {
            double aa = Math.random() * 100 - 50;
            double bb = Math.random() * 100 - 50;
            Vector a = new Vector(aa, bb);
            Vector instance = new Vector(-aa, -bb);
            double res = a.lenquad();
            double exp = instance.lenquad();
            assertEquals(res, exp, 0.0);
            assertTrue(res >= 0);
            assertTrue(exp >= 0);
        }
    }

    /**
     * Test of dist method, of class Vector.
     */
    @Test
    public void testDist() {
        System.out.println("dist");
        for (int i = 0; i < 1000; ++i) {
            double aa = Math.random() * 100 - 50;
            double bb = Math.random() * 100 - 50;
            Vector a = new Vector(aa);
            Vector instance = new Vector(bb);
            double expResult = Math.abs(aa - bb);
            double result = instance.dist(a);
            assertEquals(expResult, result, 0.0);
            assertTrue(expResult >= 0);
        }
    }

    /**
     * Test of clone method, of class Vector.
     */
    @Test
    public void testClone() {
        System.out.println("clone");
        Vector instance = new Vector(3);
        Vector result = instance.clone();
        assertTrue(instance != result);
    }
}
