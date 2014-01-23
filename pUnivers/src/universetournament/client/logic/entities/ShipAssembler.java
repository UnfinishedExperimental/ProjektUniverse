package universetournament.client.logic.entities;

import java.util.HashMap;

import universetournament.client.core.ClientMainController;
import universetournament.client.logic.entities.container.LinkedTransformation;
import universetournament.client.logic.entities.controller.*;
import universetournament.client.rendering.Renderable;
import universetournament.client.rendering.Scene;
import universetournament.client.rendering.geometrie.packed.*;
import universetournament.client.rendering.ressourcen.resmanagment.*;
import universetournament.client.rendering.shaders.ShieldShader;
import universetournament.client.util.io.obj.ObjObjektReader.Scale;
import universetournament.shared.events.GameEvent;
import universetournament.shared.events.util.PJUTEventHandler;
import universetournament.shared.logic.entities.*;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.controller.ShipDamageController;
import universetournament.shared.logic.entities.util.*;
import universetournament.shared.util.math.Vec3;

/**
 * Erhält vom Server erzeugte Schiff-Entites und übernimmt sie ins lokale
 * System des Clients oder entfernt sie wieder.
 * 
 * @author sylence
 *
 */
public class ShipAssembler implements PJUTEventHandler
{
	private final HashMap<Short, Renderable[]> renderables;
	
	public ShipAssembler()
	{
		renderables = new HashMap<Short, Renderable[]>();
	}
	
	@Override
	public void handleEvent(GameEvent event)
	{
		if (event != null && event.getData() instanceof EntityContainer)
		{
			EntityContainer container = (EntityContainer) event.getData();

			if (event.getSub() == Entity.SubEvents.CREATE
					&& container.getData() instanceof ShipEntity)
			{
				ShipEntity ship = (ShipEntity) container.getData();

				EntityController<ShipEntity> controller =
					new EntityController<ShipEntity>(ship);
				UTEntityManager.getInstance().setController(ship.getId(),
						controller);
	
				//Renderer
				try
				{
					RenderObjekt rend = ship.buildRenderObject();

                    ShieldShader shieldShader = new ShieldShader();
                    RessourcesLoader.getInstance().loadShader(
                        shieldShader, "Shield.vert", "Shield.frag");
                    LinkedTransformation lt = new LinkedTransformation(
                            rend.getTransf());
                    float r =
                        ship.getConfiguration().getPhysic_props().getRadius();
                    lt.scale(new Vec3(r, r, r));
                    RenderObjekt shield = new RObject3(shieldShader, lt);
                    ObjConfig oc = new ObjConfig("Models/uvsphere32.obj", true,
                    		Scale.WIDTH, 1);
                    RessourcesLoader.getInstance().loadRenderObjekt(
                                shield, oc, true);
                    PJUTPlayer p =ClientMainController.getInstance().
                            getEntities().getPlayer(ship.getPilotId());
                    float[] c = p.getColor().getRGBColorComponents(null);
                    shield.getModels()[0].getMaterial().setDiffuse(c);

                    addRenderable(ship.getId(), new Renderable[]
                        {rend, shield});

                    //Schild animieren
                    ShieldAnimator sa = new ShieldAnimator(shieldShader);
                    controller.addTimedController(sa);
				}
				catch (InstantiationException ex)
				{
					throw new RuntimeException(
							"RenderObject konnte nicht initialisiert werden." +
							"Fehler in der RenderConfiguration(\""
							+ ship.getConfiguration().getName() + "\")");
				}
				catch (IllegalAccessException ex)
				{
					throw new RuntimeException(
							"RenderObject konnte nicht initialisiert"
							+ " werden. Fehler in der RenderConfiguration(\""
							+ ship.getConfiguration().getName() + "\")");
				}

				//überprüfen ob eigenes Schiff
				if (ship.getPilotId() == ClientMainController.getInstance()
						.getClientId())
				{
                    HUDrefresher hudref = new HUDrefresher(ship);
                    controller.addTimedController(hudref);
					PlayerMovement mov = new PlayerMovement(ship);
					ClientMainController.getInstance().setPlayerShip(ship,
							mov);
					
					controller.addSubController(mov);
					controller.addTimedController(mov);
					controller.addTimedController(new CooldownController(
							ship));
					controller.addTimedController(new RechargeController(
							ship));
					controller.addSubController(new ShipDamageController(
							ship));
					controller.addSubController(new ShipResetController(
							ship));
				}
				else
				{
					LerpMovment mov = new LerpMovment(ship);
					
					controller.addSubController(mov);
					controller.addTimedController(mov);
				}
				
				ClientMainController.getInstance().getEntities().addShip(ship);
			}
			else if (event.getSub() == Entity.SubEvents.DELETE
					&& event instanceof EntityEvent)
			{
				short sid = ((EntityEvent) event).getId();
				
				UTEntityManager.getInstance().setController(sid, null);
				
				ClientMainController.getInstance().getEntities().
					removeShip(sid);
				removeRenderable(sid);
			}
		}
	}

	private void addRenderable(short sid, Renderable[] rend)
	{
		if(rend != null)
		{
			renderables.put(sid, rend);
			for (Renderable r : rend)
			{
				Scene.getInstance().addRenderable(r);
			}
		}
	}

	private void removeRenderable(short sid)
	{
		if(renderables.get(sid) != null)
		{
			for (Renderable r : renderables.get(sid))
			{
				Scene.getInstance().removeRenderable(r);
			}
			renderables.remove(sid);
		}
	}
}
