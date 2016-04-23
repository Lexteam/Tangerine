/*
 * This file is part of Tangerine, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package xyz.lexteam.tangerine;

import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.dispatcher.SimpleDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.EventSubscriber;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.modules.Configuration;
import sx.blah.discord.util.DiscordException;
import xyz.lexteam.eventbus.IEventBus;
import xyz.lexteam.eventbus.SimpleEventBus;
import xyz.lexteam.tangerine.base.BaseModule;
import xyz.lexteam.tangerine.data.model.ConfigModel;
import xyz.lexteam.tangerine.event.state.ReadyStateEvent;
import xyz.lexteam.tangerine.listener.DiscordMessageListener;
import xyz.lexteam.tangerine.listener.DiscordReadyListener;
import xyz.lexteam.tangerine.module.ModuleManager;
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
    private ModuleManager moduleManager;
    private IDiscordClient discordClient;
    private Dispatcher dispatcher = new SimpleDispatcher();

    public static void main(String[] args) throws DiscordException {
        new Main(new MainOptions(args));
    }

    private Main(MainOptions options) throws DiscordException {
        Configuration.AUTOMATICALLY_ENABLE_MODULES = false;
        Configuration.LOAD_EXTERNAL_MODULES = false;

        Optional<ConfigModel> configModel = JsonUtils.readModelFromFile(new File("config.json"), ConfigModel.class);
        if (configModel.isPresent()) {
            LOGGER.debug("Successfully loaded config.");
            this.config = configModel.get();
        } else {
            LOGGER.warn("Config doesn't exist! EXITING");
            System.exit(0);
        }

        this.moduleManager = new ModuleManager(this, new File("modules/"));
        this.moduleManager.loadAllModules();

        for (String module : options.getModules()) {
            try {
                Class clazz = Class.forName(module);
                this.moduleManager.loadModule(clazz, true);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (options.isBaseModuleEnabled()) {
            this.moduleManager.loadModule(BaseModule.class, true);
        }

        LOGGER.debug("Loading Discord client");
        this.discordClient = new ClientBuilder()
                .withLogin(this.config.getDiscord().getEmail(), this.config.getDiscord().getPassword())
                .login();
        this.discordClient.getDispatcher().registerListener(this);
        this.discordClient.getDispatcher().registerListener(new DiscordMessageListener(this.dispatcher));
        this.discordClient.getDispatcher().registerListener(new DiscordReadyListener(this));
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        this.eventBus.post(new ReadyStateEvent(this));
    }

    @Override
    public IEventBus getEventBus() {
        return this.eventBus;
    }

    @Override
    public ModuleManager getModuleManager() {
        return this.moduleManager;
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
