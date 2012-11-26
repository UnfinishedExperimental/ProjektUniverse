package universetournament.server.logic.gamemode;

import java.util.Map.Entry;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.UTEntityManager;
import universetournament.shared.logic.gamemode.UTLastManStandingData;
import universetournament.shared.logic.gamemode.events.GameAttribute;

/**
 * GameModeController für Last Man Standing, in dem alle Spieler gegeneinander
 * antreten, bis nur noch einer übrig ist.
 * 
 * @author Bernd Marbach
 *
 */
public class UTLastManStanding extends UTGameModeController
{
	/**
	 * Daten für den Last Man Standing Modus.
	 */
	protected UTLastManStandingData lmsData;
	
	public UTLastManStanding(UTLastManStandingData lmsData)
	{
		super(lmsData);
		
		if(lmsData != null)
		{
			this.lmsData = lmsData;
		}
		else
		{
			throw new NullPointerException("Spieldaten sind null");
		}
	}
	
	@Override
	protected void playerDied(byte id)
	{
		PJUTPlayer player = gameData.getPlayers().get(id);
		
		if(player != null)
		{
			player.died();
			player.modScore(scoreForDeath);
			lmsData.setStatus(id, true);
		}
		
		//Schiff löschen (extern und intern)
		EntityContainer container = new EntityContainer(EntityType.SHIP, null);
		EntityEvent ee = new EntityEvent(
				ServerMainController.getInstance().getEntities().getShip(id),
				Entity.SubEvents.DELETE, container);
		PJUTEventBuffManager.getInstance().sendToAll(ee);
		UTEntityManager.getInstance().handleEvent(ee);
		
		//schauen ob vorbei
		checkStatus();
		
		//Clients benachrichtigen
		PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
				MainType.TYPE_GAMEMODE, GameAttribute.PLAYER_DIED, id));
	}
	
	private void checkStatus()
	{
		if(lmsData.isUnlocked())
		{
			//wenn einer oder weniger übrig -> vorbei
			int alive = lmsData.getPlayers().size();
			for(Entry<Byte, PJUTPlayer> pe : lmsData.getPlayers().entrySet())
			{
				if(lmsData.isDead(pe.getValue().getClientId()))
				{
					--alive;
				}
			}
			
			if(alive <= 1)
			{
				lmsData.setRunning(false);
				gameOver();
			}
		}
	}
	
	@Override
	protected void gameOver()
	{
		String nachricht = "Spiel vorbei!\n";
		PJUTPlayer winner = null;
		
		/*
		 * Gewinner (letzten überlebenden) ermitteln, bzw. erster gejointer
		 * Teilnehmer.
		 */
		for(Entry<Byte, PJUTPlayer> pe : gameData.getPlayers().entrySet())
		{
			if(!lmsData.isDead(pe.getValue().getClientId()))
			{
				winner = pe.getValue();
				break;
			}
		}
		
		if(winner != null)
		{
			nachricht += winner.getName() + " gewinnt!";
		}
		
		GameEvent ge = new GameEvent(MainType.TYPE_GAMEMODE,
				GameAttribute.GAME_OVER, nachricht);
		PJUTEventBuffManager.getInstance().sendToAll(ge);
	}
	
	@Override
	protected boolean pointsReached()
	{	
		//Punkte sind hier egal, der letzte Überlebende zählt
		return false;
	}
	
	@Override
	protected void playerJoined(PJUTPlayer player)
	{
		if(player != null)
		{
			super.playerJoined(player);
			lmsData.setStatus(player.getClientId(), false);
			
			//entsperren, wenn genug Spieler
			if(lmsData.getPlayers().size() > 1)
			{
				lmsData.unlock();
			}
		}
	}
	
	@Override
	protected void playerLeft(byte id)
	{
		super.playerLeft(id);
		lmsData.setStatus(id, null);
	}
}
