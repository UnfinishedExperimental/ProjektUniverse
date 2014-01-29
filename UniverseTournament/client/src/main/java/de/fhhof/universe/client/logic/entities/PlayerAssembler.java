package de.fhhof.universe.client.logic.entities;

import javax.swing.JOptionPane;

import de.fhhof.universe.client.core.ClientMainController;
import de.fhhof.universe.client.input.commands.CloseCommand;
import de.fhhof.universe.shared.events.GameEvent;
import de.fhhof.universe.shared.events.MainType;
import de.fhhof.universe.shared.events.util.PJUTEventBus;
import de.fhhof.universe.shared.events.util.PJUTEventHandler;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.PJUTPlayer;
import de.fhhof.universe.shared.logic.entities.controller.PlayerController;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.EntityController;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;
import de.fhhof.universe.shared.logic.gamemode.events.GameAttribute;

/**
 * Erhält vom Server erzeugte Spieler-Entites und übernimmt sie ins lokale
 * System des Clients oder entfernt sie wieder.
 * 
 * @author sylence
 *
 */
public class PlayerAssembler implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof PJUTPlayer)
			{
				PJUTPlayer player = (PJUTPlayer) container.getData();
				
				//Spieler ins System übernehmen
				ClientMainController.getInstance().getEntities().
					addPlayer(player);
				
				//Spiellogik
				GameEvent ge = new GameEvent(MainType.TYPE_GAMEMODE,
						GameAttribute.PLAYER_ADD, player);
				PJUTEventBus.getInstance().handleEvent(ge);
				
				//überprüfen ob eigener Spieler
				if(player.getClientId() ==
					ClientMainController.getInstance().getClientId())
				{
					ClientMainController.getInstance().setPlayer(player);
				}
				
				//Controller
				EntityController<PJUTPlayer> controller =
					new EntityController<PJUTPlayer>(player);
				controller.addSubController(new PlayerController(player));
				
				UTEntityManager.getInstance().setController(player.getId(),
						controller);
			}
			else if(event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short pid = ((EntityEvent)event).getId();
				
				//Spieler mit ID entfernen
				ClientMainController.getInstance().getEntities().
					removePlayer(pid);
				
				UTEntityManager.getInstance().setController(pid, null);
				
				//überprüfen ob eigener Spieler
				if(pid == ClientMainController.getInstance().getPlayer().getId())
				{
					JOptionPane.showConfirmDialog(ClientMainController.
							getInstance().getWindow().getFrame(),
							"Sie wurden gekickt");
					new CloseCommand(ClientMainController.getInstance().
							getWindow().getFrame(), ClientMainController.
							getInstance().getWindow().getAnimator()).
							execute(0.f);
				}
				
				//Spiellogik
				GameEvent ge = new GameEvent(MainType.TYPE_GAMEMODE,
						GameAttribute.PLAYER_REM, pid);
				PJUTEventBus.getInstance().handleEvent(ge);
			}
		}
	}
}
