package de.fhhof.universe.shared.data.proto;

/**
 * Konfiguration für ein Objekt, welches eine Masse besitzt.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class PhysicConfig
{
    private final float mass;
    private final float radius;

    /**
     * Erstellt eine Objektkonfiguration mit der angegebenen ID und
     * der Masse 0.
     * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
     * Konfigurationen über extern geladen werden.
     *
     * @param uid eindeutige ID
     */
    public PhysicConfig() {
        mass = 0.f;
        radius = 0.f;
    }

    /**
     * @return Masse des Objekts
     */
    public float getMass() {
        return mass;
    }

    public float getRadius() {
        return radius;
    }
}
