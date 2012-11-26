package universetournament.shared.data.proto;

public class PDecConfig extends WorldConfig
{
	private PhysicConfig pConf;
	
	public PDecConfig()
	{
		pConf = new PhysicConfig();
	}

	/**
	 * @param pConf the pConf to set
	 */
	public void setpConf(PhysicConfig pConf)
	{
		this.pConf = pConf;
	}

	/**
	 * @return the pConf
	 */
	public PhysicConfig getpConf()
	{
		return pConf;
	}
}
