/*
 * This file is part of Tangerine, licensed All Rights Reserved.
 *
 * Copyright (c) 2016, Lexteam <http://www.lexteam.xyz/>
 * All Rights Reserved.
 */
package xyz.lexteam.tangerine.event.state;

import xyz.lexteam.tangerine.Tangerine;
import xyz.lexteam.tangerine.event.Event;

/**
 * Represents a state event.
 */
public interface StateEvent extends Event {

    Tangerine getTangerine();
}
