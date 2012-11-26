/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.shared.util.math;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Ein simpler MAtrix Stack
 * @author Daniel Heinrich
 */
public class MatrixStack
{

    Stack<double[]> stack = new Stack<double[]>();

    public void push(Matrix mat)
    {
        stack.push(mat.getMat());
    }

    public void pop(Matrix mat)
    {
        try {
            mat.setMat(stack.pop());
        } catch (EmptyStackException ex) {
        }
    }
}
