/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.modelviewer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *  Ein Slider Component das es erlaubt wert �nderunge abzufragen.
 * Der Slider springt nach jeder Benutzung in seinen Ursprung zur�ck
 * @author Daniel Heinrich
 */
public class FloatShifter extends JSlider
{

    private List<FloatValueListener> floatlistener = new ArrayList<FloatValueListener>();

    private float grid;
    private int last = 0;

    public FloatShifter(float gridsize)
    {
        super(JSlider.HORIZONTAL, -100, 100, 0);
        grid = gridsize;
        setMajorTickSpacing(10);
        setMinorTickSpacing(200);
        setPaintTicks(true);
        
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e)
            {
                last=0;
                ((JSlider)e.getSource()).setValue(0);
            }
        });
        
        addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e)
            {
                int val = ((JSlider)e.getSource()).getValue();
                fireChangeEvent(val-last);
                last = val;
            }
        });
    }

    private void fireChangeEvent(float f){
        for(FloatValueListener flv: floatlistener)
                flv.valueChanged(f*0.01f*grid);
    }

    public void addFloatListener(FloatValueListener fvl){
        floatlistener.add(fvl);
    }

    public void removeFloatListener(FloatValueListener fvl){
        floatlistener.remove(fvl);
    }

    public void setGrid(float f){
        grid = f;
    }
}
