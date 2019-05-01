package com.xbank.core.injector;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(new Binder());
        packages("com.xbank.core.api");
        property(ServerProperties.MEDIA_TYPE_MAPPINGS, "json : application/json");

    }
}
