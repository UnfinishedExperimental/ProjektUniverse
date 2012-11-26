/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.hudbuidler;

import javax.swing.AbstractListModel;
import universetournament.client.rendering.hud.Hud;

/**
 *
 * @author dheinrich
 */
public class HudListModel extends AbstractListModel
{
    private Hud hud;
    private int lastsize = 0;

    public int getSize() {
        int ret = 0;
        if(hud != null)
            ret =  hud.getElements().size();
        return ret;
    }

    public void randomChange(){
        fireContentsChanged(this, 0, getSize());
    }

    public Object getElementAt(int index) {
        if(hud == null)
            throw new IndexOutOfBoundsException("No Hud.");
        return hud.getElements().get(index);
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }

}
