/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.module;

/**
 * The module annotation.
 */
public @interface Module {

    String id();

    String name();

    String version();
}
