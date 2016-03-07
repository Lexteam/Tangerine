/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.base;

import com.google.inject.Inject;
import xyz.lexteam.eventbus.Listener;
import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.base.command.HelpCommand;
import xyz.lexteam.tangerine.event.state.ReadyStateEvent;
import xyz.lexteam.tangerine.module.Module;

/**
 * The base Tangerine module.
 * This module is enabled by default, however can be disabled by a command line option.
 */
@Module(id = "tangerine-base", name = "Base Module", version = "1.0.0-SNAPSHOT")
public class BaseModule {

    private final Tangerine tangerine;

    @Inject
    public BaseModule(Tangerine tangerine) {
        this.tangerine = tangerine;
    }

    @Listener
    public void onReadyState(ReadyStateEvent event) {
        event.getTangerine().getCommandDispatcher().registerCommand(new HelpCommand(this.tangerine), "help");
    }
}
