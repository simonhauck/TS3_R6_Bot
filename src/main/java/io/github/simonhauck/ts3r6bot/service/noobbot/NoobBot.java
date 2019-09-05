package io.github.simonhauck.ts3r6bot.service.noobbot;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroupClient;
import io.github.simonhauck.ts3r6bot.model.ts3.TS3NoobUser;
import io.github.simonhauck.ts3r6bot.persistence.UserServicePersistence;
import io.github.simonhauck.ts3r6bot.ts3.TS3FilteredEventListener;
import io.github.simonhauck.ts3r6bot.ts3.TS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class NoobBot extends TS3FilteredEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(NoobBot.class);

    @Autowired
    private TS3Service _ts3Service;

    @Autowired
    private UserServicePersistence _servicePersistence;

    private Environment _env;
    private int _noobRankID;
    private long _noobTimeInMS;
    private Locale _locale;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    @Autowired
    public NoobBot(Environment environment) {
        _env = environment;
        _noobRankID = Integer.valueOf(Objects.requireNonNull(_env.getProperty("noob.rank")));
        _noobTimeInMS = Long.valueOf(Objects.requireNonNull(_env.getProperty("noob.timeInMS")));
        _locale = Locale.forLanguageTag(Objects.requireNonNull(_env.getProperty("noob.locale")));
    }

    //------------------------------------------------------------------------------------------------------------------
    // TS3EventListener methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * called when a client joined.
     * Check if the user is a registered noob and if the rank is still active.
     * If the rank is active, assign the noob rank.
     * If the rank is expired, save the new value in the database.
     *
     * @param clientJoinEvent can not be {@code null}
     */
    @Override
    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        LOG.debug("Start to assign noob rank...");
        TS3NoobUser noobClient = _servicePersistence.getTS3NoobUserByUID(clientJoinEvent.getUniqueClientIdentifier());

        //Check if client exists
        if (noobClient == null) {
            LOG.debug("User not found. Rank assignment aborted");
            return;
        }

        //Check if noob is not active
        if (!noobClient.isNoobActive()) {
            LOG.debug("User found. But noob rank is not active! Rank assignment aborted");
            return;
        }

        //Check if noob should be active
        long currentTime = System.currentTimeMillis();
        if (currentTime < noobClient.getNoobUntilTimeStamp()) {
            //Noob should be active
            _ts3Service.getTs3AsyncApi().addClientToServerGroup(_noobRankID, clientJoinEvent.getClientDatabaseId());
            LOG.info("User found. Noob rank is active and will be assigned. User: " + noobClient);
        } else {
            noobClient.setNoobActive(false);
            noobClient.setTs3Name(clientJoinEvent.getClientNickname());
            _servicePersistence.saveTS3NoobUser(noobClient);
            LOG.info("User found. Noob rank expired. Saved new state to the database. User: " + noobClient);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * called after initializing the service. Register the required callbacks
     */
    @PostConstruct
    public void postConstruct() {
        LOG.info("NoobBot is registered and subscribing to TS3 callbacks...");
        _ts3Service.getEventDispatcher().registerEventListener(this);
    }

    /**
     * check every x ms if new noobs occurred or a noob rank expired
     */
    @Scheduled(fixedRate = 5000)
    public void checkForNoob() {
        LOG.debug("Checking for noobs...");
        getAllNoobUsers();
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * start a request to get all noob data
     */
    private void getAllNoobUsers() {
        LOG.debug("Requesting all users with the noob rank");
        _ts3Service.getTs3AsyncApi().getServerGroupClients(_noobRankID)
                .onSuccess(serverGroupClients -> processClients(serverGroupClients))
                .onFailure(e -> LOG.error("Error requesting noobs!", e));
    }

    /**
     * process all noob clients and save the corresponding values in the database
     *
     * @param clientList can not be {@code null}
     */
    private void processClients(List<ServerGroupClient> clientList) {
        LOG.debug("Processing noob clients. Amount clients: " + clientList.size());
        for (ServerGroupClient client : clientList) {
            LOG.debug("Processing client with UID: " + client.getUniqueIdentifier());

            //Load user
            TS3NoobUser loadedUser = _servicePersistence.getTS3NoobUserByUID(client.getUniqueIdentifier());

            long currentTimeStamp = System.currentTimeMillis();
            long calculatedNooBTimeStamp = currentTimeStamp + _noobTimeInMS;

            if (loadedUser == null) {
                //New user with noob rank
                TS3NoobUser newUser = new TS3NoobUser(client.getNickname(), client.getUniqueIdentifier(), calculatedNooBTimeStamp);
                newUser = _servicePersistence.insertUpdateUserWithTeamspeakUID(newUser);
                LOG.info("New noob user detected. User saved in database. User: " + newUser.toString());
                sendServerInfo(client.getNickname(), newUser.getNoobUntilTimeStamp());
                return;
            }

            if (loadedUser.isNoobActive() && loadedUser.getNoobUntilTimeStamp() > currentTimeStamp) {
                //Check if user rank is still active
                LOG.debug("User is still noob. No action required. User: " + loadedUser.toString());

            } else if (!loadedUser.isNoobActive()) {
                //Noob not active. Add noob rank
                loadedUser.setNoobActive(true);
                loadedUser.setNoobUntilTimeStamp(calculatedNooBTimeStamp);
                loadedUser.setTs3Name(client.getNickname());

                _servicePersistence.saveTS3NoobUser(loadedUser);
                LOG.info("User is noob again. Saved new values in the database. User: " + loadedUser.toString());
                sendServerInfo(client.getNickname(), loadedUser.getNoobUntilTimeStamp());

            } else if (loadedUser.getNoobUntilTimeStamp() < currentTimeStamp) {
                //Noob expired
                _ts3Service.getTs3AsyncApi().removeClientFromServerGroup(_noobRankID, client.getClientDatabaseId());

                loadedUser.setNoobActive(false);
                loadedUser.setTs3Name(client.getNickname());

                _servicePersistence.saveTS3NoobUser(loadedUser);
                LOG.info("User noob expired. Noob was set to inactive and removed. User: " + loadedUser);

            } else {
                LOG.warn("Unexpected state! User: " + loadedUser);
            }
        }
    }

    /**
     * send a message to the server chat that a user is registered
     *
     * @param username of the new noob user. Can not be {@code null}
     * @param noobTime unix timestamp in ms until the user is a noob
     */
    private void sendServerInfo(String username, long noobTime) {
        Date date = new Date(noobTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", _locale);
        String dateTime = dateFormat.format(date);
        String message = "Registered " + username + " as noob. You are a noob until " + dateTime;
        _ts3Service.getTs3AsyncApi().sendServerMessage(message);
        LOG.debug("Send server message: " + message);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
