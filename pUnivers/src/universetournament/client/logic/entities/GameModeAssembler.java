package universetournament.client.logic.entities;

import javax.swing.JOptionPane;

import universetournament.client.UTClient;
import universetournament.client.communication.PJUTCliConnector;
import universetournament.client.core.ClientMainController;
import universetournament.client.logic.gamemode.GameModeController;
import universetournament.client.logic.gamemode.LMSModeController;
import universetournament.client.logic.gamemode.TeamModeController;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.events.util.PJUTEventBus;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.gamemode.UTGameModeData;
import universetournament.shared.logic.gamemode.UTLastManStandingData;
import universetournament.shared.logic.gamemode.UTTeamModeData;

/**
 * Erhält vom Server erzeugte Spielmodus-Entites und übernimmt sie ins lokale
 * System des Clients oder entfernt sie wieder.
 * 
 * @author sylence
 *
 */
public class GameModeAssembler implements PJUTEventHandler
{
	@Override
	public void handleEvent(GameEvent event)
	{
		if(event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();
			
			if(event.getSub() == Entity.SubEvents.CREATE)
			{
				Object data = container.getData();
				
				GameModeController ctrl = null;
				
				if(data instanceof UTLastManStandingData)
				{
					ctrl = new LMSModeController((UTLastManStandingData)data);
					ClientMainController.getInstance().getEntities().
						setGameData((UTLastManStandingData)data);
				}
				else if(data instanceof UTTeamModeData)
				{
					ctrl = new TeamModeController((UTTeamModeData)data);
					ClientMainController.getInstance().getEntities().
						setGameData((UTTeamModeData)data);
				}
				else if(data instanceof UTGameModeData)
				{
					ctrl = new GameModeController((UTGameModeData)data);
					ClientMainController.getInstance().getEntities().
						setGameData((UTGameModeData)data);
				}
				
				//Spiel beenden, wenn schon vorbei
				if(!((UTGameModeData)data).isRunning())
				{
					//In neuem Thread, weil OpenGL-Animator sonst hängt
					new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							JOptionPane.showConfirmDialog(null,
									"Spiel ist bereits vorbei");
							System.exit(0);
						}
					}).start();
				}
				
				//für Events registrieren
				if(ctrl != null)
				{
					PJUTCliConnector.getInstance().getReceiver().setHandler(
							MainType.TYPE_GAMEMODE, ctrl);
					UTClient.getCore().addTimedRefreshable(ctrl);
					
					PJUTEventBus.getInstance().register(ctrl,
							MainType.TYPE_GAMEMODE);
				}

				ClientMainController.getInstance().setGameModeController(ctrl);
			}
			else if (event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				ClientMainController.getInstance().getEntities()
						.setGameData(null);

				PJUTCliConnector.getInstance().getReceiver()
						.setHandler(MainType.TYPE_GAMEMODE, null);
			}
		}
	}
}
