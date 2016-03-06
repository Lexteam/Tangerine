/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.event.discord;

import sx.blah.discord.api.IDiscordClient;

/**
 * Fired when Discord is ready.
 */
public class DiscordReadyEvent implements DiscordEvent {

    private final IDiscordClient discordClient;

    public DiscordReadyEvent(IDiscordClient discordClient) {
        this.discordClient = discordClient;
    }

    @Override
    public IDiscordClient getDiscordClient() {
        return this.discordClient;
    }
}
