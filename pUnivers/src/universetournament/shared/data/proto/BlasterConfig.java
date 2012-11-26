package universetournament.shared.data.proto;

/**
 * Konfiguration für einen Blaster im Spiel, welcher den Energiebedarf pro
 * Schuss und die Konfiguration für die Schüsse über ihre ID kennt.
 * 
 * @author Thikimchi Nguyen
 *
 */
public class BlasterConfig extends WeaponConfig
{
    private final short energyDrain;
    private final short shotConfig;

    /**
     * Erzeugt eine Blaster-konfiguration mit der angegebenen ID, bei der
     * der Energiebedarf und die ShotConfig-ID 0 betragen.
     * Sollte nur zur Initialisierung verwendet werden, da tatsächliche
     * Konfigurationen über extern geladen werden.
     *
     * @param uid eindeutige ID
     */
    public BlasterConfig()
    {
        energyDrain = 0;
        shotConfig = 0;
    }

    /**
     * @return wie viel Energie jeder Schuss benötigt
     */
    public short getEnergyDrain()
    {
        return energyDrain;
    }

    /**
     * @return UID der Konfiguration eines Blaster-Projektils
     */
    public short getShotConfig()
    {
        return shotConfig;
    }
}
