package de.fhhof.universe.client.core;


import de.fhhof.universe.client.UTClient;
import de.fhhof.universe.client.rendering.ressourcen.resmanagment.RessourcesLoader;

/**
 * Controller, der das Refresh von OpenGL in den Core leitet.
 * 
 * @author Daniel Heinrich
 *
 */
public class RenderController implements GLEventListener
{
	private float expFrLen;
	private PJUTClientCore core;
	
	/**
	 * Erzeugt den RenderController mit der angegebenen erwarteten
	 * Frame-Länge.
	 * 
	 * @param expFrLen erwartete Tick-Länge in Millisekunden.
	 */
	public RenderController(float expFrLen)
	{
		this.expFrLen = expFrLen;
		core = UTClient.getCore();
	}
	
	@Override
	public void display(GLAutoDrawable arg0)
	{
		core.refresh();
                RessourcesLoader.getInstance().workAllJobs();
	}

	@Override
	public void dispose(GLAutoDrawable arg0)
	{
		//ungenutzt
	}

	@Override
	public void init(GLAutoDrawable arg0)
	{
		core.initialize(expFrLen * 1000000.f);
	}

	@Override
	public void reshape(GLAutoDrawable arg0, int arg1, int arg2, int arg3,
			int arg4)
	{
		//ungenutzt
	}

}
