/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.modelviewer;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import universetournament.client.rendering.BasicScene;
import universetournament.client.rendering.Scene;
import universetournament.client.rendering.geometrie.packed.*;
import universetournament.client.rendering.ressourcen.resmanagment.*;
import universetournament.client.rendering.shaders.PhongShader;
import universetournament.client.util.io.obj.OBJObjektReader.Scale2;
import universetournament.shared.logic.entities.ingame.container.SimpleTransformation;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.Vec3;

/**
 *
 * @author dheinrich
 */
public class LoadFrame extends JFrame
{
    private JButton load;
    private JButton colormode;
    private RenderObjekt normal, fb;
    private FloatShifter yslider;
    private FloatShifter xslider;
    private FloatShifter zslider;
    private SimpleTransformation trans;
    private int count = 0;

    public LoadFrame() {
        iniComponents();
        iniListener();
        setVisible(true);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    protected void iniComponents() {

        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);

        load = new JButton("Model Laden");
        colormode = new JButton("FB-On");

        xslider = new FloatShifter(180);
        yslider = new FloatShifter(180);
        zslider = new FloatShifter(180);

        add2Pane(load);
        add2Pane(colormode);
        add2Pane(xslider);
        add2Pane(yslider);
        add2Pane(zslider);
    }

    protected void add2Pane(Component c){
        Container pane = this.getContentPane();
        GridBagConstraints  gbc;
        gbc = new GridBagConstraints(0, count++, 1, 1, 1, 1,
                                     GridBagConstraints.CENTER,
                                     GridBagConstraints.HORIZONTAL, new Insets(
                0, 0, 0, 0), 0, 0);
        pane.add(c, gbc);
    }

    protected void iniListener() {
        final Component t = this;
        load.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("./Models/");
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setFileFilter(new FileNameExtensionFilter("Wavefront Object",
                                                             "obj"));
                fc.setDialogTitle("Model laden");
                int res = fc.showOpenDialog(t);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    loadModel(file);
                }
            }
        });

        colormode.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                if (colormode.getText().equals("FB-On")) {
                    colormode.setText("FB-Off");
                    Scene.getInstance().addRenderable(fb);
                    Scene.getInstance().removeRenderable(normal);
                } else {
                    colormode.setText("FB-On");
                    Scene.getInstance().addRenderable(normal);
                    Scene.getInstance().removeRenderable(fb);
                }

            }
        });

        xslider.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                if (trans != null)
                    trans.rotateEuler(new Vec3(f, 0, 0));
            }
        });
        yslider.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                if (trans != null)
                    trans.rotateEuler(new Vec3(0, f, 0));
            }
        });
        zslider.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                if (trans != null)
                    trans.rotateEuler(new Vec3(0, 0, f));
            }
        });
    }

    private void loadModel(File file) {
        BasicScene s = Scene.getInstance();
        s.removeRenderable(fb);
        s.removeRenderable(normal);

        PhongShader p = new PhongShader();
        RessourcesLoader.getInstance().loadShader(p, "Phong.vert", "Phong.frag");
        trans = new SimpleTransformation();
//        trans.shiftWorldPosition(new Vec3f(5, -1, 5));
        normal = new RenderObjekt(p, trans);
        fb = new BackFrontTestRO(p, trans);
        ObjConfig oc = new ObjConfig(file, true, Scale2.WIDTH,
                                     5);

        RessourcesLoader.getInstance().loadRenderObjekt(normal, oc);
        RessourcesLoader.getInstance().loadRenderObjekt(fb, oc);

        s.addRenderable(normal);
    }

    public TransformationContainer getTrans() {
        return trans;
    }
}
