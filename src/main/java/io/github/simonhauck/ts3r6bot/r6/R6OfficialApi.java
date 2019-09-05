package io.github.simonhauck.ts3r6bot.r6;

import io.github.simonhauck.ts3r6bot.model.r6.R6Platform;
import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import io.github.simonhauck.ts3r6bot.model.r6.R6Rank;
import io.github.simonhauck.ts3r6bot.model.r6.R6Region;
import main.com.github.courtneyjoew.Auth;
import main.com.github.courtneyjoew.R6J;
import main.com.github.courtneyjoew.declarations.Platform;
import main.com.github.courtneyjoew.declarations.Rank;
import main.com.github.courtneyjoew.declarations.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component(value = "r6OfficialApi")
public class R6OfficialApi implements IR6Api {

    private static final Logger LOG = LoggerFactory.getLogger(R6OfficialApi.class);

    private Environment _env;

    private R6J _r6J;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    public R6OfficialApi(Environment environment) {
        LOG.info("Initializing R6Api with credentials...");

        _env = environment;
        reconnect();
    }

    //------------------------------------------------------------------------------------------------------------------
    // IR6Api methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * get a {@link R6Player} by the given name, platform and region.
     *
     * @param name     of the player. Can not be {@code null} or empty
     * @param platform where the player is playing. Can not be {@code null}
     * @param r6Region of the player. Can not be {@code null}
     * @return
     */
    @Override
    public R6Player getPlayerByName(String name, R6Platform platform, R6Region r6Region) {
        assert name != null;
        assert name.length() != 0;
        assert platform != null;
        assert r6Region != null;

        try {
            Platform targetPlatform = getPlatform(platform);
            Region targetRegion = getRegion(r6Region);

            if (_r6J == null) {
                LOG.warn("R6J is currently null. Can not process data.");
                return null;
            }

            LOG.info("Requesting player data for name: " + name + " and platform: " + targetPlatform);
            if (_r6J.playerExists(name, targetPlatform)) {
                LOG.info("Player " + name + " exists. Requesting data for platform: " + targetPlatform + " and region: " + targetRegion);
                main.com.github.courtneyjoew.R6Player player = _r6J.getPlayerByName(name, targetPlatform, targetRegion);
                LOG.info("Player data for name: " + name + " successfully loaded");
                return parsePlayer(player);
            } else {
                LOG.info("No player found for name: " + name + " and platform: " + targetPlatform);
                return null;
            }
        } catch (Exception e) {
            LOG.error("Exception occurred in R6OfficialApi", e);
            return null;
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * connect to the service
     */
    public void reconnect() {
        String username = _env.getProperty("uplay.username");
        String password = _env.getProperty("uplay.password");
        _r6J = new R6J(new Auth(username, password));
        LOG.info("Initialized R6Api.");
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * get the corresponding library platform
     *
     * @param platform can not be {@code null}
     * @return the corresponding platform
     */
    private Platform getPlatform(R6Platform platform) {
        assert platform != null;

        switch (platform) {
            case UPLAY:
                return Platform.UPLAY;
            case PLAYSTATION:
                return Platform.XBOX;
            case XBOX:
                return Platform.PS4;
            default:
                throw new AssertionError("No matching platform found!");
        }
    }

    /**
     * get the corresponding library region
     *
     * @param r6Region can not be {@code null}
     * @return the corresponding region
     */
    private Region getRegion(R6Region r6Region) {
        assert r6Region != null;

        switch (r6Region) {
            case EUROPE:
                return Region.EU;
            case ASIA:
                return Region.ASIA;
            case AMERICA:
                return Region.NA;
            default:
                throw new AssertionError("No matching region found!");
        }
    }

    /**
     * parse the library player in a {@link R6Player} object
     *
     * @param player can not be {@code null}
     * @return the initialized player
     */
    private R6Player parsePlayer(main.com.github.courtneyjoew.R6Player player) {
        String playerName = player.getName();
        R6Rank rank = parseRank(player.getRank());
        String playerID = player.getProfileId();

        R6Player newPlayer = new R6Player(playerName, rank, playerID);
        return newPlayer;
    }

    private R6Rank parseRank(Rank rank) {
        switch (rank) {
            case UNRANKED:
                return R6Rank.NO_RANK;
            case COPPER_4:
                return R6Rank.COPPER_4;
            case COPPER_3:
                return R6Rank.COPPER_3;
            case COPPER_2:
                return R6Rank.COPPER_2;
            case COPPER_1:
                return R6Rank.COPPER_1;
            case BRONZE_4:
                return R6Rank.BRONZE_4;
            case BRONZE_3:
                return R6Rank.BRONZE_3;
            case BRONZE_2:
                return R6Rank.BRONZE_2;
            case BRONZE_1:
                return R6Rank.BRONZE_1;
            case SILVER_4:
                return R6Rank.SILVER_4;
            case SILVER_3:
                return R6Rank.SILVER_3;
            case SILVER_2:
                return R6Rank.SILVER_2;
            case SILVER_1:
                return R6Rank.SILVER_1;
            case GOLD_4:
                return R6Rank.GOLD_4;
            case GOLD_3:
                return R6Rank.GOLD_3;
            case GOLD_2:
                return R6Rank.GOLD_2;
            case GOLD_1:
                return R6Rank.GOLD_1;
            case PLATINUM_3:
                return R6Rank.PLATINUM_3;
            case PLATINUM_2:
                return R6Rank.PLATINUM_2;
            case PLATINUM_1:
                return R6Rank.PLATINUM_1;
            case DIAMOND:
                return R6Rank.DIAMOND;
            default:
                throw new AssertionError("Unexpected rank occurred");

        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
