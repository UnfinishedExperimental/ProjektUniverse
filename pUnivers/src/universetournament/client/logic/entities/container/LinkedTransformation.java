/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.logic.entities.container;

import universetournament.shared.logic.entities.ingame.container.*;
import universetournament.shared.util.math.ModelMatrix;

/**
 *
 * @author dheinrich
 */
public class LinkedTransformation extends SimpleTransformation{
    private TransformationContainer trans;

    public LinkedTransformation(TransformationContainer trans) {
        this.trans = trans;
    }

    @Override
    public ModelMatrix getModelMatrix() {
        ModelMatrix m = trans.getModelMatrix();
        return (ModelMatrix) m.mult(getMatrix(), m);
    }

}
