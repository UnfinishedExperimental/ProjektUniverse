package de.fhhof.universe.server.config;


import de.fhhof.universe.shared.logic.gamemode.GameModeType;

/**
 * Internes Konfigurationsobjekt des Servers.
 * 
 * @author Daniela Geilert
 *
 */
public class ServerConfig
{
	private static ServerConfig instance = new ServerConfig();
	
	private int port;
	private String password;
	
	private GameModeType gameType;
	private double timeLimit;
	private short scoreLimit;
	
	private byte playerLimit;
	
	/**
	 * @return derzeitige Konfiguration
	 */
	public static synchronized ServerConfig getInstance()
	{
		return instance;
	}
	
	/**
	 * Setzt die aktuelle Konfiguration, falls diese ungleich null ist.
	 * 
	 * @param sc neue Konfiguration
	 */
	public static synchronized void setInstance(ServerConfig sc)
	{
		if(sc != null)
		{
			instance = sc;
		}
	}
	
	private ServerConfig()
	{
		port = 6667;
		password = "";
		gameType = GameModeType.DEATH_MATCH;
		
		timeLimit = 300.;
		scoreLimit = 20;
		
		playerLimit = 8;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port)
	{
		this.port = port;
	}

	/**
	 * @return the port
	 */
	public int getPort()
	{
		return port;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @return the password
	 */
	public String getPassword()
	{
		return password;
	}

	/**
	 * @param gameType the gameType to set
	 */
	public void setGameType(GameModeType gameType)
	{
		this.gameType = gameType;
	}

	/**
	 * @return the gameType
	 */
	public GameModeType getGameType()
	{
		return gameType;
	}

	/**
	 * @param playerLimit the playerLimit to set
	 */
	public void setPlayerLimit(byte playerLimit)
	{
		this.playerLimit = playerLimit;
	}

	/**
	 * @return the playerLimit
	 */
	public byte getPlayerLimit()
	{
		return playerLimit;
	}

	/**
	 * @param scoreLimit the scoreLimit to set
	 */
	public void setScoreLimit(short scoreLimit)
	{
		this.scoreLimit = scoreLimit;
	}

	/**
	 * @return the scoreLimit
	 */
	public short getScoreLimit()
	{
		return scoreLimit;
	}

	/**
	 * @param timeLimit the timeLimit to set
	 */
	public void setTimeLimit(double timeLimit)
	{
		this.timeLimit = timeLimit;
	}

	/**
	 * @return the timeLimit
	 */
	public double getTimeLimit()
	{
		return timeLimit;
	}
}
