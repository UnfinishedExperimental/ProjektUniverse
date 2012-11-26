/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.animation;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import universetournament.client.UTClient;
import universetournament.client.core.ClientWindow;
import universetournament.client.rendering.Scene;
import universetournament.client.rendering.geometrie.packed.BackFrontTestRO;
import universetournament.client.rendering.geometrie.packed.RenderObjekt;
import universetournament.client.rendering.ressourcen.resmanagment.ObjConfig;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.shaders.PhongShader;
import universetournament.client.util.io.obj.OBJObjektReader.Scale2;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.ingame.container.SimpleTransformation;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.Matrix4;
import universetournament.shared.util.math.Quaternion;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author some
 */
public class AnimationTester
{
    public AnimationTester() {
        try {
            final FileHandler handler = new FileHandler("errors.txt");
            handler.setLevel(Level.WARNING);

            Logger logger =
                   Logger.getLogger("universetournament");
            logger.addHandler(handler);

//            ClientWindow cw = new ClientWindow(800, 600, false);
            iniAnimation();
//            EditorFrame lf = new EditorFrame();
//            cw.getFrame().addWindowListener(new WindowAdapter()
//            {
//                @Override
//                public void windowClosed(WindowEvent e) {
//                    handler.flush();
//                }
//            });
        } catch (IOException ex) {
        } catch (SecurityException ex) {
        }
    }

    private void iniAnimation() {
        Quaternion q1 = new Quaternion();
        q1.setAxisAngle(new Vec3(0, -1, 0), 45);
        Quaternion q2 = new Quaternion();
        q2.setAxisAngle(new Vec3(2, 1, 0), 90);

        KeyFrame k1 = new KeyFrame(new Vec3(), q1, 0);
        KeyFrame k2 = new KeyFrame(new Vec3(10, 0, 0), q2, 2000);

        final Animation a = new SimpleAnimation(new KeyFrame[]{k1, k2});
        a.setLoop(true);

//        PhongShader p = new PhongShader();
//        RessourcesLoader.getInstance().loadShader(p, "Phong.vert", "Phong.frag");
//        final TransformationContainer trans = new SimpleTransformation();
//        RenderObjekt normal = new RenderObjekt(p, trans);
//        ObjConfig oc = new ObjConfig(new File("Models/ton.obj"), true,
//                                     Scale2.WIDTH,
//                                     5);
//        RessourcesLoader.getInstance().loadRenderObjekt(normal, oc);
//        Scene.getInstance().addRenderable(normal);

        a.setPercentage(0.5);
        System.out.println(q1.mult(q2));

//        UTClient.getCore().addTimedRefreshable(new PJUTTimedRefreshable()
//        {
//            public void refresh(float timeDiff) {
//                a.advance((int) (timeDiff * 1e-6));
//                Matrix4 m = a.getTransformation();
//                trans.reset();
//                trans.setPosition(m.getTranslation());
//                trans.rotate(m);
//            }
//        });
    }

    public static void main(String[] args) {
        new AnimationTester();
    }
}
