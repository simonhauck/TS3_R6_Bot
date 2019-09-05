package io.github.simonhauck.ts3r6bot.service.rankbot;

import io.github.simonhauck.ts3r6bot.model.r6.R6Platform;
import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import io.github.simonhauck.ts3r6bot.model.r6.R6Region;
import io.github.simonhauck.ts3r6bot.model.ts3.TS3User;
import io.github.simonhauck.ts3r6bot.persistence.UserServicePersistence;
import io.github.simonhauck.ts3r6bot.r6.IR6Api;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;

class RankAssignmentRunnable implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(RankAssignmentRunnable.class);

    private final Environment _env;
    private final UserServicePersistence _userServicePersistence;
    private final TS3Service _ts3Service;
    private final IR6Api _r6Api;

    private final String _userUID;
    private final int _clientDatabaseID;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * create a new rank assignment task with the given fields
     *
     * @param userServicePersistence can not be {@code null}
     * @param ts3Service             can not be {@code null}
     * @param userUID                can not be {@code null}
     * @param clientDatabaseID       id of the client in the database
     */
    public RankAssignmentRunnable(
            UserServicePersistence userServicePersistence,
            TS3Service ts3Service,
            IR6Api r6Api,
            Environment environment,
            String userUID, int clientDatabaseID) {
        assert userServicePersistence != null;
        assert ts3Service != null;
        assert r6Api != null;
        assert userUID != null;
        assert environment != null;

        _userServicePersistence = userServicePersistence;
        _ts3Service = ts3Service;
        _r6Api = r6Api;
        _env = environment;
        _userUID = userUID;
        _clientDatabaseID = clientDatabaseID;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Runnable methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void run() {
        LOG.debug("Started Thread to assign rank to user: " + _userUID);
        assignRainbow6Rank();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * assign the rainbow 6 rank to the given ts3 user.
     * If the user is not existing, return without assigning a rank
     */
    private void assignRainbow6Rank() {
        TS3User user = _userServicePersistence.getTS3UserByUID(_userUID);

        //If the user is null, don't continue
        if (user == null) {
            LOG.info("User not found. Cancel rainbow6 rank assignment.");
            return;
        }

        R6Platform platform = R6Platform.getR6PlatformByName(_env.getProperty("r6.platform"));
        R6Region region = R6Region.getR6RegionByName(_env.getProperty("r6.region"));

        //TODO change
        R6Player player = _r6Api.getPlayerByName(user.getUplayName(), platform, region);

        if (player == null) {
            LOG.info("Player not found for name: " + user.getUplayName() + ". Aborting rank assignment");
            return;
        }

        //Set the current player and save the data in the database
        LOG.info("Updating user with playerStats Data");
        user.updateR6PlayerStat(player);
        _userServicePersistence.saveTS3User(user);

        int rankID = player.getPlayerRank().getChannelID(_env);

        _ts3Service.getTs3AsyncApi().addClientToServerGroup(rankID, _clientDatabaseID);
        LOG.info("Assigned user: " + _userUID + " rainbow6 rank with ID: " + rankID);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
