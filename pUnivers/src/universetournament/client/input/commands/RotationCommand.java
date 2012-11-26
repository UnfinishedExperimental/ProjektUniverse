package universetournament.client.input.commands;

import universetournament.client.input.PJUTInputBuffer;
import universetournament.client.logic.entities.controller.PlayerMovement;
import universetournament.shared.logic.PJUTTimedRefreshable;
import universetournament.shared.logic.entities.ingame.ShipEntity;
import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import universetournament.shared.util.math.Vec3;

/**
 * Kommando, welches das Schiff zur Rotation in eine Richtung veranlasst,
 * wobei die Richtung von der Position des Mauszeigers abhängt.
 * Muss nicht gebunden werden, holt sich seine Daten selbst.
 * 
 * @author Florian Holzschuher
 *
 */
public class RotationCommand implements PJUTTimedRefreshable
{
	private final TransformationContainer container;
	private final PlayerMovement pm;
	private final Vec3 accelRot;
	private final PJUTInputBuffer buffer;
	
	public RotationCommand(ShipEntity se, PlayerMovement pm)
	{
		if(se == null)
		{
			throw new NullPointerException("Schiff war null");
		}
		if(pm == null)
		{
			throw new NullPointerException("Movement war null");
		}
		
		this.pm = pm;
		container = se.getTransformation();
		accelRot = new Vec3(0.f, se.getConfiguration().getAccRot(), 0.f);
		buffer = PJUTInputBuffer.getInstance();
	}

	@Override
	public void refresh(float timeDiff)
	{
            timeDiff /= 1000000000f;

		float pos = -buffer.getMouseRelX();
		
		/*
		 * die übergebene Zeit lang beschleunigen, je nach Mausposition mehr
		 * oder weniger stark.
		 */
		Vec3 rotation = accelRot.mult(timeDiff*pos);
		container.rotateEuler(rotation);
		pm.setRotation(rotation);
	}
}
