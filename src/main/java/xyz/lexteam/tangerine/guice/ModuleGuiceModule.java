/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.guice;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.module.Module;

import java.io.File;

/**
 * The module guice module :P
 */
public class ModuleGuiceModule extends AbstractModule {

    private final Logger logger;
    private final Tangerine tangerine;
    private final File configDir;

    public ModuleGuiceModule(Tangerine tangerine, Module module) {
        this.tangerine = tangerine;
        this.logger = LoggerFactory.getLogger(module.name());
        this.configDir = new File("config", module.id());
    }

    @Override
    protected void configure() {
        this.bind(Tangerine.class).toInstance(this.tangerine);
        this.bind(Logger.class).toInstance(this.logger);
        this.bind(File.class).annotatedWith(ConfigDir.class).toInstance(this.configDir);
    }
}
