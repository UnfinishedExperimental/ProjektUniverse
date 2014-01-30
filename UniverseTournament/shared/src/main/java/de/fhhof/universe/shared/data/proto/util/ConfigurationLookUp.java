package de.fhhof.universe.shared.data.proto.util;

import com.google.inject.Guice;
import com.google.inject.Injector;
import darwin.resourcehandling.dependencies.ResourceHandlingModul;
import darwin.resourcehandling.dependencies.ResourceInjector;
import darwin.resourcehandling.handle.ClasspathHelper;

import de.fhhof.universe.shared.data.proto.Config;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Einfache Helferklasse, die lediglich die Konfigurationen von der Festplatte
 * einliest und in den ConfigurationManager einträgt. Er liest dazu ein Array
 * von Datei-Pfaden ein, die jeweils eine Konfiguration enthalten (XML-Format).
 *
 * @author Florian Holzschuher
 *
 */
@Singleton
public class ConfigurationLookUp {

    private static final String STD_PATH = "resources/data/configs/";

    private final Map<Short, Config> configurations = new HashMap<>();

    @Inject
    public ConfigurationLookUp(ResourceInjector injector) {
        try {
            Collection<URL> configs = ClasspathHelper.elementsOfFolder(STD_PATH);
            for (URL url : configs) {
                System.out.println(Paths.get(url.));
                Config conf = injector.get(new ConfigLoader(), url.getPath().substring(1));
                configurations.put(conf.getUid(), conf);
            }
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(ConfigurationLookUp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Sieht nach, ob unter der angegebenen ID eine Konfiguration gespeichert
     * ist und ob diese eine Instanz der gewüschten Klasse ist. Trifft beides
     * zu, wird die Konfiguration zurückgegeben, wenn nicht wird null
     * zurückgegeben.
     *
     * @param <T> Konfigurationstyp der benötigt wird.
     * @param cls angeforderte Klasse (zur Überprüfung)
     * @param uid eindeutige ID der angefragten Konfiguration
     * @return benötigte Konfiguration oder null
     */
    public <T extends Config> T getConfiguration(Class<T> cls, short uid) {
        Config c = configurations.get(uid);
        T result = null;

        if (cls.isInstance(c)) {
            result = cls.cast(c);
        }

        return result;
    }

    public static void main(String[] args) {
        Injector inj = Guice.createInjector(new ResourceHandlingModul());
        ConfigurationLookUp lp = inj.getInstance(ConfigurationLookUp.class);
    }
}
