package io.github.simonhauck.ts3r6bot.model.r6;

import io.github.simonhauck.ts3r6bot.model.ts3.TS3User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "rainbowUser")
public class R6Player {

    private static final Logger LOG = LoggerFactory.getLogger(R6Player.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long _id;

    @Column(name = "playerName", nullable = false, length = 25)
    private String _playerName;

    @Column(name = "playerID", nullable = false, length = 50)
    private String _playerID;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "playerRank", nullable = false, length = 25)
    private R6Rank _playerRank;

    @OneToOne(optional = false)
    @JoinColumn(name = "ts3user", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private TS3User _ts3User;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * create a new {@link R6Player} with the given fields
     *
     * @param playerName can not be {@code null}
     * @param playerRank can not be {@code null}
     * @param playerID   can not be {@code null}
     */
    public R6Player(String playerName, R6Rank playerRank, String playerID) {
        assert playerName != null;
        assert playerRank != null;
        assert playerID != null;

        _playerName = playerName;
        _playerRank = playerRank;
        _playerID = playerID;
        _id = 0;
    }

    /**
     * jpa constructor
     */
    protected R6Player() {
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * copy the raw values. The {@link TS3User} will not be updated
     *
     * @param r6Player can not be {@code null}
     */
    public void copyValues(R6Player r6Player) {
        assert r6Player != null;

        this._playerID = r6Player._playerID;
        this._playerRank = r6Player._playerRank;
        this._playerName = r6Player._playerName;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return the name of the player
     */
    public String getPlayerName() {
        return _playerName;
    }

    /**
     * @return the rank of the player
     */
    public R6Rank getPlayerRank() {
        return _playerRank;
    }

    /**
     * @return the database id for the {@link R6Player}
     */
    public long getId() {
        return _id;
    }

    /**
     * @return the uplay/playerId from R6Tab
     */
    public String getPlayerID() {
        return _playerID;
    }

    public TS3User getTs3User() {
        return _ts3User;
    }

    /**
     * @param ts3User for the {@link R6Player}, can not be {@code null}
     */
    public void setTs3User(TS3User ts3User) {
        assert ts3User != null;
        _ts3User = ts3User;
    }
}
