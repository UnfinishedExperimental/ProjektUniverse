/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.testing.dheinrich.rendering.procedual;

/**
 *
 * @author dheinrich
 */
public class NoEdgeException extends Exception{

    public NoEdgeException(Triangle t) {
        super("No Edge with Trinagle: "+t);
    }

}
