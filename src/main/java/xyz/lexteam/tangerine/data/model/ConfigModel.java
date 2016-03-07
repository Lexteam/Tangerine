/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.data.model;

/**
 * Represents the config.
 */
public class ConfigModel {

    private Discord discord;

    public Discord getDiscord() {
        return this.discord;
    }

    public static class Discord {

        private String email;
        private String password;

        public String getEmail() {
            return this.email;
        }

        public String getPassword() {
            return this.password;
        }
    }
}
