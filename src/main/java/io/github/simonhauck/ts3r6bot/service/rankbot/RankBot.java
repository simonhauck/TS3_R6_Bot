package io.github.simonhauck.ts3r6bot.service.rankbot;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import io.github.simonhauck.ts3r6bot.persistence.UserServicePersistence;
import io.github.simonhauck.ts3r6bot.r6.IR6Api;
import io.github.simonhauck.ts3r6bot.r6.R6TabApi;
import io.github.simonhauck.ts3r6bot.ts3.TS3FilteredEventListener;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class RankBot extends TS3FilteredEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(RankBot.class);

    @Autowired
    private TS3Service _ts3Service;

    @Autowired
    private UserServicePersistence _userServicePersistence;

    @Autowired
    @Qualifier("r6tab")
    private IR6Api _r6Api;

    @Autowired
    private Environment _env;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    public RankBot() {

    }

    //------------------------------------------------------------------------------------------------------------------
    // TS3FilteredEventListener methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        LOG.info("Client connected with UID: " + clientJoinEvent.getUniqueClientIdentifier());

        Thread thread = new Thread(
                new RankAssignmentRunnable(
                        _userServicePersistence,
                        _ts3Service,
                        _r6Api,
                        _env,
                        clientJoinEvent.getUniqueClientIdentifier(),
                        clientJoinEvent.getClientDatabaseId()
                )
        );

        thread.start();

    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * called after initializing the service. Register the required callbacks
     */
    @PostConstruct
    public void postConstruct() {
        LOG.info("RankBot is registered and subscribing to TS3 callbacks...");
        _ts3Service.getEventDispatcher().registerEventListener(this);
    }

    /**
     * check every 10 minutes if new noobs occurred or a noob rank expired
     */
    @Scheduled(fixedRate = 1000 * 60 * 30)
    public void triggerR6TabRanks() {
        if (_r6Api instanceof R6TabApi) {
            LOG.info("Triggering R6Tab Update for alle users...");

            //Get all players
            LOG.info("Requesting all players from the database...");
            List<R6Player> allPlayer = _userServicePersistence.getAllR6Player();
            LOG.info("All Users loaded. The List contains " + allPlayer.size() + " element(s).Start Update Process...");

            allPlayer.forEach(r6Player -> ((R6TabApi) _r6Api).triggerR6TabUpdate(r6Player));
            LOG.info("Finished. Update process is finished. " + allPlayer.size() + " updates were triggered.");
        } else {
            LOG.info("Trigger R6Tab Rank Update was started but will be aborted.");
        }

    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * trigger an update on R6Tab for the given player
     *
     * @param player can not be {@code null}
     */
    private void triggerUpdateOnR6Tab(R6Player player) {
        assert player != null;

        LOG.debug("Trigger update for player: " + player.getPlayerName() + " and id: " + player.getPlayerID());

    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
