package de.fhhof.universe.shared.data.proto.util;

import darwin.resourcehandling.dependencies.ResourceInjector;
import java.io.File;

import de.fhhof.universe.shared.data.proto.Config;
import de.fhhof.universe.shared.util.io.UTXMLReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
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

    private static final String STD_PATH = "data/configs";

    private final ResourceInjector injector;

    private final Map<Short, Config> configurations = new HashMap<>();

    @Inject
    public ConfigurationLookUp(ResourceInjector injector) {
        this.injector = injector;
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

    /**
     * Liest alle in der Liste befindlichen Konfigurationen ein, bei denen dies
     * möglich ist und trägt sie in den ConfigurationManager ein.
     */
    private void readAll() {

//        UTXMLReader reader = new UTXMLReader();
//        String[] paths = reader.read(String[].class, configList);
//
//        if (paths != null) {
//            File config = null;
//
//            for (String path : paths) {
//                config = new File(path);
//                readConfig(config);
//            }
//        }
    }

    String[] getResourceListing(String path) throws URISyntaxException, IOException {
        String me = ConfigurationLookUp.class.getName().replace(".", "/") + ".class";
        URL dirURL = ConfigurationLookUp.class.getClassLoader().getResource(me);

        if (dirURL.getProtocol().equals("jar")) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    String entry = name.substring(path.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }

    private void readConfig(File config) {
        UTXMLReader reader = new UTXMLReader();
        Config c = reader.read(Config.class, config);
        if (c != null) {
            configurations.put(c.getUid(), c);
        }
    }
}
