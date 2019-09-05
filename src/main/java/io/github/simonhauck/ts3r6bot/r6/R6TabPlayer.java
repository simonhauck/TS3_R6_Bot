package io.github.simonhauck.ts3r6bot.r6;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
class R6TabPlayer {

    private static final Logger LOG = LoggerFactory.getLogger(R6TabPlayer.class);

    @SerializedName(value = "p_id")
    private String _playerID;

    @SerializedName(value = "p_name")
    private String _playerName;

    @SerializedName(value = "p_level")
    private String _level;

    @SerializedName(value = "p_platform")
    private String _platform;

    @SerializedName(value = "p_user")
    private String _playerUser;

    @SerializedName(value = "p_currentmmr")
    private String _playerCurrentMMR;

    @SerializedName(value = "p_currentrank")
    private String _playerCurrentRank;

    @SerializedName(value = "kd")
    private String _playerKD;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return is the Identifier assigned by ubisoft to the player
     */
    public String getPlayerID() {
        return _playerID;
    }

    /**
     * @return is the current name of the player
     */
    public String getPlayerName() {
        return _playerName;
    }

    /**
     * @return is the current level of the player
     */
    public String getLevel() {
        return _level;
    }

    /**
     * @return of the player
     */
    public String getPlatform() {
        return _platform;
    }

    /**
     * @return id to get the player image
     */
    public String getPlayerUser() {
        return _playerUser;
    }

    /**
     * @return get the current mmr as a string
     */
    public String getPlayerCurrentMMR() {
        return _playerCurrentMMR;
    }

    /**
     * @return get the current rank of the player
     */
    public String getPlayerCurrentRank() {
        return _playerCurrentRank;
    }

    /**
     * @return get the kill death ratio. Must be divided bei 100
     */
    public String getPlayerKD() {
        return _playerKD;
    }
}
