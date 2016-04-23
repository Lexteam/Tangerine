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
package xyz.lexteam.tangerine.module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import xyz.lexteam.spectre.Module;
import xyz.lexteam.spectre.ModuleContainer;
import xyz.lexteam.spectre.loader.ModuleLoader;
import xyz.lexteam.spectre.loader.hook.Hook;
import xyz.lexteam.spectre.loader.hook.HookInfo;
import xyz.lexteam.spectre.loader.hook.Hooks;
import xyz.lexteam.tangerine.Main;
import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.data.model.ModuleDescriptorModel;
import xyz.lexteam.tangerine.guice.ModuleGuiceModule;
import xyz.lexteam.tangerine.util.JsonUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The module manager.
 */
public class ModuleManager {

    private final List<ModuleContainer> modules = new ArrayList<>();
    private final Tangerine tangerine;
    private final ModuleLoader moduleLoader;

    public ModuleManager(Tangerine tangerine, File modulesDir) {
        this.tangerine = tangerine;
        this.moduleLoader = new ModuleLoader(modulesDir);

        this.moduleLoader.registerHook(Hooks.READ_DESCRIPTOR, new Hook() {
            @Override
            public void execute(HookInfo info) {
                try {
                    URL url = new URL("jar:file:" + info.get(File.class).getAbsolutePath() + "!/module.json");
                    Optional<ModuleDescriptorModel> descriptorModel =
                            JsonUtils.readModelFromUrl(url, ModuleDescriptorModel.class);
                    if (descriptorModel.isPresent()) {
                        info.put(xyz.lexteam.spectre.ModuleDescriptorModel.class, descriptorModel.get());
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        this.moduleLoader.registerHook(Hooks.CONSTRUCT_INSTANCE, new Hook() {
            @Override
            public void execute(HookInfo info) {
                Class<?> moduleClass = info.get(Class.class);
                info.put("instance", ModuleManager.this.loadModule(moduleClass, false));
            }
        });
    }

    public List<ModuleContainer> getModules() {
        return this.modules;
    }

    public void loadAllModules() {
        this.modules.addAll(this.moduleLoader.loadAllModules());
    }

    public Object loadModule(Class<?> moduleClass, boolean createContainer) {
        if (moduleClass.isAnnotationPresent(Module.class)) {
            Module module = moduleClass.getDeclaredAnnotation(Module.class);
            Main.LOGGER.info("Loading " + module.name() + " v" + module.version());

            Injector injector = Guice.createInjector(new ModuleGuiceModule(this.tangerine, module));
            Object instance = injector.getInstance(moduleClass);

            if (createContainer) {
                this.modules.add(new ModuleContainer() {
                    @Override
                    public String getId() {
                        return module.id();
                    }

                    @Override
                    public String getName() {
                        return module.name();
                    }

                    @Override
                    public String getVersion() {
                        return module.version();
                    }

                    @Override
                    public Object getInstance() {
                        return instance;
                    }

                    @Override
                    public xyz.lexteam.spectre.ModuleDescriptorModel getDescriptor() {
                        return () -> moduleClass.getName();
                    }
                });
            }

            this.tangerine.getEventBus().registerListener(instance);
            Main.LOGGER.info("Finished Loading " + module.name() + " v" + module.version());

            return instance;
        } else {
            Main.LOGGER.warn("Module didn't have @Module annotation, cannot load!");
        }

        return null;
    }
}
