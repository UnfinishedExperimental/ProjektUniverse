package de.fhhof.universe.shared.data.proto;

/**
 * Konfiguration für ein Objekt, welches optisch in der Spielwelt existiert.
 * Sie hält ein Model zur Darstellung bereit.
 * 
 * @author Daniel Heinrich
 *
 */
public class WorldConfig extends Config
{
    private final RenderConfig renderconfig;

    public WorldConfig() {
        renderconfig = new RenderConfig();
    }

    public RenderConfig getRenderConfig() {
        return renderconfig;
    }
    
}
