package universetournament.client.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Puffer für die temporäre Zwischenspeicherung von Input-Event-Daten, der
 * sich direkt als Listener für diverse Arten von Input registrieren lässt.
 * Gespeichert werden gedrückte und gedrückt gehaltene Tastaturtasten und
 * Mausknöpfe, sowie die relative Mauszeigerposition.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTInputBuffer implements KeyListener, MouseListener,
	MouseMotionListener
{
	//wie viele Tasten gleichzeitig erwartet werden
	private static final int EXP_KEYS = 8, EXP_BUTTONS = 5;
	
	private static final PJUTInputBuffer instance = new PJUTInputBuffer();
	
	private final Set<Integer> pressedKeys, heldKeys, pressedButtons,
		heldButtons;
	private int mouseX, mouseY;
	private int compX, compY;

	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized PJUTInputBuffer getInstance()
	{		
		return instance;
	}
	
	private PJUTInputBuffer()
	{
		pressedKeys = new HashSet<Integer>(EXP_KEYS);
		heldKeys = new HashSet<Integer>(EXP_KEYS);
		
		/*
		 * initiale Größe 5, weil 3 Maustasten erwartet werden, aber ein
		 * HashSet sollte etwas größer angelegt werden.
		 */
		pressedButtons = new HashSet<Integer>(EXP_BUTTONS);
		heldButtons = new HashSet<Integer>(EXP_BUTTONS);
		
		mouseX = 0;
		mouseY = 0;
		
		compX = 1;
		compY = 1;
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
		synchronized(pressedKeys)
		{
			pressedKeys.add(e.getKeyCode());
		}
		
		synchronized(heldKeys)
		{
			heldKeys.add(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		synchronized(heldKeys)
		{
			heldKeys.remove(e.getKeyCode());
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		//wird nicht benutzt
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		//wird nicht benutzt
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		//wird nicht benutzt
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		//wird nicht benutzt
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		synchronized(pressedButtons)
		{
			pressedButtons.add(e.getButton());
		}
		
		synchronized(heldButtons)
		{
			heldButtons.add(e.getButton());
		}
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		synchronized(heldButtons)
		{
			heldButtons.remove(e.getButton());
		}
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		//ob einfach bewegen oder "draggen" ist egal
                // führt zu doppel überschreibung, da selbes wie move, aber wayne
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		mouseX = e.getX();
		mouseY = e.getY();
	}

	/**
	 * Gibt ein Set mit den Keycodes der Tasten zurück, die seit der letzten
	 * Puffer-Leerung gedrückt wurden.
	 * Die Werte entsprechen den Codes aus dem Java Virtual Keyboard.
	 * Auf dieses Set sollte um Probleme zu vermeiden nur synchronized
	 * zugegriffen werden.
	 * 
	 * @return Set aus Keycodes
	 */
	public Set<Integer> getPressedKeys()
	{
		return pressedKeys;
	}
	
	/**
	 * Gibt ein Set mit den Keycodes der Tasten zurück, die momentan gehalten
	 * werden.
	 * Die Werte entsprechen den Codes aus dem Java Virtual Keyboard.
	 * Auf dieses Set sollte um Probleme zu vermeiden nur synchronized
	 * zugegriffen werden.
	 * 
	 * @return Set aus Keycodes
	 */
	public Set<Integer> getHeldKeys()
	{
		return heldKeys;
	}
	
	/**
	 * Gibt ein Set mit den Codes der Maustasten zurück, die seit der
	 * letzten Puffer-Leerung gedrückt wurden.
	 * Die Werte entsprechen den Codes aus dem MouseEvent.
	 * Auf dieses Set sollte um Probleme zu vermeiden nur synchronized
	 * zugegriffen werden.
	 * 
	 * @return Set aus Button Codes
	 */
	public Set<Integer> getPressedButtons()
	{
		return pressedButtons;
	}
	
	/**
	 * Gibt ein Set mit den Codes der Maustasten zurück, die momentan gehalten
	 * werden.
	 * Die Werte entsprechen den Codes aus dem MouseEvent.
	 * Auf dieses Set sollte um Probleme zu vermeiden nur synchronized
	 * zugegriffen werden.
	 * 
	 * @return Set aus Button Codes
	 */
	public Set<Integer> getHeldButtons()
	{
		return heldButtons;
	}
	
	/**
	 * @return letzte eingetroffene horizontale Mausposition in der Komponente
	 */
	public int getMouseX()
	{
		return mouseX;
	}
	
	/**
	 * 
	 * @return letzte eingetroffene vertikale Mausposition in der Komponente
	 */
	public int getMouseY()
	{
		return mouseY;
	}

	/**
	 * Setzt die intern gehaltene Breite der Komponente, auf der der Puffer
	 * registriert ist.
	 * Wird ein Wert <= 1 übergeben wird diese auf 2 gesetzt.
	 * 
	 * @param width Breite der Mutter-Komponente
	 */
	public void setCompX(int width)
	{
		if(width > 1)
		{
			compX = width;
		}
		else
		{
			compX = 2;
		}
	}
	
	/**
	 * Setzt die intern gehaltene Höhe der Komponente, auf der der Puffer
	 * registriert ist.
	 * Wird ein Wert <= 1 übergeben wird diese auf 2 gesetzt.
	 * 
	 * @param height Höhe der Mutter-Komponente
	 */
	public void setCompY(int height)
	{
		if(height > 1)
		{
			compY = height;
		}
		else
		{
			compY = 2;
		}
	}
	
	/**
	 * Liefert, wenn die richtige Komponentengröße eingestellt wurde, die
	 * relative X-Position des Mauszeigers als Wert zwischen -1 und 1.
	 * -1 bedeutet "ganz links", 1 bedeutet "ganz rechts".
	 * 
	 * @return relative horizontale Mausposition
	 */
	public float getMouseRelX()
	{
		return (mouseX - compX / 2) / (float)(compX / 2);
	}
	
	/**
	 * Liefert, wenn die richtige Komponentengröße eingestellt wurde, die
	 * relative Y-Position des Mauszeigers als Wert zwischen -1 und 1.
	 * -1 bedeutet "ganz unten", 1 bedeutet "ganz oben".
	 * 
	 * @return relative vertikale Mausposition
	 */
	public float getMouseRelY()
	{
		return (compY / 2 - mouseY) / (float)(compY / 2);
	}
	
	/**
	 * Leert die eingegangenen Tastenanschläge und Maus-Clicks.
	 * Gehaltene Tasten auf Tastatur und Maus, und Mausposition bleiben
	 * erhalten.
	 */
	public void clearStrokes()
	{
		synchronized(pressedKeys)
		{
			pressedKeys.clear();
		}
		
		synchronized(pressedButtons)
		{
			pressedButtons.clear();
		}
	}
}
