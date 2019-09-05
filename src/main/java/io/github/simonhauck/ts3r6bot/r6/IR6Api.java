package io.github.simonhauck.ts3r6bot.r6;

import io.github.simonhauck.ts3r6bot.model.r6.R6Platform;
import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import io.github.simonhauck.ts3r6bot.model.r6.R6Region;

public interface IR6Api {

    /**
     * request a {@link R6Player} by the exact name
     *
     * @param name     of the player. Can not be {@code null} or empty
     * @param platform where the player is playing. Can not be {@code null}
     * @return the player or null, if not found
     */
    R6Player getPlayerByName(String name, R6Platform platform, R6Region r6Region);
}
