package de.fhhof.universe.client.core;

import java.awt.Dimension;
import java.awt.event.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.sun.javafx.newt.KeyEvent;
import com.sun.opengl.util.Animator;

import de.fhhof.universe.client.input.commands.CloseCommand;
import de.fhhof.universe.client.rendering.geometrie.packed.*;
import de.fhhof.universe.client.rendering.ressourcen.resmanagment.*;
import de.fhhof.universe.client.rendering.shaders.StandartShader;
import de.fhhof.universe.shared.logic.entities.ingame.WorldEntity;
import de.fhhof.universe.shared.logic.entities.ingame.container.*;
import de.fhhof.universe.shared.util.math.*;

/**
 * Kein Fenster im eigentlichen Sinne, sondern mehr eine Ansammlung der
 * Objekte, aus denen das Fenster aufgebaut ist.
 * Dient zur Initialisierung des Renderers und dem Zugriff auf dessen
 * Komponenten.
 * 
 * @author Daniel Heinrich
 *
 */
public class ClientWindow
{
	/**
	 * Minimalbreite für ein Fenster.
	 */
	public static final int HOR_MIN = 640;
	/**
	 * Maximalbreite für ein Fenster.
	 */
	public static final int HOR_MAX = 1920;
	/**
	 * Minimalhöhe für ein Fenster.
	 */
	public static final int VER_MIN = 480;
	/**
	 * Maximalhöhe für ein Fenster.
	 */
	public static final int VER_MAX = 1200;
	private GLCanvas canvas;
	private RenderFrame rFrame;
	private final Animator animator;
	private final FirstPersonCam fpCamera;

	/**
	 * Lässt ein Client-Fenster mit der angegebenen Größe erstellen.
	 * Zur Sicherheit werden Werte kleiner als HOR_MIN und VERT_MIN, sowie
	 * Werte größer als HOR_MAX und VER_MAX auf die jeweils näher liegende
	 * Grenze zurückgesetzt.
	 * Bindet außerdem den InputBuffer an das Fenster.
	 *
	 * @param xSize horizontale Größe in Pixeln
	 * @param ySize vertiakel Größe in Pixeln
	 * @param fullscreen ob der Vollbildmodus aktiv sein soll
	 */
	public ClientWindow(int xSize, int ySize, boolean fullscreen)
	{
		if (xSize < HOR_MIN)
			xSize = HOR_MIN;
		else if (xSize > HOR_MAX)
			xSize = HOR_MAX;

		if (ySize < VER_MIN)
			ySize = VER_MIN;
		else if (ySize > VER_MAX)
			ySize = VER_MAX;

		// TODO: Fullscreen
		createWindow(xSize, ySize);

		addListeners();

		// initiale Szene aufbauen
		fpCamera = new FirstPersonCam(canvas);
		setupScene();

		// Animator, der das Bild auffrischt
		animator = new Animator(canvas);
		animator.setRunAsFastAsPossible(false);
		animator.start();

		registerInput();
	}

	private void createWindow(int xSize, int ySize)
	{
		// OpenGL-Informationen holen
		GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setHardwareAccelerated(true);

		// Canvas ("Leinwand") und Frame
		canvas = new GLCanvas(capabilities);
		rFrame = new RenderFrame(canvas);
		rFrame.setSize(xSize, ySize);
		rFrame.setResizable(false);
		rFrame.setLocationRelativeTo(null);
		rFrame.setVisible(true);
		rFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		// Workaround, der bewirkt, dass der Canvas seine Größe anpasst.
		canvas.setMinimumSize(new Dimension());

		createClosingHandler();
	}

	private void createClosingHandler()
	{
		rFrame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				/*
				 * Zur Sicherheit in eigenem Thread ausführen, da das
				 * Swing-System blockieren könnte.
				 */
				new Thread(new Runnable()
				{
					public void run()
					{
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
	}

	private void addListeners()
	{
		// TODO: Parameter ...

		// interner Controller
		canvas.addGLEventListener(new RenderController(1000.f / 60.f));

		// Renderer
		BasicScene scene = Scene.getInstance();
		canvas.addGLEventListener(new GLRenderer(scene));
		canvas.addGLEventListener(scene);
	}

	private void setupScene()
	{
		// Kamera setzen
		setFirstPersonCam(0, -1, -5);

		// TODO: Grid wieder entfernen
		StandartShader stds = new StandartShader();
		RessourcesLoader.getInstance().loadShader(stds, "Grid.vert",
				"Grid.frag");

		TransformationStack t = new TransformationStack();
		RenderObjekt grid = new RObjekt2(stds, t);
		RessourcesLoader.getInstance().loadRenderObjekt(grid,
				new ObjConfig("Models/grid.obj"));
		Scene.getInstance().addRenderable(grid);
		ModelMatrix m = t.getMainMatrix();
		m.rotateEuler(-90, 0, 0);
		m.scale(90);
	}

	private void registerInput()
	{
		// Input-Puffer registrieren
		PJUTInputBuffer buffer = PJUTInputBuffer.getInstance();
		buffer.setCompX(canvas.getWidth());
		buffer.setCompY(canvas.getHeight());

		canvas.addMouseListener(buffer);
		canvas.addMouseMotionListener(buffer);
		canvas.addKeyListener(buffer);

		// Kommandos registrieren
		registerCommands();
	}

	private void registerCommands()
	{
		// TODO: Keybindings laden

		PJUTInputDistributor.getInstance().bindToKeyPress(KeyEvent.VK_ESCAPE,
				new CloseCommand(rFrame, animator));
	}

	public void setFollowCamera(WorldEntity we)
	{
		float radius = 10.f;
		Vec3 rotation = new Vec3(17.f, 0.f, 0.f), translation = null;
		
		TransformationContainer o = we.getTransformation();
		if(o instanceof PhysicContainer)
		{
			float size = ((PhysicContainer)o).getRadius();
			translation = new Vec3(0.f, 3.f, -size);
		}
		
		if(translation == null)
		{
			translation = new Vec3(0.f, 0, 5.f);
		}
		
		FollowCam cam = new FollowCam(we, radius, rotation, translation);
		Scene.getInstance().setCamera(cam);
	}

	public void setFirstPersonCam(float x, float y, float z)
	{
		fpCamera.setPosition(x, y, z);
		Scene.getInstance().setCamera(fpCamera);
	}

	public RenderFrame getFrame()
	{
		return rFrame;
	}

	public Animator getAnimator()
	{
		return animator;
	}
}
