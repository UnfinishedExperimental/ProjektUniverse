package de.fhhof.universe.shared.util.io;

import com.thoughtworks.xstream.XStream;
import darwin.resourcehandling.handle.ResourceHandle;
import darwin.resourcehandling.handle.UrlHandle;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasse, deren Instanzen mit hilfe von XStream Objekte im XML-Format von der
 * Festplatte lesen können.
 *
 * @author Daniela Geilert
 *
 */
public class UTXMLReader {

    private static final Logger logger = Logger.getLogger(
            UTXMLReader.class.getName());
    private final XStream xstream;

    /**
     * Erzeugt einen neuen XMLReader mit eigener XStream-Instanz.
     */
    public UTXMLReader() {
        xstream = new XStream();
    }

    /**
     * Liest ein als XML gespeichertes Objekt von der Festplatte und gibt dessen
     * Repräsentation als Ergebnis, wenn es eine Instanz der gewünschten Klasse
     * ist, zurück. Gibt null zurück, falls etwas schiefgeht. Ignoriert Aufrufe,
     * bei denen Parameter null sind.
     *
     * @param <T> Gewünschter Typ des zu lesenden Objekts
     * @param cls Klasse des gewünschten Typs
     * @param url zu lesende Datei
     * @return gelesenes Objekt vom angegebenen Tpy oder null
     */
    
    public <T> T read(Class<T> cls, ResourceHandle url) {
        T result = null;

        if (cls != null && url != null) {
            Object incoming = null;

            try(InputStream is = url.getStream()) {
                incoming = xstream.fromXML(is);
            } catch (Exception e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, url.getName() + " konnte nicht gelesen werden.");
            }

            // nur casten, wenn möglich
            if (cls.isInstance(incoming)) {
                result = cls.cast(incoming);
                /*
                 * Eclipse erzeugt hier keine Warnings, Netbeans bei der
                 * Variante mit dem direkten Cast.
                 * Das Ergebnis ist aber effektiv das gleiche.
                 */
            } else {
                logger.log(Level.SEVERE, "Gelesenes Objekt ist nicht vom"
                        + "gewünschten Typ " + cls.getCanonicalName());
            }
        }
        return result;
    }
}
