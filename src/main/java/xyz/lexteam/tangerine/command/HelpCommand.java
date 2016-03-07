/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.command;

import com.beust.jcommander.internal.Lists;
import com.sk89q.intake.CommandCallable;
import com.sk89q.intake.CommandException;
import com.sk89q.intake.CommandMapping;
import com.sk89q.intake.Description;
import com.sk89q.intake.Parameter;
import com.sk89q.intake.context.CommandLocals;
import com.sk89q.intake.util.auth.AuthorizationException;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import xyz.lexteam.tangerine.Tangerine;

import java.util.List;

/**
 * The 'help' command.
 */
public class HelpCommand implements CommandCallable {

    private final Tangerine tangerine;

    public HelpCommand(Tangerine tangerine) {
        this.tangerine = tangerine;
    }

    @Override
    public boolean call(String arguments, CommandLocals locals, String[] parentCommands)
            throws CommandException, AuthorizationException {
        try {
            IPrivateChannel channel = this.tangerine.getDiscordClient().getOrCreatePMChannel(locals.get(IUser.class));
            channel.sendMessage("== Tangerine Help ==");

            for (CommandMapping command : this.tangerine.getCommandDispatcher().getCommands()) {
                channel.sendMessage(String.format("%s  -  %s",
                        command.getDescription().getUsage(), command.getDescription().getHelp()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Description getDescription() {
        return new Description() {
            @Override
            public List<Parameter> getParameters() {
                return Lists.newArrayList();
            }

            @Override
            public String getShortDescription() {
                return "Displays the help text for all commands.";
            }

            @Override
            public String getHelp() {
                return this.getShortDescription();
            }

            @Override
            public String getUsage() {
                return "!help";
            }

            @Override
            public List<String> getPermissions() {
                return Lists.newArrayList();
            }
        };
    }

    @Override
    public boolean testPermission(CommandLocals locals) {
        return true;
    }

    @Override
    public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
        return Lists.newArrayList();
    }
}
