package universetournament.client.logic.gamemode;

import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.gamemode.UTLastManStandingData;
import universetournament.shared.logic.gamemode.events.GameAttribute;

/**
 * Einfachter Controller für die Daten des "last man standing"-Spielmodus,
 * welcher lediglich dazu dient, über Events empfangene Daten in die lokal
 * gehaltenen Spielmodus-Daten einzutragen. 
 * 
 * @author Bernd Marbach
 *
 */
public class LMSModeController extends GameModeController
{
	private final UTLastManStandingData gData;
	
	/**
	 * Erzeugt einen "last man standing"-Spielmodus-Controller, welcher Daten
	 * in das übergebene Daten-Objekt einträgt.
	 * Wirft eine NullPointerException, falls die übergebenen Daten null sind.
	 * 
	 * @param data lokales Datenobjekt, welches Spieldaten hält
	 */
	public LMSModeController(UTLastManStandingData data)
	{
		//wirft Exception, wenn null
		super(data);
		this.gData = data;
	}
	
	@Override
	protected void setAttribute(GameAttribute ga, Object data)
	{
		switch(ga)
		{
			case PLAYER_ADD:
				if(data instanceof PJUTPlayer)
				{
					super.setAttribute(ga, data);
					gData.setStatus(((PJUTPlayer)data).getClientId(), false);
				}
				break;
				
			case PLAYER_REM:
				if(data instanceof Byte)
				{
					super.setAttribute(ga, data);
					gData.setStatus((Byte)data, false);
				}
				break;
			
			case PLAYER_DIED:
				if(data instanceof Byte)
				{
					gData.setStatus((Byte)data, true);
				}
				break;
			
			default:
				super.setAttribute(ga, data);
		}
	}
}
