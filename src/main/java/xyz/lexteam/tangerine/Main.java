/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.lexteam.tangerine.data.ConfigModel;
import xyz.lexteam.tangerine.util.JsonUtils;

import java.io.File;
import java.util.Optional;

/**
 * The main class, where the entry point is.
 */
public final class Main {

    public static final Logger LOGGER = LoggerFactory.getLogger("Tangerine");

    private ConfigModel config;

    private Main() {
        Optional<ConfigModel> configModel = JsonUtils.readModelFromFile(new File("config.json"), ConfigModel.class);
        if (configModel.isPresent()) {
            LOGGER.debug("Successfully loaded config.");
            this.config = configModel.get();
        } else {
            LOGGER.warn("Config doesn't exist! EXITING");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
