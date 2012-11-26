/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package universetournament.client.util.io;

import java.io.File;
import universetournament.client.rendering.geometrie.unpacked.ModelObjekt;

/**
 * Abstrakte Klasse die von Parsern von unterschiedlichen Formaten von 3D Modellen
 * implementiert wird
 * @author Daniel Heinrich
 */
public abstract class ObjektReader {

    public ModelObjekt loadObjekt(String path){
        return loadObjekt(new File(path));
    }

    public abstract ModelObjekt loadObjekt(File file);
}
