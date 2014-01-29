package de.fhhof.universe.server.logic.controller;

import java.util.List;

import de.fhhof.universe.server.core.ServerMainController;
import de.fhhof.universe.shared.logic.PJUTTimedRefreshable;
import de.fhhof.universe.shared.logic.entities.Entity;
import de.fhhof.universe.shared.logic.entities.EntityEvent;
import de.fhhof.universe.shared.logic.entities.EntityType;
import de.fhhof.universe.shared.logic.entities.ingame.ProjectileEntity;
import de.fhhof.universe.shared.logic.entities.util.EntityCollection;
import de.fhhof.universe.shared.logic.entities.util.EntityContainer;
import de.fhhof.universe.shared.logic.entities.util.UTEntityManager;

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
