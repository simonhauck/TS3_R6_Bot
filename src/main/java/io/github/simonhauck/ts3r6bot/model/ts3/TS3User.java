package io.github.simonhauck.ts3r6bot.model.ts3;

import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "ts3user")
public class TS3User {

    private static final Logger LOG = LoggerFactory.getLogger(TS3User.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long _id;

    @Column(name = "ts3_uid", unique = true, nullable = false, length = 50)
    private String _ts3UniqueIdentifier;

    @Column(name = "uplay_name", nullable = false, length = 25)
    private String _uplayName;

    @Column(name = "firstname", nullable = false, length = 25)
    private String _firstName;

    @Column(name = "lastname", nullable = false, length = 25)
    private String _lastName;

    @OneToOne(mappedBy = "_ts3User", optional = true, cascade = {CascadeType.ALL})
    private R6Player _r6Player;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    public TS3User(String ts3UniqueIdentifier, String uplayName, String firstName, String lastName) {
        _ts3UniqueIdentifier = ts3UniqueIdentifier;
        _uplayName = uplayName;
        _firstName = firstName;
        _lastName = lastName;
        _id = 0;
    }

    /**
     * empty constructor for jpa
     */
    protected TS3User() {
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * copy all the data fields in the new user. The id will not be copied
     *
     * @param oldUser can not be {@code null}
     */
    public void updateValues(TS3User oldUser) {
        _ts3UniqueIdentifier = oldUser._ts3UniqueIdentifier;
        _uplayName = oldUser._uplayName;
        _firstName = oldUser._firstName;
        _lastName = oldUser._lastName;
    }

    /**
     * Set the {@link R6Player}.
     * If the player is currently null, set the given player as current value. Else the values will be copied
     *
     * @param player can not be {@code null}
     */
    public void updateR6PlayerStat(R6Player player) {
        assert player != null;
        if (_r6Player == null) {
            _r6Player = player;
            _r6Player.setTs3User(this);
        } else {
            _r6Player.copyValues(player);
        }
    }

    @Override
    public String toString() {
        return "TS3User{" +
                "_id=" + _id +
                ", _ts3UniqueIdentifier='" + _ts3UniqueIdentifier + '\'' +
                ", _uplayName='" + _uplayName + '\'' +
                ", _firstName='" + _firstName + '\'' +
                ", _lastName='" + _lastName + '\'' +
                '}';
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------

    public long getId() {
        return _id;
    }

    public void setId(long id) {
        _id = id;
    }

    public String getTs3UniqueIdentifier() {
        return _ts3UniqueIdentifier;
    }

    public void setTs3UniqueIdentifier(String ts3UniqueIdentifier) {
        _ts3UniqueIdentifier = ts3UniqueIdentifier;
    }

    public String getUplayName() {
        return _uplayName;
    }

    public void setUplayName(String uplayName) {
        _uplayName = uplayName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public R6Player getR6Player() {
        return _r6Player;
    }

    public void setR6Player(R6Player r6Player) {
        _r6Player = r6Player;
    }
}
