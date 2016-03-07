/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.listener;

import com.sk89q.intake.CommandException;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.dispatcher.Dispatcher;
import com.sk89q.intake.util.auth.AuthorizationException;
import sx.blah.discord.handle.IListener;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import xyz.lexteam.tangerine.data.Constants;

/**
 * The message listener.
 */
public class DiscordMessageListener implements IListener<MessageReceivedEvent> {

    private final Dispatcher dispatcher;

    public DiscordMessageListener(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void handle(MessageReceivedEvent event) {
        String content = event.getMessage().getContent();
        if (content.startsWith(Constants.COMMAND_PREFIX)) {
            content = content.substring(1);

            CommandLocals commandLocals = new CommandLocals();
            commandLocals.put(IMessage.class, event.getMessage());
            commandLocals.put(IChannel.class, event.getMessage().getChannel());
            commandLocals.put(IUser.class, event.getMessage().getAuthor());

            try {
                this.dispatcher.call(content, commandLocals, new String[0]);
            } catch (CommandException e) {
                e.printStackTrace();
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }
    }
}