package universetournament.shared.logic.entities.controller;

import java.awt.Color;
import universetournament.client.core.ClientMainController;
import universetournament.client.gui.huds.DMHud;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.util.EntitySubController;

/**
 * Controller, welcher je nach eingehenden Events die Attribute eines Spielers
 * setzt.
 * 
 * @author sylence
 *
 */
public class PlayerController extends EntitySubController<PJUTPlayer>
{
	private static final SubType[] types = { PlayerAttribute.COLOR,
			PlayerAttribute.SCORE, PlayerAttribute.KILLS,
			PlayerAttribute.DEATHS };

	/**
	 * Erzeugt einen neuen Spieler-Controller für den übergebenen Spieler.
	 * Wirft eine NullPointerException, falls dieser null ist.
	 *
	 * @param entity zu manipulierender Spieler
	 */
	public PlayerController(PJUTPlayer entity)
	{
		super(entity);
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if (event != null && event.getSub() instanceof PlayerAttribute)
			switch ((PlayerAttribute) event.getSub())
			{
				case COLOR:
					if (event.getData() instanceof Integer)
						entity.setColor(new Color((Integer) event.getData()));
					break;

				case SCORE:
					if (event.getData() instanceof Short)
					{
						entity.setScore((Short) event.getData());
						PJUTPlayer p = ClientMainController.getInstance()
								.getPlayer();
						if (entity.equals(p))
						{
							DMHud hud = ClientMainController.getInstance()
									.getGameModeController().getHud();
							hud.setPlayerScore((Short) event.getData());
						}
					}
					break;

				case KILLS:
					if (event.getData() instanceof Short)
						entity.setKills((Short) event.getData());
					break;

				case DEATHS:
					if (event.getData() instanceof Short)
						entity.setDeaths((Short) event.getData());
					break;
			}
	}

	@Override
	public SubType[] getTypes()
	{
		return types;
	}
}
