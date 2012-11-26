package universetournament.client.logic.gamemode;

import java.io.File;
import javax.swing.JOptionPane;
import universetournament.client.gui.huds.DMHud;
import universetournament.client.rendering.Scene;
import universetournament.client.rendering.hud.Hud;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTBufferedHandler;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.gamemode.UTGameModeData;
import universetournament.shared.logic.gamemode.events.GameAttribute;
import universetournament.shared.util.io.UTXMLReader;

/**
 * Einfachter Controller für die Daten eines Basis-Spielmodus, welcher
 * lediglich dazu dient, über Events empfangene Daten in die lokal gehaltenen
 * Spielmodus-Daten einzutragen. 
 * 
 * @author Bernd Marbach
 *
 */
public class GameModeController extends PJUTBufferedHandler implements
		PJUTTimedRefreshable
{
	private final UTGameModeData gData;
	private DMHud hud;

	/**
	 * Erzeugt einen einfachen Spielmodus-Controller, welcher Daten in das
	 * übergebene Daten-Objekt einträgt.
	 * Wirft eine NullPointerException, falls die übergebenen Daten null sind.
	 * 
	 * @param data lokales Datenobjekt, welches Spieldaten hält
	 */
	public GameModeController(UTGameModeData data)
	{
		if (data == null)
		{
			throw new NullPointerException("Daten sind null");
		}

		this.gData = data;
		iniHud();
	}

	/**
	 * HUD initialisieren
	 */
	protected void iniHud()
	{
		UTXMLReader r = new UTXMLReader();
		Hud h = r.read(Hud.class, new File("data/hud_dm.xml"));
		setHud(new DMHud(h));
	}

	/**
	 * @return aktuelles HUD
	 */
	public DMHud getHud()
	{
		return hud;
	}

	/**
	 * Setzt das aktuelle HUD
	 * 
	 * @param hud neues HUD
	 */
	protected void setHud(DMHud hud)
	{
		this.hud = hud;
//		Scene.getInstance().setHud(hud.getHud());
	}

	@Override
	public void refresh(float timeDiff)
	{
		synchronized (buffer)
		{
			for (GameEvent ge : buffer)
			{
				if (ge.getSub() instanceof GameAttribute
						&& ge.getData() != null)
				{
					setAttribute((GameAttribute) ge.getSub(), ge.getData());
				}
			}
			buffer.clear();
		}
	}

	/**
	 * Methode, die zum übergebenen Spiel-Attribut die angegebenen Daten
	 * verarbeitet.
	 * Sollte von den Unterklassen überschrieben werden, um eigene Events
	 * zu verarbeiten.
	 * Muss Typ nicht auf null überprüfen.
	 * 
	 * @param ga zu setzendes Spielattribut
	 * @param data einzutragende Daten
	 */
	protected void setAttribute(GameAttribute ga, final Object data)
	{
		switch (ga)
		{
			case PLAYER_ADD:
				if (data instanceof PJUTPlayer)
				{
					PJUTPlayer player = (PJUTPlayer) data;
					gData.getPlayers().put(player.getClientId(), player);
				}
				break;

			case PLAYER_REM:
				if (data instanceof Byte)
				{
					gData.getPlayers().remove((Byte) data);
				}
				break;

			case GAME_OVER:
				if (data instanceof String)
				{
					// In neuem Thread, weil OpenGL-Animator sonst hängt
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							JOptionPane.showMessageDialog(null, (String) data);
							System.exit(0);
						}
					}).start();
				}
				break;
		}
	}
}
