package io.github.simonhauck.ts3r6bot.model.ts3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

@Entity
@Table(name = "noob_user")
public class TS3NoobUser {

    private static final Logger LOG = LoggerFactory.getLogger(TS3NoobUser.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long _id;

    //Noob data
    @Column(name = "ts3_uid", unique = true, nullable = false, length = 50)
    private String _ts3UniqueIdentifier;

    @Column(name = "active", nullable = false)
    private boolean _noobActive;

    @Column(name = "noob_timeStamp", nullable = true)
    private long _noobUntilTimeStamp;

    @Column(name = "ts3Name", nullable = true)
    private String _ts3Name;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * create a new noob user with the uid and the time until the user is a noob
     *
     * @param ts3Name             The name of the user. Can not be {@code null}
     * @param ts3UniqueIdentifier can not be {@code null}
     * @param noobUntilTimeStamp  unix timestamp in ms until the user is a noob
     */
    public TS3NoobUser(String ts3Name, String ts3UniqueIdentifier, long noobUntilTimeStamp) {
        _ts3UniqueIdentifier = ts3UniqueIdentifier;
        _noobUntilTimeStamp = noobUntilTimeStamp;
        _noobActive = true;
        _ts3Name = ts3Name;
    }

    /**
     * jpa constructor
     */
    protected TS3NoobUser() {
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * update the values of the user with the fields of the given user. The id will not be copied
     *
     * @param noobUser can not be {@code null}
     */
    public void updateValues(TS3NoobUser noobUser) {
        _ts3UniqueIdentifier = noobUser.getTs3UniqueIdentifier();
        _noobUntilTimeStamp = noobUser.getNoobUntilTimeStamp();
        _noobActive = noobUser.isNoobActive();
        _ts3Name = noobUser.getTs3Name();
    }

    @Override
    public String toString() {
        return "TS3NoobUser{" +
                "_id=" + _id +
                ", _ts3UniqueIdentifier='" + _ts3UniqueIdentifier + '\'' +
                ", _noobActive=" + _noobActive +
                ", _noobUntilTimeStamp=" + _noobUntilTimeStamp +
                ", _ts3Name='" + _ts3Name + '\'' +
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

    public boolean isNoobActive() {
        return _noobActive;
    }

    public void setNoobActive(boolean noobActive) {
        _noobActive = noobActive;
    }

    public long getNoobUntilTimeStamp() {
        return _noobUntilTimeStamp;
    }

    public void setNoobUntilTimeStamp(long noobUntilTimeStamp) {
        _noobUntilTimeStamp = noobUntilTimeStamp;
    }

    public String getTs3Name() {
        return _ts3Name;
    }

    public void setTs3Name(String ts3Name) {
        _ts3Name = ts3Name;
    }
}
