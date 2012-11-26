package universetournament.shared.logic.entities.ingame;


import universetournament.shared.data.proto.RocketConfig;

/**
 * Projektil-Entity, die eine Rakete darstellt und ihre Eigentschaften über
 * eine RocketConfig erhält.
 * 
 * @author sylence
 *
 */
public class RocketEntity extends ProjectileEntity
{	
	/**
	 * Erzeugt eine Raketen-Entity mit der angegebenen ID, dem angegebenen
	 * Eigentümer und der übergebenen Konfiguration.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 * 
	 * @param id ID der Entity
	 * @param ownerId ID des Spielers, der das Projektil gefeuert aht
	 * @param configuration Konfiguration, die das Projektil definiert
	 */
	public RocketEntity(short id, byte ownerId, RocketConfig configuration)
	{
		super(id, ownerId, configuration);
	}
	
	@Override
	public RocketConfig getConfiguration()
	{
		return (RocketConfig) super.getConfiguration();
	}
}
