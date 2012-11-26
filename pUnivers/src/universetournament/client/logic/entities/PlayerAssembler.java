package universetournament.client.logic.entities;

import javax.swing.JOptionPane;

import universetournament.client.core.ClientMainController;
import universetournament.client.input.commands.CloseCommand;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventBus;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.controller.PlayerController;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.EntityController;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.logic.gamemode.events.GameAttribute;

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
