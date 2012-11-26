package pjut.client.input;

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
 * @author sylence
 *
 */
public class PJUTInputBuffer implements KeyListener, MouseListener,
	MouseMotionListener
{
	//wie viele Tasten gleichzeitig erwartet werden
	private static final int EXP_KEYS = 8, EXP_BUTTONS = 5;
	
	private static PJUTInputBuffer instance;
	
	private Set<Integer> pressedKeys, heldKeys, pressedButtons, heldButtons;
	private int mouseX, mouseY;

	public static synchronized PJUTInputBuffer getInstance()
	{
		if(instance == null)
		{
			instance = new PJUTInputBuffer();
		}
		
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
