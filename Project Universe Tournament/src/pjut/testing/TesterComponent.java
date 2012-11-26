package pjut.testing;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

public class TesterComponent extends JComponent
{
	private short[] x = new short[9];
	private short[] y = new short[9];
	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		for(int i = 0; ++i < 9;)
		{
			g2.drawRect(x[i] - 5, y[i] - 5, 10, 10);
		}
	}
	
	public void setCirclePos(byte id, short x, short y)
	{
		this.x[id] = x;
		this.y[id] = y;
	}
}
