/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.universe.client.logic.entities.container;

import darwin.util.math.composits.ModelMatrix;
import de.fhhof.universe.shared.logic.entities.ingame.container.*;

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
        return (ModelMatrix) m.mult(getMatrix());
    }

}
