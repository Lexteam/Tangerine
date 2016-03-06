/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.module;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * The module class loader.
 */
public class ModuleClassLoader extends URLClassLoader {

    public ModuleClassLoader(URL moduleUrl, ClassLoader parent) {
        super(new URL[] { moduleUrl }, parent);
    }
}
