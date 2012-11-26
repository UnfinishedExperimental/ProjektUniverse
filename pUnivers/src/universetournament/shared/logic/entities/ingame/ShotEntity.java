package universetournament.shared.logic.entities.ingame;

import universetournament.shared.data.proto.ProjectileConfig;
/**
 * Projektil-Entity, welche ein Blaster-Projektil im Spiel repräsentiert und
 * ihre Eigenschaften über eine ProjectileConfig erhält.
 * 
 * @author sylence
 *
 */
public class ShotEntity extends ProjectileEntity
{
	/**
	 * Erzeugt eine Blaster-Schuss-Entity, welche die angegebene ID und
	 * Eigentümer-ID übernimmt und ihre Eigenschaften über die ProjectileConfig
	 * erhält.
	 * Wirft eine NullPointerException, wenn die Konfiguration null ist.
	 * 
	 * @param id ID der Entity
	 * @param ownerId ID des Spielers, der das Projektil gefeuert aht
	 * @param configuration Konfiguration, die das Projektil definiert
	 */
	public ShotEntity(short id, byte ownerId,
			ProjectileConfig configuration)
	{
		super(id, ownerId, configuration);
	}
}
