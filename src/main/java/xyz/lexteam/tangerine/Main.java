/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine;

import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.dispatcher.SimpleDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.DiscordException;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import xyz.lexteam.eventbus.IEventBus;
import xyz.lexteam.eventbus.SimpleEventBus;
import xyz.lexteam.tangerine.data.ConfigModel;
import xyz.lexteam.tangerine.event.discord.DiscordReadyEvent;
import xyz.lexteam.tangerine.listener.MessageListener;
import xyz.lexteam.tangerine.util.JsonUtils;

import java.io.File;
import java.util.Optional;

/**
 * The main class, where the entry point is.
 */
public final class Main implements Tangerine {

    public static final Logger LOGGER = LoggerFactory.getLogger("Tangerine");

    private ConfigModel config;
    private IEventBus eventBus = new SimpleEventBus();
    private IDiscordClient discordClient;
    private Dispatcher dispatcher = new SimpleDispatcher();

    public static void main(String[] args) throws DiscordException {
        new Main();
    }

    private Main() throws DiscordException {
        Optional<ConfigModel> configModel = JsonUtils.readModelFromFile(new File("config.json"), ConfigModel.class);
        if (configModel.isPresent()) {
            LOGGER.debug("Successfully loaded config.");
            this.config = configModel.get();
        } else {
            LOGGER.warn("Config doesn't exist! EXITING");
            System.exit(0);
        }

        this.discordClient = new ClientBuilder()
                .withLogin(this.config.getDiscord().getEmail(), this.config.getDiscord().getPassword())
                .login();
        this.discordClient.getDispatcher().registerListener(this);
        this.discordClient.getDispatcher().registerListener(new MessageListener(this.dispatcher));
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        this.eventBus.post(new DiscordReadyEvent(event.getClient()));
    }

    @Override
    public IEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public IDiscordClient getDiscordClient() {
        return this.discordClient;
    }

    @Override
    public Dispatcher getCommandDispatcher() {
        return this.dispatcher;
    }
}
