package universetournament.shared.logic.entities.ingame.controller;

import universetournament.shared.events.GameEvent;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.controller.CollisionType;
import universetournament.shared.logic.entities.ingame.ProjectileEntity;
import universetournament.shared.logic.entities.ingame.container.DamageContainer;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.EntitySubController;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * Kollisions-Controller für einen Blaster-Schuss, der dem getroffenen Objekt
 * einen gewissen Schaden zufügt und den Schuss löschen lässt.
 * 
 * @author sylence
 *
 */
public class ProjectileCollisionController
	extends EntitySubController<ProjectileEntity>
{
	private final DamageContainer container;
	
	public ProjectileCollisionController(ProjectileEntity entity)
	{
		super(entity);
		container =
			new DamageContainer(entity.getConfiguration().getHullDamage(),
			entity.getConfiguration().getShieldDamage(), entity.getOwnerId());
	}

	@Override
	public void handleEvent(GameEvent event)
	{
		if(event.getData() instanceof Short)
		{
			short id = (Short)event.getData();
			
			//Schaden übermitteln
			EntityEvent ee = new EntityEvent(id, CollisionType.DAMAGE,
					container);
			UTEntityManager.getInstance().handleEvent(ee);
			
			//Schuss löschen
			EntityContainer ec = new EntityContainer(EntityType.SHOT, null);
			ee = new EntityEvent(entity.getId(), Entity.SubEvents.DELETE, ec);
			UTEntityManager.getInstance().handleEvent(ee);
		}
	}

	@Override
	public SubType[] getTypes()
	{
		SubType types[] = {CollisionType.NOTIFY};
		return types;
	}

}
