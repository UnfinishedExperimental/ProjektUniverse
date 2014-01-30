/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.fhhof.universe.shared.data.proto.util;

import darwin.annotations.ServiceProvider;
import darwin.resourcehandling.factory.ResourceFromHandle;
import darwin.resourcehandling.factory.ResourceFromHandleProvider;
import darwin.resourcehandling.handle.ResourceHandle;
import de.fhhof.universe.shared.data.proto.Config;
import de.fhhof.universe.shared.util.io.UTXMLReader;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author hein
 */
@ServiceProvider(ResourceFromHandleProvider.class)
public class ConfigLoader extends ResourceFromHandleProvider<Config> implements ResourceFromHandle<Config> {

    private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
        
    public ConfigLoader() {
        super(Config.class);
    }

    @Override
    public void update(ResourceHandle rh, Config t) {
        logger.warn("No update logic implemented for Configs. Tryed to update config: "+ rh.getName());
    }

    @Override
    public Config create(ResourceHandle rh) throws IOException {  
        logger.info("loading Config: "+ rh.getName());      
        UTXMLReader reader = new UTXMLReader();
        return reader.read(Config.class, rh);
    }

    @Override
    public Config getFallBack() {
        return new Config() {
        };
    }

    @Override
    public ResourceFromHandle<Config> get(String[] strings) {
        return this;
    }
}
