/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;

import java.util.Arrays;
import java.util.List;

/**
 * The command line options.
 */
class MainOptions {

    @Parameter(names = { "-m", "--modules" })
    private String modules = "";

    @Parameter(names = { "-b", "--base" })
    private boolean baseModuleEnabled = true;

    protected MainOptions(String[] args) {
        new JCommander(this, args);
    }

    public List<String> getModules() {
        if (this.modules != "" && this.modules != null) {
            return Arrays.asList(this.modules.split(","));
        }
        return Lists.newArrayList();
    }

    public boolean isBaseModuleEnabled() {
        return this.baseModuleEnabled;
    }
}
