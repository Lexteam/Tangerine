/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.util;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Optional;

/**
 * Utilities for json.
 */
public final class JsonUtils {

    public static final Gson GSON = new Gson();

    public static <T> Optional<T> readModelFromFile(File file, Class<T> clazz) {
        try {
            return Optional.of(GSON.fromJson(new FileReader(file), clazz));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
