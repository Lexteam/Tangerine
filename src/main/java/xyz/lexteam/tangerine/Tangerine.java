/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine;

import com.sk89q.intake.dispatcher.Dispatcher;
import sx.blah.discord.api.IDiscordClient;
import xyz.lexteam.eventbus.IEventBus;
import xyz.lexteam.tangerine.module.ModuleManager;

/**
 * The core interface.
 */
public interface Tangerine {

    IEventBus getEventBus();

    ModuleManager getModuleManager();

    IDiscordClient getDiscordClient();

    Dispatcher getCommandDispatcher();
}
