package universetournament.client.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Internes Konfigurationsobjekt des Clients.
 * 
 * @author Daniela Geilert
 *
 */
public class ClientConfig
{
	private static ClientConfig instance = new ClientConfig();
	
	private int xResolution, yResolution;
	private float targetFps, frameLimit;
	
	private String host;
	private int port;
	
	private String playerName;
	private int playerColor;
	private short shipConfig;
	
	private KeyBindings bindings;
	
	private transient String password;
	
	private List<Short> ships;
	
	/**
	 * @return Instanz der Konfiguration
	 */
	public static synchronized ClientConfig getInstance()
	{
		return instance;
	}
	
	/**
	 * Setzt die registrierte Konfigurationsinstanz auf das übergebene Objekt,
	 * falls dieses nicht null ist.
	 * 
	 * @param c neue Konfigurationsinstanz
	 */
	public static synchronized void setInstance(ClientConfig c)
	{
		if(c != null)
		{
			instance = c;
		}
	}
	
	private ClientConfig()
	{
		xResolution = 1024;
		yResolution = 768;
		
		targetFps = 60.f;
		frameLimit = 100.f;
		
		host = "localhost";
		port = 6667;
		
		playerName = "Player";
		playerColor = 0;
		shipConfig = 1;
		
		bindings = new KeyBindings();
		
		password = "";
		ships = new ArrayList<Short>();
	}

	/**
	 * @return konfigurierte Keybindings.
	 */
	public KeyBindings getBindings()
	{
		return bindings;
	}

	/**
	 * @param xResolution the xResolution to set
	 */
	public void setxResolution(int xResolution)
	{
		this.xResolution = xResolution;
	}

	/**
	 * @return the xResolution
	 */
	public int getxResolution()
	{
		return xResolution;
	}

	/**
	 * @param yResolution the yResolution to set
	 */
	public void setyResolution(int yResolution)
	{
		this.yResolution = yResolution;
	}

	/**
	 * @return the yResolution
	 */
	public int getyResolution()
	{
		return yResolution;
	}

	/**
	 * @param targetFps the targetFps to set
	 */
	public void setTargetFps(float targetFps)
	{
		this.targetFps = targetFps;
	}

	/**
	 * @return the targetFps
	 */
	public float getTargetFps()
	{
		return targetFps;
	}

	/**
	 * @param frameLimit the frameLimit to set
	 */
	public void setFrameLimit(float frameLimit)
	{
		this.frameLimit = frameLimit;
	}

	/**
	 * @return the frameLimit
	 */
	public float getFrameLimit()
	{
		return frameLimit;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host)
	{
		this.host = host;
	}

	/**
	 * @return the host
	 */
	public String getHost()
	{
		return host;
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
	 * @param playerName the playerName to set
	 */
	public void setPlayerName(String playerName)
	{
		this.playerName = playerName;
	}

	/**
	 * @return the playerName
	 */
	public String getPlayerName()
	{
		return playerName;
	}

	/**
	 * @param playerColor the playerColor to set
	 */
	public void setPlayerColor(int playerColor)
	{
		this.playerColor = playerColor;
	}

	/**
	 * @return the playerColor
	 */
	public int getPlayerColor()
	{
		return playerColor;
	}

	/**
	 * @param shipConfig the shipConfig to set
	 */
	public void setShipConfig(short shipConfig)
	{
		this.shipConfig = shipConfig;
	}

	/**
	 * @return the shipConfig
	 */
	public short getShipConfig()
	{
		return shipConfig;
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
	 * @return Nummern der anzuzeigenden Schiffskonfigurationen
	 */
	public List<Short> getShips()
	{
		return ships;
	}
}
