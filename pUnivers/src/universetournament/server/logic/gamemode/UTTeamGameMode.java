package universetournament.server.logic.gamemode;

import java.awt.Color;

import universetournament.server.core.ServerMainController;
import universetournament.server.events.buffers.PJUTEventBuffManager;
import universetournament.server.logic.entities.PhysicalDecFactory;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.MainType;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.PJUTPlayer;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.WorldEntity;
import universetournament.shared.logic.entities.ingame.controller.MoveData;
import universetournament.shared.logic.entities.requests.PDecRequest;
import universetournament.shared.logic.gamemode.UTTeamModeData;
import universetournament.shared.logic.gamemode.events.GameAttribute;
import universetournament.shared.logic.gamemode.events.TeamPlayerData;
import universetournament.shared.util.math.Vec3;

/**
 * Abstrakte Oberklasse für Team-basierte Spielmodi, die Team-Score Verwaltung
 * und Team-Zuordnung für Spieler liefert.
 * 
 * @author Bernd Marbach
 *
 */
public abstract class UTTeamGameMode extends UTGameModeController
{
	/**
	 * Punktzahl, die ein Team für einen Abschuss bekommt.
	 */
	protected short teamScoreForKill;
	
	/**
	 * Punktzahl, die ein Team für einen Tod bekommt.
	 * Hier ist wahrscheinlich ein negativer Wert gewünscht.
	 */
	protected short teamScoreForDeath;
	
	/**
	 * Allgemeine Team-Modus-Daten.
	 */
	protected UTTeamModeData teamData;
	
	//wie viele Spieler in den Teams sind
	private byte blueSize, redSize;
	
	private short blueMotherShip, redMotherShip;
	
	/**
	 * Erzeugt einen Team-basierten Spielmodus-Controller für zwei Teams
	 * (rot und blau) mit der angegebenen Restzeit und dem angegebenen
	 * Punktelimit.
	 * Standardmäßig bekommt jedes Team für einen Kill einen Punkt und einen
	 * Punkt für jeden Tod abgezogen.
	 * Wirft eine NullPointerException, wenn die Spieldaten null sind.
	 * 
	 * @param teamData zu benutzende Spieldaten
	 */
	public UTTeamGameMode(UTTeamModeData teamData)
	{
		super(teamData);
		
		if(teamData != null)
		{
			this.teamData = teamData;
		}
		else
		{
			throw new NullPointerException("Spieldaten sind null");
		}
		
		teamScoreForKill = 1;
		teamScoreForDeath = -1;
		
		blueSize = 0;
		redSize = 0;
		blueMotherShip = 19;
		redMotherShip = 20;
	}
	
	@Override
	protected void playerJoined(PJUTPlayer player)
	{
		if(player != null)
		{
			super.playerJoined(player);
			int color = 0;
			TeamPlayerData data = null;
			
			//Team zuordnen, kleineres Team auffüllen
			if(redSize < blueSize)
			{
				++redSize;
				teamData.setTeam(player.getClientId(), false);
				color = Color.RED.getRGB();
				data = new TeamPlayerData(player.getClientId(), false);
			}
			else
			{
				++blueSize;
				teamData.setTeam(player.getClientId(), true);
				color = Color.BLUE.getRGB();
				data = new TeamPlayerData(player.getClientId(), true);
			}
			
			//Farbe vor Entity-Versand setzen
			player.setColor(new Color(color));
			
			//Clients benachrichtigen
			PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
					MainType.TYPE_GAMEMODE, GameAttribute.TEAM_JOIN, data));
		}
	}
	
	@Override
	protected void playerLeft(byte id)
	{
		if(gameData.getPlayers().get(id) != null)
		{
			super.playerLeft(id);
			
			if(teamData.getTeam(id) != null)
			{
				if(teamData.getTeam(id))
				{
					--blueSize;
				}
				else
				{
					--redSize;
				}
				teamData.unsetTeam(id);
			}
			
			//Clients benachrichtigen
			PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
					MainType.TYPE_GAMEMODE, GameAttribute.TEAM_LEAVE, id));
		}
	}
	
	@Override
	protected void playerKilled(byte id)
	{
		if(teamData.getTeam(id))
		{
			teamData.changeTeamScoreBlue(teamScoreForKill);
			
			PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
					MainType.TYPE_GAMEMODE, GameAttribute.SCORE_BLUE,
					teamData.getTeamScoreBlue()));
		}
		else
		{
			teamData.changeTeamScoreRed(teamScoreForKill);
			
			PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
					MainType.TYPE_GAMEMODE, GameAttribute.SCORE_RED,
					teamData.getTeamScoreRed()));
		}
		
		super.playerKilled(id);
	}
	
	@Override
	protected void playerDied(byte id)
	{
		if(teamData.getTeam(id))
		{
			teamData.changeTeamScoreBlue(teamScoreForDeath);
			
			PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
					MainType.TYPE_GAMEMODE, GameAttribute.SCORE_BLUE,
					teamData.getTeamScoreBlue()));
		}
		else
		{
			teamData.changeTeamScoreRed(teamScoreForDeath);
			
			PJUTEventBuffManager.getInstance().sendToAll(new GameEvent(
					MainType.TYPE_GAMEMODE, GameAttribute.SCORE_RED,
					teamData.getTeamScoreRed()));
		}
		
		super.playerDied(id);
	}
	
	@Override
	/**
	 * @return ob ein Team die Zielpunktzahl erreicht hat
	 */
	protected boolean pointsReached()
	{
		boolean reached = false;
		
		short limit = teamData.getScoreLimit();
		
		if(teamData.getTeamScoreBlue() >= limit
				|| teamData.getTeamScoreRed() >= limit)
		{
			reached = true;
		}
		
		return reached;
	}
	
	@Override
	/**
	 * Platziert das Schiff je nach Team des zugehörigen Spielers auf einer
	 * Seite des Spielfelds.
	 */
	public void placeShip(ShipEntity ship)
	{
		if(ship != null)
		{
			float orientation = 1.0f;
			if(teamData.getTeam(ship.getPilotId()) == null)
			{
				orientation = 0.0f;
			}
			else if(teamData.getTeam(ship.getPilotId()))
			{
				orientation = -1.0f;
			}
			
			Vec3 pos = new Vec3(orientation * mapSize,  0.f,
					(float) ((0.5f * mapSize) * (1. - Math.random())));
			Vec3 vel = new Vec3();
			
			ship.getTransformation().setWorldPosition(pos);
			ship.getTransformation().setVelocity(vel);
			
			//den Clients Änderung übermitteln
	    	MoveData data = new MoveData(pos, vel, vel);
	    	data.setOverride(true);
	    	EntityEvent ee = new EntityEvent(ship.getId(),
	    			WorldEntity.SubEvents.MOVE, data);
	    	PJUTEventBuffManager.getInstance().sendToAll(ee);
		}
		
		spawnDecorations();
	}
	
	@Override
	protected void gameOver()
	{
		String nachricht = "Spiel vorbei!\n";
		
		//Gewinner ermitteln, falls vorhanden
		short blue = teamData.getTeamScoreBlue();
		short red = teamData.getTeamScoreRed();
		
		if(blue > red)
		{
			nachricht += "Team Blau gewinnt!";
		}
		else if(red > blue)
		{
			nachricht += "Team Rot gewinnt!";
		}
		else
		{
			nachricht += "Unentschieden!";
		}
		
		GameEvent ge = new EntityEvent(
				ServerMainController.getInstance().getEntities().getGameData().
				getId(), GameAttribute.GAME_OVER, nachricht);
		PJUTEventBuffManager.getInstance().sendToAll(ge);
	}
	
	@Override
	public void kick(int index)
	{
		//wegen Team-Punkten
		index -= 2;
		super.kick(index);
	}
	
	@Override
	protected void spawnDecorations()
	{
		super.spawnDecorations();
		
		PhysicalDecFactory fact = new PhysicalDecFactory();
		
		//Mutterschiffe der Teams
		fact.handleEvent(new GameEvent(MainType.TYPE_ENTITY,
				Entity.SubEvents.CREATE, new PDecRequest(
				new Vec3(mapSize * -1.5f, 0.f, 0.f), blueMotherShip)));
		
		fact.handleEvent(new GameEvent(MainType.TYPE_ENTITY,
				Entity.SubEvents.CREATE, new PDecRequest(
				new Vec3(mapSize * 1.5f, 0.f, 0.f), redMotherShip)));
	}
}
