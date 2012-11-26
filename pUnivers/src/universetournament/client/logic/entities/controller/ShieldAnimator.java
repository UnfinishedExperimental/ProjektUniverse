package universetournament.client.logic.entities.controller;

import universetournament.client.rendering.shaders.ShieldShader;
import universetournament.shared.logic.PJUTTimedRefreshable;

/**
 * Refreshable, welches ein Schild um ein Schiff animiert
 * 
 * @author sylence
 *
 */
public class ShieldAnimator implements PJUTTimedRefreshable
{
	//Bestimmt die Geschwindigkeit der Schildanimation
	private final static float OFF_FACT = 1/500000000.f;
	private final ShieldShader shader;
	private float offset;
	
	/**
	 * Erzeugt einen Schildanimator, der bei jedem refresh, das Schild
	 * weiteranimiert.
	 * Wirft eine NullPointerException, wenn das übergebene Schild null ist.
	 * 
	 * @param shader zu animierender Schild-Shader
	 */
	public ShieldAnimator(ShieldShader shader)
	{
		if(shader == null)
		{
			throw new NullPointerException("Schild-Shader war null");
		}
		this.shader = shader;
		offset = 0.f;
	}
	
	@Override
	public void refresh(float timeDiff)
	{
		offset += timeDiff * OFF_FACT;
		shader.setOffset(offset);
	}
}
