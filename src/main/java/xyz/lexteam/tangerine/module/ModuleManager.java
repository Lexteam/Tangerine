/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.module;

import xyz.lexteam.tangerine.data.ModuleDescriptorModel;
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

    public ModuleManager(File modulesDir) {
        this.modulesDir = modulesDir;
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
                    Module module = (Module) moduleClass.getDeclaredAnnotation(Module.class);
                    Object instance = moduleClass.newInstance();
                    this.modules.add(ModuleUtils.getContainer(module, instance));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
