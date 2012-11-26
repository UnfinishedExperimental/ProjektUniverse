package universetournament.client.config;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * Objekt, welches enthält welche Tasten an welche Kommandos gebunden werden
 * sollen.
 * Die Bindings selbst sind eher auf manuelles Editieren im XML-Format
 * ausgelegt wofür die Key- und Button-Codes aus der Javadoc entnommen werden
 * können.
 * 
 * @author Florian Holzschuher
 *
 */
public class KeyBindings
{
	/**
	 * Verfügbare Kommando-Typen.
	 * 
	 * @author sylence
	 *
	 */
	public enum CommandType
	{
		ACCEL_FORWARD,
		
		ACCEL_BACKWARD,
		
		STRAFE_LEFT,
		
		STRAFE_RIGHT,
		
		FIRE_BLASTER,
		
		FIRE_ROCKET,
		
		QUIT_GAME;
	}
	
	private HashMap<CommandType, Integer> keyBinds, buttonBinds;
	
	/**
	 * Erzeugt die Standard-Keybindings.
	 */
	public KeyBindings()
	{
		keyBinds = new HashMap<CommandType, Integer>();
		buttonBinds = new HashMap<CommandType, Integer>();
		
		//Bewegung
		keyBinds.put(CommandType.ACCEL_FORWARD, KeyEvent.VK_W);
		keyBinds.put(CommandType.ACCEL_BACKWARD, KeyEvent.VK_S);
		keyBinds.put(CommandType.STRAFE_LEFT, KeyEvent.VK_A);
		keyBinds.put(CommandType.STRAFE_RIGHT, KeyEvent.VK_D);
		
		//Feuerkontrolle
		buttonBinds.put(CommandType.FIRE_BLASTER, MouseEvent.BUTTON1);
		buttonBinds.put(CommandType.FIRE_ROCKET, MouseEvent.BUTTON3);
		
		//Beenden des Spiels
		keyBinds.put(CommandType.QUIT_GAME, KeyEvent.VK_ESCAPE);
	}
	
	/**
	 * Sieht nach, ob eine Tastatur-Taste für dieses Kommando registriert ist
	 * und liefert, falls vorhanden, den VK-Keycode zurück.
	 * Existiert kein entsprechendes Binding, wird null zurückgegeben.
	 * 
	 * @param comm angefragter Kommandotyp
	 * @return Keycode oder null
	 */
	public Integer getKeyBinding(CommandType comm)
	{
		return keyBinds.get(comm);
	}
	
	/**
	 * Sieht nach, ob eine Maus-Taste für dieses Kommando registriert ist
	 * und liefert, falls vorhanden, den Button-Code zurück.
	 * Existiert kein entsprechendes Binding, wird null zurückgegeben.
	 * 
	 * @param comm angefragter Kommandotyp
	 * @return Button-Code oder null
	 */
	public Integer getMouseBinding(CommandType comm)
	{
		return buttonBinds.get(comm);
	}
}
