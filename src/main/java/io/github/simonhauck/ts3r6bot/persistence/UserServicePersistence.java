package io.github.simonhauck.ts3r6bot.persistence;

import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import io.github.simonhauck.ts3r6bot.model.ts3.TS3NoobUser;
import io.github.simonhauck.ts3r6bot.model.ts3.TS3User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServicePersistence {

    private static final Logger LOG = LoggerFactory.getLogger(UserServicePersistence.class);

    @Autowired
    private ITS3UserPersistence _ts3UserPersistence;

    @Autowired
    private ITS3NoobUserPersistence _noobUserPersistence;

    @Autowired
    private IRainbowUserPersistence _rainbowUserPersistence;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * get a list with all active {@link TS3NoobUser} users
     *
     * @return the initialized list
     */
    public List<TS3NoobUser> getAllActiveTS3NoobUser() {
        return _noobUserPersistence.getAllActiveTS3NoobUsers();
    }

    /**
     * request a {@link TS3User} by the unique ts3 identifier
     *
     * @param uid can not be {@code null}
     * @return the ts3 user or null
     */
    public TS3User getTS3UserByUID(String uid) {
        return _ts3UserPersistence.getTS3UserBy_ts3UniqueIdentifier(uid);
    }

    /**
     * request a {@link TS3NoobUser} by the unique ts3 identifier
     *
     * @param uid can not be {@code null}
     * @return the {@link TS3NoobUser} or null
     */
    public TS3NoobUser getTS3NoobUserByUID(String uid) {
        return _noobUserPersistence.getTS3NoobUserBy_ts3UniqueIdentifier(uid);
    }

    /**
     * save the given instance in the database
     *
     * @param user can not be {@code null}
     * @return the saved instance
     */
    public TS3User insertUpdateUserWithTeamspeakUID(TS3User user) {
        //If the user has an id save the entity with the id
        if (user.getId() != 0) return _ts3UserPersistence.save(user);

        TS3User loadedUser = _ts3UserPersistence.getTS3UserBy_ts3UniqueIdentifier(user.getTs3UniqueIdentifier());

        if (loadedUser == null) {
            //Insert new user
            return _ts3UserPersistence.save(user);
        } else {
            //Update loaded user
            loadedUser.updateValues(user);
            return _ts3UserPersistence.save(loadedUser);
        }
    }

    /**
     * save the given instance in the database
     *
     * @param user can not be {@code null}
     * @return the saved instance
     */
    public TS3NoobUser insertUpdateUserWithTeamspeakUID(TS3NoobUser user) {
        if (user.getId() != 0) return _noobUserPersistence.save(user);

        TS3NoobUser loadedUser = _noobUserPersistence.getTS3NoobUserBy_ts3UniqueIdentifier(user.getTs3UniqueIdentifier());

        if (loadedUser == null) {
            return _noobUserPersistence.save(user);
        } else {
            loadedUser.updateValues(user);
            return _noobUserPersistence.save(user);
        }
    }

    /**
     * save the {@link TS3NoobUser} in the database
     *
     * @param user can not be {@code null}
     * @return the saved user
     */
    public TS3NoobUser saveTS3NoobUser(TS3NoobUser user) {
        return _noobUserPersistence.save(user);
    }

    /**
     * save the current ts3User in the database with the id
     *
     * @param user can not be {@code null}.
     * @return the saved user
     */
    public TS3User saveTS3User(TS3User user) {
        return _ts3UserPersistence.save(user);
    }

    /**
     * @return a list with all players. The list can not be {@code null}, but emty
     */
    public List<R6Player> getAllR6Player() {
        List<R6Player> result = new ArrayList<>();

        Iterable<R6Player> playersIterator = _rainbowUserPersistence.findAll();
        playersIterator.forEach(result::add);
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
