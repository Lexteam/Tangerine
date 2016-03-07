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
import xyz.lexteam.tangerine.Main;
import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.data.model.ModuleDescriptorModel;
import xyz.lexteam.tangerine.guice.ModuleGuiceModule;
import xyz.lexteam.tangerine.util.JsonUtils;
import xyz.lexteam.tangerine.util.ModuleUtils;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The module manager.
 */
public class ModuleManager {

    private final File modulesDir;
    private final List<ModuleContainer> modules = new ArrayList<>();
    private final Tangerine tangerine;

    public ModuleManager(Tangerine tangerine, File modulesDir) {
        this.tangerine = tangerine;
        this.modulesDir = modulesDir;

        if (!modulesDir.exists()) {
            modulesDir.mkdir();
        }
    }

    public List<ModuleContainer> getModules() {
        return this.modules;
    }

    public void loadAllModules() {
        File[] jarFiles = this.modulesDir.listFiles(file -> {
            return file.getName().endsWith(".jar");
        });
        for (File jarFile : jarFiles) {
            Optional<ModuleDescriptorModel> descriptorModel = JsonUtils.readModelFromFile(
                    new File(jarFile.getAbsolutePath(), "module.json"), ModuleDescriptorModel.class);
            if (descriptorModel.isPresent()) {
                try {
                    ModuleClassLoader moduleClassLoader =
                            new ModuleClassLoader(jarFile.toURI().toURL(), ModuleManager.class.getClassLoader());
                    Class moduleClass = moduleClassLoader.loadClass(descriptorModel.get().getMainClass());
                    this.loadModule(moduleClass);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Main.LOGGER.warn("Module didn't have config.json, cannot load!");
            }
        }
    }

    public void loadModule(Class<?> moduleClass) {
        if (moduleClass.isAnnotationPresent(Module.class)) {
            Module module = moduleClass.getDeclaredAnnotation(Module.class);
            Main.LOGGER.info("Loading " + module.name() + " v" + module.version());

            Injector injector = Guice.createInjector(new ModuleGuiceModule(this.tangerine, module));
            Object instance = injector.getInstance(moduleClass);

            this.modules.add(ModuleUtils.getContainer(module, instance));
            this.tangerine.getEventBus().registerListener(instance);
            Main.LOGGER.info("Finished Loading " + module.name() + " v" + module.version());
        } else {
            Main.LOGGER.warn("Module didn't have @Module annotation, cannot load!");
        }
    }
}
