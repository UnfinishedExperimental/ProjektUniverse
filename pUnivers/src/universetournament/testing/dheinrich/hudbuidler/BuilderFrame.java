/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.hudbuidler;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import universetournament.client.rendering.Scene;
import universetournament.client.rendering.hud.*;
import universetournament.client.rendering.hud.DynamicHudElement.*;
import universetournament.shared.util.io.*;
import universetournament.testing.dheinrich.modelviewer.*;

/**
 *
 * @author dheinrich
 */
public class BuilderFrame extends JFrame
{
    private Hud hud;
    private JList elements;
    private JButton load, save, add, remove, addNumb;
    private JTextField name;
    private HudListModel hudmodel;
    private JCheckBox aspec, crop;
    private FloatShifter posx, posy, sizex, sizey, ratio;
    private JSeparator possep, sizesep;
    private JRadioButton staticele, dynamicele;
    private JComboBox taele, dirtype, alligm;
    private ButtonGroup type;

    public BuilderFrame() {
        iniComponents();
        buildPanel();
        iniListener();
        loadHUD(new File("data/hud.xml"));
    }

    private void iniComponents() {
        hudmodel = new HudListModel();
        elements = new JList(hudmodel);

        load = new JButton("HUD laden");
        save = new JButton("HUD speichern");
        add = new JButton("+");
        remove = new JButton("-");
        addNumb = new JButton("addCounter");

        posx = new FloatShifter(0.5f);
        posy = new FloatShifter(0.5f);
        aspec = new JCheckBox("verhältniss");
        sizex = new FloatShifter(0.1f);
        sizey = new FloatShifter(0.1f);

        ratio = new FloatShifter(1);

        possep = new JSeparator(JSeparator.HORIZONTAL);
        sizesep = new JSeparator(JSeparator.HORIZONTAL);

        name = new JTextField();
        taele = new JComboBox();

        type = new ButtonGroup();
        staticele = new JRadioButton("Statisch", true);
        dynamicele = new JRadioButton("Dynamisch", false);
        type.add(staticele);
        type.add(dynamicele);

        ComboBoxModel cbm = new DefaultComboBoxModel(Direction.values());
        dirtype = new JComboBox(cbm);
        dirtype.setEnabled(false);

        cbm = new DefaultComboBoxModel(DynamicHudElement.Alligment.values());
        alligm = new JComboBox(cbm);
        alligm.setEnabled(false);

        crop = new JCheckBox("Crop Picture");
    }

    private void buildPanel() {
        Container main = getContentPane();
//        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        JPanel settings = new JPanel();
        settings.setLayout(new BoxLayout(settings, BoxLayout.X_AXIS));
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));

        main.add(settings, BorderLayout.NORTH);
        main.add(buttons, BorderLayout.CENTER);
        main.add(ratio, BorderLayout.SOUTH);

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        JScrollPane scroll =
                    new JScrollPane(elements);
        left.add(scroll);
        elements.setPreferredSize(new Dimension(200, 600));

        JPanel bot = new JPanel();
        bot.setLayout(new BoxLayout(bot, BoxLayout.X_AXIS));
        left.add(bot);
        bot.add(add);
        bot.add(addNumb);
        bot.add(remove);
        settings.add(left);

        JPanel elesettings = new JPanel();
        elesettings.setLayout(new BoxLayout(elesettings, BoxLayout.Y_AXIS));
        settings.add(elesettings);

        elesettings.add(name);
        elesettings.add(posx);
        elesettings.add(posy);
        elesettings.add(possep);
        elesettings.add(sizex);
        elesettings.add(sizey);
        elesettings.add(aspec);
        elesettings.add(sizesep);
        elesettings.add(taele);
        JPanel typeop = new JPanel();
        typeop.setLayout(new BoxLayout(typeop, BoxLayout.X_AXIS));
        elesettings.add(typeop);
        elesettings.add(dirtype);
        elesettings.add(alligm);
        elesettings.add(crop);

        typeop.add(staticele);
        typeop.add(dynamicele);

        buttons.add(load);
        buttons.add(save);

        main.validate();
    }

    private void iniListener() {
        elements.addListSelectionListener(new ListSelectionListener()
        {
            public void valueChanged(ListSelectionEvent e) {
                HudElement he = (HudElement) elements.getSelectedValue();
                name.setText(he.getName());
                taele.setSelectedItem(he.getTaElement());
                dynamicele.setEnabled(true);
                staticele.setEnabled(true);
                dirtype.setEnabled(true);
                alligm.setEnabled(true);
                crop.setEnabled(true);
                if (he instanceof DynamicHudElement) {
                    dynamicele.doClick();
                    dirtype.setSelectedItem(((DynamicHudElement) he).
                            getDirection());
                    alligm.setSelectedItem(
                            ((DynamicHudElement) he).getAlligment());
                    crop.setSelected(((DynamicHudElement) he).isCrop());
                } else if (he instanceof HudNumber) {
                    dynamicele.setEnabled(false);
                    staticele.setEnabled(false);
                    dirtype.setEnabled(false);
                    alligm.setEnabled(false);
                    crop.setEnabled(false);
                } else
                    staticele.doClick();

            }
        });

        add.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                String name =
                       JOptionPane.showInputDialog(
                        "please enter name of new Element",
                        "");
                HudElement he = new HudElement(name, new float[]{0, 0},
                                               new float[]{0.1f, 0.1f},
                                               (TextureAtlasElement) hud.
                        getTexatlas().getElements().toArray()[0]);
                hud.addElement(he);
                hudmodel.randomChange();
                elements.setSelectedValue(he, true);
                System.out.println(elements.getModel().getSize());
            }
        });

        remove.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                HudElement he = (HudElement) elements.getSelectedValue();
                hud.removeElement(he);
                hudmodel.randomChange();
                if (hudmodel.getSize() > 0)
                    elements.setSelectedIndex(0);
            }
        });

        addNumb.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                String name =
                       JOptionPane.showInputDialog(
                        "please enter name of new Element",
                        "");
                HudNumber he = new HudNumber(name, new float[]{0, 0},
                                             new float[]{0.1f, 0.1f},
                                             (TextureAtlasElement) hud.
                        getTexatlas().getElements().toArray()[0]);
                he.setNumber(1234);
                hud.addElement(he);
                hudmodel.randomChange();
                elements.setSelectedValue(he, true);
                System.out.println(elements.getModel().getSize());
            }
        });

        dynamicele.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                dirtype.setEnabled(true);
                HudElement he = (HudElement) elements.getSelectedValue();
                int index = elements.getSelectedIndex();
                if (he instanceof DynamicHudElement)
                    return;

                DynamicHudElement he2 = new DynamicHudElement(he);
                hud.switchElement(he, he2);
                hudmodel.randomChange();
                elements.setSelectedIndex(index);
            }
        });

        staticele.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                dirtype.setEnabled(false);
                HudElement he = (HudElement) elements.getSelectedValue();
                if (he instanceof DynamicHudElement) {
                    int index = elements.getSelectedIndex();
                    HudElement he2 = new HudElement(he);
                    hud.switchElement(he, he2);
                    hudmodel.randomChange();
                    elements.setSelectedIndex(index);
                }
            }
        });

        posx.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                HudElement he = (HudElement) elements.getSelectedValue();
                he.getPosition()[0] += f;
            }
        });

        posy.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                HudElement he = (HudElement) elements.getSelectedValue();
                he.getPosition()[1] += f;
            }
        });

        sizex.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                HudElement he = (HudElement) elements.getSelectedValue();
                if (aspec.isSelected()) {
                    float aspect = he.getSize()[1] / he.getSize()[0];
                    he.getSize()[1] += f * aspect;
                }
                he.getSize()[0] += f;
            }
        });

        sizey.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                HudElement he = (HudElement) elements.getSelectedValue();
                if (aspec.isSelected()) {
                    float aspect = he.getSize()[0] / he.getSize()[1];
                    he.getSize()[0] += f * aspect;
                }
                he.getSize()[1] += f;
            }
        });

        ratio.addFloatListener(new FloatValueListener()
        {
            public void valueChanged(float f) {
                for (HudElement he : hud.getElements())
                    if (he instanceof DynamicHudElement){
                        DynamicHudElement de = (DynamicHudElement) he;
                        de.setPerc(de.getPerc()+f);
                    }
            }
        });

        taele.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                HudElement he = (HudElement) elements.getSelectedValue();
                he.setTaElement((TextureAtlasElement) taele.getSelectedItem());
            }
        });

        name.getDocument().addDocumentListener(new DocumentListener()
        {
            public void insertUpdate(DocumentEvent e) {
                HudElement he = (HudElement) elements.getSelectedValue();
                he.setName(name.getText());
                hudmodel.randomChange();
            }

            public void removeUpdate(DocumentEvent e) {
                HudElement he = (HudElement) elements.getSelectedValue();
                he.setName(name.getText());
                hudmodel.randomChange();
            }

            public void changedUpdate(DocumentEvent e) {
            }
        });

        load.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("./data/");
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setFileFilter(new FileNameExtensionFilter("XML",
                                                             "xml"));
                fc.setDialogTitle("HUD laden");
                int res = fc.showOpenDialog(load);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    loadHUD(file);
                }
            }
        });

        save.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser("./data/");
                fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fc.setFileFilter(new FileNameExtensionFilter("xml",
                                                             "xml"));
                fc.setDialogTitle("HUD speichern");
                int res = fc.showSaveDialog(load);
                if (res == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String fname = file.getName();
                    if (!fname.contains("."))
                        file = new File(fname + ".xml");
                    saveHUD(file);
                }
            }
        });

        dirtype.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                DynamicHudElement he = (DynamicHudElement) elements.
                        getSelectedValue();
                he.setDirection((Direction) dirtype.getSelectedItem());
            }
        });

        alligm.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                DynamicHudElement he = (DynamicHudElement) elements.
                        getSelectedValue();
                he.setAlligment((Alligment) alligm.getSelectedItem());
            }
        });

        crop.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) {
                DynamicHudElement he = (DynamicHudElement) elements.
                        getSelectedValue();
                he.setCrop(crop.isSelected());
            }
        });
    }

    private void loadHUD(File file) {
        UTXMLReader r = new UTXMLReader();
        Hud tmp = r.read(Hud.class, file);
        if (tmp == null)
            return;
        hud = tmp;
        hudmodel.setHud(hud);

        if (hudmodel.getSize() > 0)
            elements.setSelectedIndex(0);
        hudmodel.randomChange();

        taele.setModel(new DefaultComboBoxModel(hud.getTexatlas().getElements().
                toArray()));

        for (HudElement he : hud.getElements())
            if (he instanceof DynamicHudElement)
                ((DynamicHudElement) he).setPerc(0.5f);

//        Scene.getInstance().setHud(hud);
    }

    private void saveHUD(File file) {
        UTXMLWriter w = new UTXMLWriter();
        w.write(file, hud);
    }
}
