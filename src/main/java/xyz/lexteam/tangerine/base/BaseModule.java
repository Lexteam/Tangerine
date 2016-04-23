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
package xyz.lexteam.tangerine.base;

import com.google.inject.Inject;
import xyz.lexteam.eventbus.Listener;
import xyz.lexteam.spectre.Module;
import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.base.command.HelpCommand;
import xyz.lexteam.tangerine.event.state.ReadyStateEvent;

/**
 * The base Tangerine module.
 * This module is enabled by default, however can be disabled by a command line option.
 */
@Module(id = "tangerine-base", name = "Base Module", version = "1.0.0-SNAPSHOT")
public class BaseModule {

    private final Tangerine tangerine;

    @Inject
    public BaseModule(Tangerine tangerine) {
        this.tangerine = tangerine;
    }

    @Listener
    public void onReadyState(ReadyStateEvent event) {
        event.getTangerine().getCommandDispatcher().registerCommand(new HelpCommand(this.tangerine), "help");
    }
}
