package universetournament.server.logic.controller;

import java.util.List;

import universetournament.server.core.ServerMainController;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.Entity;
import universetournament.shared.logic.entities.EntityEvent;
import universetournament.shared.logic.entities.EntityType;
import universetournament.shared.logic.entities.ingame.ProjectileEntity;
import universetournament.shared.logic.entities.util.EntityCollection;
import universetournament.shared.logic.entities.util.EntityContainer;
import universetournament.shared.logic.entities.util.UTEntityManager;

/**
 * Controller, welcher im Alter beschränkte Objekte, also Blaster-Schüsse und
 * Raketen nach Ablauf ihrer Lebenszeit löscht.
 * 
 * @author Florian Holzschuher
 *
 */
public class AgeController implements PJUTTimedRefreshable
{
	private final EntityCollection entities;
	
	/**
	 * Erzeugt einen Alterungs-Controller welcher seine Entities aus der
	 * EntityCollection des ServerMainControllers bezieht.
	 */
	public AgeController()
	{
		entities = ServerMainController.getInstance().getEntities();
	}
	
	@Override
	public void refresh(float timeDiff)
	{
		timeDiff /= 1000000000.f;
		
		//Blaster-Schüsse
		List<? extends ProjectileEntity> projectiles = entities.getShots();
		for(ProjectileEntity pe : projectiles)
		{
			pe.increaseAge(timeDiff);
			if(pe.getAge() > pe.getConfiguration().getLifeTime())
			{
				deleteEntity(pe.getId(), EntityType.SHOT);
			}
		}
		
		//Raketen
		projectiles = entities.getRockets();
		for(ProjectileEntity pe : projectiles)
		{
			pe.increaseAge(timeDiff);
			if(pe.getAge() > pe.getConfiguration().getLifeTime())
			{
				deleteEntity(pe.getId(), EntityType.ROCKET);
			}
		}
	}
	
	private void deleteEntity(short id, EntityType et)
	{
		EntityContainer container = new EntityContainer(et, null);
		EntityEvent ee = new EntityEvent(id, Entity.SubEvents.DELETE,
				container);
		UTEntityManager.getInstance().handleEvent(ee);
	}
}
