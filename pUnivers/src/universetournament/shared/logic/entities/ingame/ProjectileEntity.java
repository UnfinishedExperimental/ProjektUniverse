package universetournament.shared.logic.entities.ingame;


import universetournament.shared.data.proto.ProjectileConfig;
import universetournament.shared.logic.entities.ingame.container.PhysicContainer;
import universetournament.shared.logic.entities.ingame.container.SimplePhysic;

/**
 * Entity, welche ein Projektil im Spiel repräsentiert und ihre Eigenschaften
 * über eine ProjectileConfig erhält.
 * Sie besitzt ein Alter und die ID ihres "Eigentümers", also des Spielers, der
 * das Projektil abgefeuert hat.
 * 
 * @author sylence
 *
 */
public class ProjectileEntity extends WorldEntity<PhysicContainer>
{
	private final byte ownerId;
	
	//interessiert den Client erstmal nicht
	transient private double age;
	
	/**
	 * Erzeugt eine Projektil-Entity mit der angegebenen ID, dem angegebenen
	 * Eigentümer und der übergebenen Konfiguration.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 * 
	 * @param id ID der Entity
	 * @param ownerId ID des Spielers, der das Projektil gefeuert aht
	 * @param configuration Konfiguration, die das Projektil definiert
	 */
	public ProjectileEntity(short id, byte ownerId,
			ProjectileConfig configuration)
	{
		super(id, configuration, new SimplePhysic(
				configuration.getPhysic_props()));
		this.ownerId = ownerId;
	}

	/**
	 * @return ID des Spielers, der das Projektil abgefeuert hat
	 */
	public byte getOwnerId()
	{
		return ownerId;
	}

	/**
	 * @param age neues Alter des Projektils
	 */
	public void setAge(double age)
	{
		this.age = age;
	}
	
	/**
	 * @param diff um wie viel das Alter erhöht werden soll
	 */
	public void increaseAge(double diff)
	{
		age += diff;
	}

	/**
	 * @return alter des Projektils
	 */
	public double getAge()
	{
		return age;
	}

	@Override
	public ProjectileConfig getConfiguration()
	{
		return (ProjectileConfig) super.getConfiguration();
	}
}
