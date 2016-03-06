/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.event.discord;

import sx.blah.discord.api.IDiscordClient;
import xyz.lexteam.tangerine.event.Event;

/**
 * Any Discord related event.
 */
public interface DiscordEvent extends Event {

    IDiscordClient getDiscordClient();
}
