package de.fhhof.universe.shared.logic.entities.util;

import java.util.ArrayList;
import java.util.List;

import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.PJUTPlayer;
import de.fhhof.universe.shared.logic.entities.ingame.RocketEntity;
import de.fhhof.universe.shared.logic.entities.ingame.ShipEntity;
import de.fhhof.universe.shared.logic.entities.ingame.ShotEntity;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.PhysicContainer;
import de.fhhof.universe.shared.logic.gamemode.UTGameModeData;

/**
 * In einer Instanz dieser Klasse werden alle Entities zu Verwaltungszwecken
 * gehalten.
 * Auf Thread-Sicherheit ausgelegt.
 * Wenn von anderen Klassen aus auf die internen Listen zugegriffen wird,
 * sollte immer synchronized() eingesetzt werden.
 * 
 * @author sylence
 *
 */
public class EntityCollection
{
	private final ArrayList<Entity> entities;
	private final ArrayList<ShipEntity> ships;
	private final ArrayList<PJUTPlayer> players;
	private final ArrayList<WorldEntity<PhysicContainer>> worldEntities;
	private final ArrayList<RocketEntity> rockets;
	private final ArrayList<ShotEntity> shots;
	private UTGameModeData gameData;
	
	/**
	 * Erzeugt eine noch leere EntityCollection.
	 */
	public EntityCollection()
	{
		entities = new ArrayList<Entity>();
		ships = new ArrayList<ShipEntity>();
		players = new ArrayList<PJUTPlayer>();
		worldEntities = new ArrayList<WorldEntity<PhysicContainer>>();
		rockets = new ArrayList<RocketEntity>();
		shots = new ArrayList<ShotEntity>();
		gameData = null;
	}
	
	/**
	 * Fügt den Entities das übergebene Objekt hizu, falls es nicht null ist.
	 * 
	 * @param e hinzuzufügende Entity
	 */
	public void addEntity(Entity e)
	{
		if(e != null)
		{
			synchronized(entities)
			{
				entities.add(e);
			}
		}
	}
	
	/**
	 * Entfernt die erste Entity, die mit dieser ID gefunden wird, falls
	 * vorhanden.
	 * 
	 * @param id ID der zu entfernenden Entity
	 */
	public void removeEntity(short id)
	{
		synchronized(entities)
		{
			int size = entities.size();
			for(int i = 0; i < size; ++i)
			{
				if(entities.get(i).getId() == id)
				{
					entities.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt eine Entity nach ihrer Referenz, falls vorhanden.
	 * Ignoriert null.
	 * 
	 * @param e zu entfernende Entity
	 */
	public void removeEntity(Entity e)
	{
		if(e != null)
		{
			synchronized(entities)
			{
				entities.remove(e);
			}
		}
	}
	
	/**
	 * Liefert, falls vorhanden, die Entity mit der angegebenen ID zurück,
	 * ansonsten null.
	 * 
	 * @param id ID der gesuchten Entity
	 * @return gesuchte Entity oder null
	 */
	public Entity getEntity(short id)
	{
		Entity e = null;
		
		synchronized(entities)
		{
			for(Entity ent : entities)
			{
				if(ent.getId() == id)
				{
					e = ent;
					break;
				}
			}
		}
		
		return e;
	}
	
	/**
	 * Fügt ein neues Schiff nur der Schiffsliste hinzu.
	 * Ignoriert null.
	 * 
	 * @param se hinzuzufügendes Schiff
	 */
	public void addShip(ShipEntity se)
	{
		if(se != null)
		{
			synchronized(ships)
			{
				ships.add(se);
			}
		}
	}
	
	/**
	 * Entfernt das erste gefundene Schiff, bei dem die ID des Piloten mit der
	 * übergebenen ID übereinstimmt, falls eines existiert.
	 * 
	 * @param pilotId ID des Piloten des gesuchten Schiffs
	 */
	public void removeShip(byte pilotId)
	{
		synchronized(ships)
		{
			int size = ships.size();
			for(int i = 0; i < size; ++i)
			{
				if(ships.get(i).getPilotId() == pilotId)
				{
					ships.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt das erste gefundene Schiff mit der übergebenen ID, falls eines
	 * existiert.
	 * 
	 * @param id ID der zu entfernenden Schiffs-Entity
	 */
	public void removeShip(short id)
	{
		synchronized(ships)
		{
			int size = ships.size();
			for(int i = 0; i < size; ++i)
			{
				if(ships.get(i).getId() == id)
				{
					ships.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt eine Schiffs-Entity nach ihrer Referenz, falls vorhanden.
	 * 
	 * @param se zu entfernendes Schiff
	 */
	public void removeShip(ShipEntity se)
	{
		synchronized(ships)
		{
			ships.remove(se);
		}
	}
	
	/**
	 * Liefert das erste Schiff mit der übergebenen Entity-ID, oder null, falls
	 * keines gefunden wird.
	 * 
	 * @param id ID des gesuchten Schiffs
	 * @return Schiff mit dieser ID oder null
	 */
	public ShipEntity getShip(short id)
	{
		ShipEntity se = null;
		
		synchronized(ships)
		{
			for(ShipEntity s : ships)
			{
				if(s.getId() == id)
				{
					se = s;
					break;
				}
			}
		}
		
		return se;
	}
	
	/**
	 * Liefert das erste Schiff mit der übergebenen Piloten-ID, oder null,
	 * falls keines vorhanden ist.
	 * 
	 * @param pilotId Piloten-ID des gesuchten Schiffs
	 * @return gefundenes Schiff oder null
	 */
	public ShipEntity getShip(byte pilotId)
	{
		ShipEntity se = null;
		
		synchronized(ships)
		{
			for(ShipEntity s : ships)
			{
				if(s.getPilotId() == pilotId)
				{
					se = s;
					break;
				}
			}
		}
		
		return se;
	}
	
	/**
	 * Fügt der internen Liste einen Spieler hinzu.
	 * Ignoriert null.
	 * 
	 * @param player hinzuzufügender Spieler
	 */
	public void addPlayer(PJUTPlayer player)
	{
		if(player != null)
		{
			synchronized(players)
			{
				players.add(player);
			}
		}
	}
	
	/**
	 * Entfernt den ersten gefundenen Spieler mit der übergebenen Client-ID,
	 * sofern vorhanden.
	 * 
	 * @param clientId Client-ID des zu entfernenden Spielers.
	 */
	public void removePlayer(byte clientId)
	{
		synchronized(players)
		{
			int size = players.size();
			for(int i = 0; i < size; ++i)
			{
				if(players.get(i).getClientId() == clientId)
				{
					players.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt den ersten gefundenen Spieler mit der angegebenen Entity-ID,
	 * sofern vorhanden.
	 * 
	 * @param id Entity-ID des zu entfernenden Spielers.
	 */
	public void removePlayer(short id)
	{
		synchronized(players)
		{
			int size = players.size();
			for(int i = 0; i < size; ++i)
			{
				if(players.get(i).getId() == id)
				{
					players.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt den übergebenen Spieler aus der internen Liste, falls
	 * vorhanden.
	 * 
	 * @param player zu entfernender Spieler
	 */
	public void removePlayer(PJUTPlayer player)
	{
		synchronized(players)
		{
			players.remove(player);
		}
	}
	
	/**
	 * Liefert den ersten Spieler mit der übergebenen Client-ID oder
	 * null, falls keiner gefunden wurde.
	 * 
	 * @param clientId Client-ID des gesuchten Spielers
	 * @return gesuchter Spieler oder null
	 */
	public PJUTPlayer getPlayer(byte clientId)
	{
		PJUTPlayer player = null;
		
		synchronized(players)
		{
			int size = players.size();
			for(int i = 0; i < size; ++i)
			{
				if(players.get(i).getClientId() == clientId)
				{
					player = players.get(i);
					break;
				}
			}
		}
		
		return player;
	}
	
	/**
	 * Liefert den ersten Spieler mit der übergebenen Entity-ID oder
	 * null, falls keiner gefunden wurde.
	 * 
	 * @param id Entity-ID des gesuchten Spielers
	 * @return gesuchter Spieler oder null
	 */
	public PJUTPlayer getPlayer(short id)
	{
		PJUTPlayer player = null;
		
		synchronized(players)
		{
			int size = players.size();
			for(int i = 0; i < size; ++i)
			{
				if(players.get(i).getId() == id)
				{
					player = players.get(i);
					break;
				}
			}
		}
		
		return player;
	}
	
	/**
	 * Fügt der internen Liste eine WorldEntity hinzu.
	 * Ignoriert null.
	 * 
	 * @param we hinzuzufügende WorldEntity
	 */
	public void addWorldEntity(WorldEntity<PhysicContainer> we)
	{
		if(we != null)
		{
			synchronized(worldEntities)
			{
				worldEntities.add(we);
			}
		}
	}
	
	/**
	 * Entfernt die erste gefundene WorldEntity mit der angegebenen ID,
	 * sofern vorhanden.
	 * 
	 * @param id ID der zu entfernenden WorldEntity
	 */
	public void removeWorldEntity(short id)
	{
		synchronized(worldEntities)
		{
			int size = worldEntities.size();
			for(int i = 0; i < size; ++i)
			{
				if(worldEntities.get(i).getId() == id)
				{
					worldEntities.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt die übergebene WorldEntity aus der internen Liste, sofern
	 * vorhanden.
	 * 
	 * @param we zu entfernende WorldEntity
	 */
	public void removeWorldEntity(WorldEntity<PhysicContainer> we)
	{
		synchronized(worldEntities)
		{
			worldEntities.remove(we);
		}
	}
	
	/**
	 * Liefert die erste gefundene WorldEntity mit der übergebenen ID zurück,
	 * oder null, falls keine existiert.
	 * 
	 * @param id ID der gesuchten WorldEntity
	 * @return gesuchte Entity oder null
	 */
	public WorldEntity<?> getWorldEntity(short id)
	{
		WorldEntity<?> we = null;
		
		synchronized(worldEntities)
		{
			int size = worldEntities.size();
			for(int i = 0; i < size; ++i)
			{
				if(worldEntities.get(i).getId() == id)
				{
					we = worldEntities.get(i);
					break;
				}
			}
		}
		
		return we;
	}
	
	/**
	 * Fügt die übergebene Rakete der internen Liste hinzu.
	 * Ignoriert null.
	 * 
	 * @param re
	 */
	public void addRocket(RocketEntity re)
	{
		synchronized(rockets)
		{
			rockets.add(re);
		}
	}
	
	/**
	 * Entfernt die erste gefundene Rakete mit der angegebenen ID, falls
	 * vorhanden.
	 * 
	 * @param id ID der zu entfernenden Rakete
	 */
	public void removeRocket(short id)
	{
		synchronized(rockets)
		{
			int size = rockets.size();
			for(int i = 0; i < size; ++i)
			{
				if(rockets.get(i).getId() == id)
				{
					rockets.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt eine Rakete aus der internen Liste, falls vorhanden.
	 * 
	 * @param re zu entfernende Rakete
	 */
	public void removeRocket(RocketEntity re)
	{
		synchronized(rockets)
		{
			rockets.remove(re);
		}
	}
	
	/**
	 * Liefert die erste gefundene Rakte mit der angegebenen ID, oder null,
	 * falls keine gefunden wurde.
	 * 
	 * @param id ID der gesuchten Rakete
	 * @return gesuchte Rakete oder null
	 */
	public RocketEntity getRocket(short id)
	{
		RocketEntity re = null;
		
		synchronized(rockets)
		{
			int size = rockets.size();
			for(int i = 0; i < size; ++i)
			{
				if(rockets.get(i).getId() == id)
				{
					re = rockets.get(i);
					break;
				}
			}
		}
		
		return re;
	}
	
	/**
	 * Fügt der internen Liste einen Schuss hinzu.
	 * Ingoriert null.
	 * 
	 * @param se hinzuzufügender Schuss
	 */
	public void addShot(ShotEntity se)
	{
		synchronized(shots)
		{
			shots.add(se);
		}
	}
	
	/**
	 * Entfernt den ersten gefundenen Schuss mit der angegebenen ID, falls
	 * vorhanden.
	 * 
	 * @param id ID des zu entfernenden Schusses
	 */
	public void removeShot(short id)
	{
		synchronized(shots)
		{
			int size = shots.size();
			for(int i = 0; i < size; ++i)
			{
				if(shots.get(i).getId() == id)
				{
					shots.remove(i);
					break;
				}
			}
		}
	}
	
	/**
	 * Entfernt den übergebenen Schuss aus der internen Liste, falls vorhanden.
	 * 
	 * @param se zu entfernender Schuss
	 */
	public void removeShot(ShotEntity se)
	{
		synchronized(shots)
		{
			shots.remove(se);
		}
	}
	
	/**
	 * Liefert den ersten gefundenen Schuss mit der angegebenen ID, oder null,
	 * falls keiner gefunden wurde.
	 * 
	 * @param id ID des gesuchten Schusses
	 * @return gesuchter Schuss oder null
	 */
	public ShotEntity getShot(short id)
	{
		ShotEntity se = null;
		
		synchronized(shots)
		{
			int size = shots.size();
			for(int i = 0; i < size; ++i)
			{
				if(shots.get(i).getId() == id)
				{
					se = shots.get(i);
					break;
				}
			}
		}
		
		return se;
	}
	
	/**
	 * @return aktuelle Spieldaten, oder null
	 */
	public UTGameModeData getGameData()
	{
		return gameData;
	}

	/**
	 * Setzt die aktuellen Spieldaten, akzeptiert auch null.
	 * 
	 * @param gameData zu setzende Spieldaten
	 */
	public void setGameData(UTGameModeData gameData)
	{
		this.gameData = gameData;
	}

	/**
	 * @return Liste aller eingetragenen Entities
	 */
	public List<Entity> getEntities()
	{
		return entities;
	}

	/**
	 * @return Liste aller eingetragenen Schiffe
	 */
	public List<ShipEntity> getShips()
	{
		return ships;
	}

	/**
	 * @return Liste aller eingetragenen Spieler
	 */
	public List<PJUTPlayer> getPlayers()
	{
		return players;
	}

	/**
	 * @return Liste aller eingetragenen WorldEntities
	 */
	public List<WorldEntity<PhysicContainer>> getWorldEntities()
	{
		return worldEntities;
	}

	/**
	 * @return Liste aller eingetragenen Raketen
	 */
	public List<RocketEntity> getRockets()
	{
		return rockets;
	}

	/**
	 * @return Liste aller eingetragenen Schüsse
	 */
	public List<ShotEntity> getShots()
	{
		return shots;
	}
}
