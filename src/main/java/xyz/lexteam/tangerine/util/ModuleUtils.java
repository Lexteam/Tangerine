/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.util;

import xyz.lexteam.tangerine.module.Module;
import xyz.lexteam.tangerine.module.ModuleContainer;

/**
 * Utilities for modules.
 */
public final class ModuleUtils {

    public static ModuleContainer getContainer(Module module, Object instance) {
        return new ModuleContainer() {
            @Override
            public String getId() {
                return module.id();
            }

            @Override
            public String getName() {
                return module.name();
            }

            @Override
            public String getVersion() {
                return module.version();
            }

            @Override
            public Object getInstance() {
                return instance;
            }
        };
    }
}
