/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.animation;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.MouseInputAdapter;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.Matrix4;
import universetournament.shared.util.math.Vec3;
import universetournament.testing.dheinrich.modelviewer.FloatShifter;
import universetournament.testing.dheinrich.modelviewer.FloatValueListener;
import universetournament.testing.dheinrich.modelviewer.LoadFrame;

/**
 *
 * @author some
 */
public class EditorFrame extends LoadFrame
{
    private FloatShifter xslider, yslider, zslider;
    private JFormattedTextField anitime;
    private JButton savepos;
    private JList poslist;
    private DefaultListModel dlm;
    private JSlider slider;
    private Animation ani = null;

    @Override
    protected void iniComponents() {

        super.iniComponents();
        xslider = new FloatShifter(4);
        yslider = new FloatShifter(4);
        zslider = new FloatShifter(4);

        anitime = new JFormattedTextField(0);
        savepos = new JButton("Save Position");
        poslist = new JList();

        slider = new JSlider(JSlider.VERTICAL, 0, 200, 0);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);


        Container pane = this.getContentPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints(1, 0, 1, 12, 1, 1,
                                     GridBagConstraints.CENTER,
                                     GridBagConstraints.VERTICAL, new Insets(
                0, 0, 0, 0), 0, 0);
        pane.add(slider, gbc);

        add2Pane(new JSeparator(JSeparator.HORIZONTAL));
        add2Pane(xslider);
        add2Pane(yslider);
        add2Pane(zslider);

        add2Pane(anitime);
        add2Pane(savepos);
        add2Pane(new JScrollPane(poslist));

        dlm = new DefaultListModel();
        poslist.setModel(dlm);
        poslist.setCellRenderer(new KeyFrameCellRenderer());
    }

    void calcAnimation() {
        Object[] kfs = dlm.toArray();
        KeyFrame[] frames = new KeyFrame[kfs.length];
        for (int i = 0; i < frames.length; ++i)
            frames[i] = (KeyFrame) kfs[i];
        if (frames.length > 1)
            ani = new SimpleAnimation(frames);
        else
            ani = null;
    }

    @Override
    protected void iniListener() {
        super.iniListener();

        dlm.addListDataListener(new ListDataListener()
        {
            public void intervalAdded(ListDataEvent e) {
                calcAnimation();
            }

            public void intervalRemoved(ListDataEvent e) {
                calcAnimation();
            }

            public void contentsChanged(ListDataEvent e) {
                calcAnimation();
            }
        });

        xslider.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                TransformationContainer t = getTrans();
                if (t != null)
                    t.shiftWorldPosition(new Vec3(f, 0, 0));
            }
        });
        yslider.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                TransformationContainer t = getTrans();
                if (t != null)
                    t.shiftWorldPosition(new Vec3(0, f, 0));
            }
        });
        zslider.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                TransformationContainer t = getTrans();
                if (t != null)
                    t.shiftWorldPosition(new Vec3(0, 0, f));
            }
        });

        poslist.addMouseListener(new MouseInputAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2)
                    return;
                int i = poslist.getSelectedIndex();
                ((DefaultListModel) poslist.getModel()).remove(i);
            }
        });

        savepos.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                Matrix4 m = getTrans().getModelMatrix();
                dlm.addElement(
                        new KeyFrame(m.getTranslation(), m.getRotation(),
                                     (Integer)anitime.getValue()));
            }
        });

        slider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e) {
                TransformationContainer t = getTrans();
                if (ani != null && t != null) {
                    double per = ((JSlider) e.getSource()).getValue() / 200.;
                    ani.setPercentage(per);
                    Matrix4 m = ani.getTransformation();
                    t.setWorldPosition(m.getTranslation());
                    t.setRotation(m.getRotation());
                }
            }
        });
    }
}
