/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.LoggerFactory;
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

            Injector injector = Guice.createInjector(
                    new ModuleGuiceModule(this.tangerine, LoggerFactory.getLogger(module.name())));
            Object instance = injector.getInstance(moduleClass);

            this.modules.add(ModuleUtils.getContainer(module, instance));

            // register module to both event buses
            this.tangerine.getEventBus().registerListener(instance);
            this.tangerine.getDiscordClient().getDispatcher().registerListener(instance);
        } else {
            Main.LOGGER.warn("Module didn't have @Module annotation, cannot load!");
        }
    }
}
