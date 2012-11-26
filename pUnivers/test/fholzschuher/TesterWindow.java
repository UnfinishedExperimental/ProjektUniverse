package fholzschuher;

import javax.swing.JFrame;

import universetournament.client.communication.PJUTCliConnector;
import universetournament.client.input.PJUTInputBuffer;
import universetournament.client.input.PJUTInputDistributor;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventBus;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * 
 * @author  Florian Holzschuher
 *
 */
public class TesterWindow extends JFrame implements Runnable
{
    private TesterComponent comp;

    public TesterWindow()
    {
        comp = new TesterComponent();
        
        UTEntityManager.getInstance().setEntityCreator(comp);
        UTEntityManager.getInstance().setEntityDestroyer(comp);
        
        this.add(comp);

        PJUTInputBuffer buffer = PJUTInputBuffer.getInstance();
        comp.addMouseListener(buffer);
        comp.addMouseMotionListener(buffer);
        comp.addKeyListener(buffer);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setSize(640, 480);
        this.setVisible(true);
    }

    @Override
    public void run()
    {
        while (true)
        {
        	UTEntityManager.getInstance().refresh(0);
            PJUTInputDistributor.getInstance().refresh(0);
            PJUTCliConnector.getInstance().getBuffer().flush();

            try
            {
                Thread.sleep(16);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            comp.repaint();
        }
    }
}
