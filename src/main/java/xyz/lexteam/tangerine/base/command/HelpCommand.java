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
package xyz.lexteam.tangerine.base.command;

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
            channel.sendMessage("Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>");

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
