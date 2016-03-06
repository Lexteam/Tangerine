/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine;

import sx.blah.discord.api.IDiscordClient;
import xyz.lexteam.eventbus.IEventBus;

/**
 * The core interface.
 */
public interface Tangerine {

    IEventBus getEventBus();

    IDiscordClient getDiscordClient();
}
