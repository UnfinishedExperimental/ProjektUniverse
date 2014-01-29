package de.fhhof.universe.client.input;

import java.util.HashMap;
import java.util.Set;

import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;

/**
 * Verteiler, welcher bei "refresh"-Aufruf für die Input-Daten im
 * PJUTInputBuffer die entsprechenden Aufrufe tätigt und diesen danach "leert",
 * Bei ihm lassen sich für Tastenanschläge und gehaltene Tasten auf Maus und
 * Tastatur Aufrufe registrieren, die das PJUTCommand-Interface erfüllen.
 * Funktioniert nach dem singleton pattern.
 * 
 * @author Florian Holzschuher
 *
 */
public class PJUTInputDistributor implements PJUTTimedRefreshable
{
	private static final PJUTInputDistributor instance =
		new PJUTInputDistributor();
	
	//initiale Größe der Keymap, ungefähr wie viele Knöpfe erwartet werden
	private static final int EXP_KEYS = 16, EXP_BUTTONS = 5;
	
	private PJUTInputBuffer buffer;
	
	//Tastatur
	private HashMap<Integer, PJUTCommand> keyPressMap, keyHoldMap;
	
	//Maus
	private HashMap<Integer, PJUTCommand> buttonPressMap, buttonHoldMap;
	
	/**
	 * @return Instanz der Klasse
	 */
	public static synchronized PJUTInputDistributor getInstance()
	{		
		return instance;
	}
	
	private PJUTInputDistributor()
	{
		buffer = PJUTInputBuffer.getInstance();
		
		keyPressMap = new HashMap<Integer, PJUTCommand>(EXP_KEYS);
		keyHoldMap = new HashMap<Integer, PJUTCommand>(EXP_KEYS);
		buttonPressMap = new HashMap<Integer, PJUTCommand>(EXP_BUTTONS);
		buttonHoldMap = new HashMap<Integer, PJUTCommand>(EXP_BUTTONS);
	}
	
	private void executeCommands(Set<Integer> pressed, Set<Integer> held,
			HashMap<Integer, PJUTCommand> pressBindings,
			HashMap<Integer, PJUTCommand> holdBindings, float timeDiff)
	{
		PJUTCommand temp = null;
		
		//einzelne Tastenanschläge
		for(Integer code : pressed)
		{
			synchronized(pressBindings)
			{
				temp = pressBindings.get(code);
			}
			
			if(temp != null)
			{
				temp.execute(timeDiff);
			}
		}
		
		//gehaltene Tasten
		for(Integer code : held)
		{
			//erste Iteration ignorieren
			if(!pressed.contains(code))
			{
				synchronized(holdBindings)
				{
					temp = holdBindings.get(code);
				}
				
				if(temp != null)
				{
					temp.execute(timeDiff);
				}
			}
		}
	}
	
	@Override
	/**
	 * Liest die gepufferten Eingabedaten aus dem PJUTInputBuffer aus und
	 * ruft die execute-Methoden bei den enstprechenden Kommandos auf.
	 * Im ersten Schleifendurchlauf, in dem eine Taste gedrückt wurde, wird
	 * das KeyPress-Binding abgerufen, in allen darauf folgenden das
	 * KeyHold-Binding. Das soll Doppelaufrufe vermeiden.
	 * Nach dem Abarbeiten werden die Tastenanschläge im Puffer geleert.
	 */
	public void refresh(float timeDiff)
	{
		//Maus
		Set<Integer> pressed = buffer.getPressedButtons();
		Set<Integer> held = buffer.getHeldButtons();
		
		executeCommands(pressed, held, buttonPressMap, buttonHoldMap,
				timeDiff);
		
		//Tastatur
		pressed = buffer.getPressedKeys();
		held = buffer.getHeldKeys();
		
		executeCommands(pressed, held, keyPressMap, keyHoldMap, timeDiff);
		
		buffer.clearStrokes();
	}
	
	private void bind(HashMap<Integer, PJUTCommand> map, int code,
			PJUTCommand comm)
	{
		if(comm != null)
		{
			synchronized(map)
			{
				map.put(code, comm);
			}
		}
	}

	/**
	 * Bindet das übergebene Kommando auf einen Tastenanschlag über den
	 * Keycode.
	 * Das Kommando wird nur bei der ersten Iteration aufgerufen, in der die
	 * Taste gedrückt wurde, bei gehaltenen Tasten geschehen also nicht mehrere
	 * Aufrufe (dazu dient bindToKeyHold).
	 * Wird eine Taste mehrmals pro Iteration gedrückt, geschieht auch nur ein
	 * Aufruf.
	 * Ist bereits ein gleichartiges Binding für diese Taste vorhanden, wird es
	 * überschrieben.
	 * Der Keycode ist eine vom Java Virtual Keyboard vorgegebene Konstante.
	 * Ignoriert null, führt keine Keycode-Prüfung durch.
	 * 
	 * @param keyCode Keycode der Taste auf dem Java Virtual Keyboard
	 * @param comm an Tastendruck zu bindendes Kommando
	 */
	public void bindToKeyPress(int keyCode, PJUTCommand comm)
	{
		bind(keyPressMap, keyCode, comm);
	}
	
	/**
	 * Bindet das übergebene Kommando an eine gedrückt gehaltene Taste über
	 * ihren Keycode.
	 * Das Kommando wird erst ab der zweiten Iteration aufgerufen, in der eine
	 * Taste gehalten wird, um Doppelaufrufe zu vermeiden und trotzdem alle
	 * Anschläge angemessen zu registrieren. Das Kommando wird, wenn die Taste
	 * weiter gehalten wird, bei jedem Aufruf von refresh aufgerufen.
	 * In der ersten Iteration werden nur über keyPress gebundene Kommandos
	 * aufgerufen.
	 * Ist bereits ein gleichartiges Binding für diese Taste vorhanden, wird es
	 * überschrieben.
	 * Der Keycode ist eine vom Java Virtual Keyboard vorgegebene Konstante.
	 * Ignoriert null, führt keine Keycode-Prüfung durch.
	 * 
	 * @param keyCode Keycode der Taste auf dem Java Virtual Keyboard
	 * @param comm an gehaltene Taste zu bindendes Kommando
	 */
	public void bindToKeyHold(int keyCode, PJUTCommand comm)
	{
		bind(keyHoldMap, keyCode, comm);
	}
	
	/**
	 * Bindet das übergebene Kommando auf einen Maustastendruck über den
	 * Maustasten-Code.
	 * Das Kommando wird nur bei der ersten Iteration aufgerufen, in der die
	 * Taste gedrückt wurde, bei gehaltenen Tasten geschehen also nicht mehrere
	 * Aufrufe (dazu dient bindToKeyHold).
	 * Wird eine Taste mehrmals pro Iteration gedrückt, geschieht auch nur ein
	 * Aufruf.
	 * Ist bereits ein gleichartiges Binding für diese Taste vorhanden, wird es
	 * überschrieben.
	 * Der Tasten-Code ist eine vom MouseEvent vorgegebene Konstante.
	 * Ignoriert null, führt keine Code-Prüfung durch.
	 * 
	 * @param button Code der Maustaste
	 * @param comm an Maus-Tastendruck zu bindendes Kommando
	 */
	public void bindToMousePress(int button, PJUTCommand comm)
	{
		bind(buttonPressMap, button, comm);
	}
	
	/**
	 * Das Kommando wird erst ab der zweiten Iteration aufgerufen, in der eine
	 * Taste gehalten wird, um Doppelaufrufe zu vermeiden und trotzdem alle
	 * Anschläge angemessen zu registrieren. Das Kommando wird, wenn die Taste
	 * weiter gehalten wird, bei jedem Aufruf von refresh aufgerufen.
	 * In der ersten Iteration werden nur über keyPress gebundene Kommandos
	 * aufgerufen.
	 * Ist bereits ein gleichartiges Binding für diese Taste vorhanden, wird es
	 * überschrieben.
	 * Der Tasten-Code ist eine vom MouseEvent vorgegebene Konstante.
	 * Ignoriert null, führt keine Code-Prüfung durch.
	 * 
	 * @param button Code der Maustaste
	 * @param comm an gehaltene Maustaste zu bindendes Kommando
	 */
	public void bindToMouseHold(int button, PJUTCommand comm)
	{
		bind(buttonHoldMap, button, comm);
	}
	
	/**
	 * Entfernt, falls vorhanden, das Binding für das Drücken der Taste mit
	 * dem angegebenen Keycode.
	 * 
	 * @param keyCode Keycode der Taste auf dem Java Virtual Keyboard
	 */
	public void unbindKeyPress(int keyCode)
	{
		synchronized(keyPressMap)
		{
			keyPressMap.remove(keyCode);
		}
	}
	
	/**
	 * Entfernt, falls vorhanden, das Binding für die gehaltene Taste mit
	 * dem angegebenen Keycode.
	 * 
	 * @param keyCode Keycode der Taste auf dem Java Virtual Keyboard
	 */
	public void unbindKeyHold(int keyCode)
	{
		synchronized(keyHoldMap)
		{
			keyHoldMap.remove(keyCode);
		}
	}
	
	/**
	 * Entfernt, falls vorhanden, das Binding für das Drücken der Maustaste mit
	 * dem angegebenen Code.
	 * 
	 * @param button Code der Maustaste
	 */
	public void unbindMousePress(int button)
	{
		synchronized(buttonPressMap)
		{
			buttonPressMap.remove(button);
		}
	}
	
	/**
	 * Entfernt, falls vorhanden, das Binding für die gehaltene Maustaste mit
	 * dem angegebenen Code.
	 * 
	 * @param button Code der Maustaste
	 */
	public void unbindMouseHold(int button)
	{
		synchronized(buttonHoldMap)
		{
			buttonHoldMap.remove(button);
		}
	}
	
	/**
	 * Entfernt alle Bindungen für Maus und Tastatur.
	 */
	public void unbindAll()
	{
		synchronized(keyPressMap)
		{
			keyPressMap.clear();
		}
		
		synchronized(keyHoldMap)
		{
			keyHoldMap.clear();
		}
		
		synchronized(buttonPressMap)
		{
			buttonPressMap.clear();
		}
		
		synchronized(buttonHoldMap)
		{
			buttonHoldMap.clear();
		}
	}
}
