/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.event.state;

import xyz.lexteam.tangerine.Tangerine;

/**
 * The ready state event.
 */
public class ReadyStateEvent implements StateEvent {

    private final Tangerine tangerine;

    public ReadyStateEvent(Tangerine tangerine) {
        this.tangerine = tangerine;
    }

    @Override
    public Tangerine getTangerine() {
        return this.tangerine;
    }
}
