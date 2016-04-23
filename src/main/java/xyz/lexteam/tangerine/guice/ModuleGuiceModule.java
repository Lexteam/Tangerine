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
package xyz.lexteam.tangerine.guice;

import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.lexteam.spectre.Module;
import xyz.lexteam.tangerine.Tangerine;

import java.io.File;

/**
 * The module guice module :P
 */
public class ModuleGuiceModule extends AbstractModule {

    private final Logger logger;
    private final Tangerine tangerine;
    private final File configDir;

    public ModuleGuiceModule(Tangerine tangerine, Module module) {
        this.tangerine = tangerine;
        this.logger = LoggerFactory.getLogger(module.name());
        this.configDir = new File("config", module.id());
        if (!this.configDir.exists()) {
            this.configDir.mkdirs();
        }
    }

    @Override
    protected void configure() {
        this.bind(Tangerine.class).toInstance(this.tangerine);
        this.bind(Logger.class).toInstance(this.logger);
        this.bind(File.class).annotatedWith(ConfigDir.class).toInstance(this.configDir);
    }
}
