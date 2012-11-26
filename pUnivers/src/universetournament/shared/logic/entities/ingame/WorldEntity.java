package universetournament.shared.logic.entities.ingame;

import universetournament.shared.logic.entities.ingame.container.TransformationContainer;
import java.io.IOException;
import universetournament.client.rendering.geometrie.packed.RenderObjekt;
import universetournament.client.rendering.ressourcen.resmanagment.RessourcesLoader;
import universetournament.client.rendering.shaders.StandartShader;
import universetournament.shared.data.proto.RenderConfig;
import universetournament.shared.data.proto.WorldConfig;
import universetournament.shared.data.proto.util.ConfigurationManager;
import universetournament.shared.events.SubType;
import universetournament.shared.logic.entities.Entity;

/**
 * Entity in der Spielwelt, welches eine Position und einen Bewegungsvektor
 * besitzt und über eine WorldConfig ein Model zugeordnet hat.
 * 
 * @author sylence
 *
 */
public class WorldEntity<T extends TransformationContainer>  extends Entity
{
    public enum SubEvents implements SubType{
        MOVE;
    }

    private transient WorldConfig configuration;
    private transient T transformation;

    /**
     * Erzeugt eine WorldEntity mit der übergebenen ID und Konfiguration.
     * Wirft eine NullPointerException, wenn die Konfiguration null ist.
     *
     * @param id ID der Entity
     * @param configuration Konfiguration der zu erstellenden Entity
     */
    public WorldEntity(short id, WorldConfig configuration,
                       T tcon) {
        super(id);
        if (configuration == null)
            throw new NullPointerException("keine gültige Konfiguration");
        this.configuration = configuration;
        transformation = tcon;
    }

    /**
     * @return Konfiguration, die die Entity definiert
     */
    public WorldConfig getConfiguration() {
        return configuration;
    }

    /**
     * @return transformation container which holds position and other transformation of the object
     */
    public T getTransformation() {
        return transformation;
    }
    
    public RenderObjekt buildRenderObject() throws InstantiationException,
			IllegalAccessException
	{
                RenderConfig rconfig = configuration.getRenderConfig();
		StandartShader s = rconfig.getShaderClass().newInstance();
		RenderObjekt r = rconfig.getROClass().newInstance();
		r.setShader(s);
		r.setTransf(transformation);
		RessourcesLoader.getInstance().loadShader(s,
				rconfig.getShader_vertex(), rconfig.getShader_frag());
		RessourcesLoader.getInstance()
				.loadRenderObjekt(r, rconfig.getObjConf());
		return r;
	}

    private void readObject(java.io.ObjectInputStream in) throws IOException,
                                                                 ClassNotFoundException {
        short configid = in.readShort();
        WorldConfig config = ConfigurationManager.getInstance().getConfiguration(
                WorldConfig.class, configid);
        if (config == null)
            throw new IOException("Konfiguration nicht gefunden");

        configuration = config;
        transformation = (T) in.readObject();
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeShort(configuration.getUid());
        out.writeObject(transformation);
    }
}
