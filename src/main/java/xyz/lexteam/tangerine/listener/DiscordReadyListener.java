/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.listener;

import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.module.ModuleContainer;

/**
 * The ready listener
 */
public class DiscordReadyListener implements IListener<ReadyEvent> {

    private final Tangerine tangerine;

    public DiscordReadyListener(Tangerine tangerine) {
        this.tangerine = tangerine;
    }

    @Override
    public void handle(ReadyEvent event) {
        for (ModuleContainer moduleContainer : this.tangerine.getModuleManager().getModules()) {
            event.getClient().getDispatcher().registerListener(moduleContainer.getInstance());
        }
    }
}
