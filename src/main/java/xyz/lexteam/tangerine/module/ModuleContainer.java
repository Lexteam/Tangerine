/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.module;

/**
 * Represents a module.
 */
public interface ModuleContainer {

    String getId();

    String getName();

    String getVersion();

    Object getInstance();
}
