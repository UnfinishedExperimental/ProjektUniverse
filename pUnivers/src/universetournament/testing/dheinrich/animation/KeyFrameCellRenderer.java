/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package universetournament.testing.dheinrich.animation;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import sun.swing.DefaultLookup;

/**
 *
 * @author some
 */
public class KeyFrameCellRenderer extends DefaultListCellRenderer
{
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1,
                                                                       1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1,
                                                                          1, 1);

    private String getIndexString(ListModel lm, int index, Object value){
        KeyFrame kf = (KeyFrame)value;
        double total = kf.getTime();
        String res = "["+index+']';
        while(index-->0){
            KeyFrame k = (KeyFrame) lm.getElementAt(index);
            total += k.getTime();
        }
        return res+total;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());

        Color bg = null;
        Color fg = null;

        JList.DropLocation dropLocation = list.getDropLocation();
        if (dropLocation != null
                && !dropLocation.isInsert()
                && dropLocation.getIndex() == index) {

            bg = DefaultLookup.getColor(this, ui, "List.dropCellBackground");
            fg = DefaultLookup.getColor(this, ui, "List.dropCellForeground");

            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        if (value instanceof Icon) {
            setIcon((Icon) value);
            setText("");
        } else {
            setIcon(null);
            setText((value == null) ? "" : 
                getIndexString(list.getModel(), index, value));
        }

        setEnabled(list.isEnabled());
        setFont(list.getFont());

        Border border = null;
        if (cellHasFocus) {
            if (isSelected)
                border =
                DefaultLookup.getBorder(this, ui,
                                        "List.focusSelectedCellHighlightBorder");
            if (border == null)
                border =
                DefaultLookup.getBorder(this, ui,
                                        "List.focusCellHighlightBorder");
        } else
            border = getNoFocusBorder();
        setBorder(border);

        return this;
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, ui,
                                                "List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            if (border != null)
                return border;
            return SAFE_NO_FOCUS_BORDER;
        } else {
            if (border != null
                    && (noFocusBorder == null
                    || noFocusBorder == DEFAULT_NO_FOCUS_BORDER))
                return border;
            return noFocusBorder;
        }
    }
}
