package de.fhhof.universe.client.logic.gamemode;

import java.io.File;
import de.fhhof.universe.client.core.ClientMainController;
import de.fhhof.universe.client.gui.huds.TDMHud;
import de.fhhof.universe.client.rendering.hud.Hud;
import de.fhhof.universe.shared.logic.entities.PJUTPlayer;
import de.fhhof.universe.shared.logic.gamemode.UTTeamModeData;
import de.fhhof.universe.shared.logic.gamemode.events.GameAttribute;
import de.fhhof.universe.shared.logic.gamemode.events.TeamPlayerData;
import de.fhhof.universe.shared.util.io.UTXMLReader;

/**
 * Einfachter Controller für die Daten eines Team-Spielmodus, welcher
 * lediglich dazu dient, über Events empfangene Daten in die lokal gehaltenen
 * Spielmodus-Daten einzutragen. 
 * 
 * @author Bernd Marbach
 *
 */
public class TeamModeController extends GameModeController
{
	private final UTTeamModeData gData;

	/**
	 * Erzeugt einen Team-Spielmodus-Controller, welcher Daten in das
	 * übergebene Daten-Objekt einträgt.
	 * Wirft eine NullPointerException, falls die übergebenen Daten null sind.
	 *
	 * @param data lokales Datenobjekt, welches Spieldaten hält
	 */
	public TeamModeController(UTTeamModeData data)
	{
		// wirft Exception, wenn null
		super(data);
		this.gData = data;
	}

	@Override
	protected void iniHud()
	{
		UTXMLReader r = new UTXMLReader();
		Hud h = r.read(Hud.class, new File("data/hud_tdm.xml"));
		setHud(new TDMHud(h));
	}

	@Override
	protected void setAttribute(GameAttribute ga, Object data)
	{
		switch (ga)
		{
			case TEAM_JOIN:
				if (data instanceof TeamPlayerData)
				{
					TeamPlayerData tpd = (TeamPlayerData) data;
					gData.setTeam(tpd.getId(), tpd.isBlue());
				}
				break;

			case TEAM_LEAVE:
				if (data instanceof Byte)
					gData.unsetTeam((Byte) data);
				break;

			case SCORE_RED:
				if (data instanceof Short)
				{
					gData.setTeamScoreRed((Short) data);
					PJUTPlayer p = ClientMainController.getInstance()
							.getPlayer();
					TDMHud hud = (TDMHud) getHud();
					if (!gData.getTeam(p.getClientId()))
					{
						hud.setTeamScore(gData.getTeamScoreRed());

						hud.setTeamScoreBar((float) gData.getTeamScoreRed()
								/ gData.getScoreLimit());

						hud.setTeamScoreDiff(gData.getTeamScoreRed()
								- gData.getTeamScoreBlue());
					}
					else
					{
						hud.setTeamScoreDiff(gData.getTeamScoreBlue()
								- gData.getTeamScoreRed());
						hud.setEnemyScoreBar((float) gData.getTeamScoreRed()
								/ gData.getScoreLimit());
					}
				}
				break;

			case SCORE_BLUE:
				if (data instanceof Short)
				{
					gData.setTeamScoreBlue((Short) data);
					PJUTPlayer p = ClientMainController.getInstance()
							.getPlayer();
					TDMHud hud = (TDMHud) getHud();
                                        Boolean b = gData.getTeam((byte) p.getId());
                                        if(b == null)
                                            break;
					if (b)
					{
						hud.setTeamScore(gData.getTeamScoreBlue());
						hud.setTeamScoreBar((float) gData.getTeamScoreBlue()
								/ gData.getScoreLimit());
						hud.setTeamScoreDiff(gData.getTeamScoreBlue()
								- gData.getTeamScoreRed());
					}
					else
					{
						hud.setTeamScoreDiff(gData.getTeamScoreRed()
								- gData.getTeamScoreRed());
						hud.setEnemyScoreBar((float) gData.getTeamScoreBlue()
								/ gData.getScoreLimit());
					}
				}
				break;

			default:
				super.setAttribute(ga, data);
		}
	}
}
