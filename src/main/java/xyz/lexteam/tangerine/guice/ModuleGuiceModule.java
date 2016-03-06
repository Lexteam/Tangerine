/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.guice;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import xyz.lexteam.tangerine.Tangerine;

/**
 * The module guice module :P
 */
public class ModuleGuiceModule extends AbstractModule {

    private final Logger logger;
    private final Tangerine tangerine;

    public ModuleGuiceModule(Tangerine tangerine, Logger logger) {
        this.tangerine = tangerine;
        this.logger = logger;
    }

    @Override
    protected void configure() {
        this.bind(Tangerine.class).toInstance(this.tangerine);
        this.bind(Logger.class).toInstance(this.logger);
    }
}
