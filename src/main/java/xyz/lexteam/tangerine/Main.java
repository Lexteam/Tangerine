/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine;

import xyz.lexteam.tangerine.data.ConfigModel;
import xyz.lexteam.tangerine.util.JsonUtils;

import java.io.File;
import java.util.Optional;

/**
 * The main class, where the entry point is.
 */
public final class Main {

    private ConfigModel config;

    private Main() {
        Optional<ConfigModel> configModel = JsonUtils.readModelFromFile(new File("config.json"), ConfigModel.class);
        if (configModel.isPresent()) {
            System.out.println("Successfully loaded config.");
            this.config = configModel.get();
        } else {
            System.out.println("Config doesn't exist! EXITING");
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
