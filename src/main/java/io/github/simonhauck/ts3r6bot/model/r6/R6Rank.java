package io.github.simonhauck.ts3r6bot.model.r6;

import org.springframework.core.env.Environment;

public enum R6Rank {

    NO_RANK("No Rank", "norank", 0),
    COPPER_4("Copper 4", "copper4", 1),
    COPPER_3("Copper 3", "copper3", 2),
    COPPER_2("Copper 2", "copper2", 3),
    COPPER_1("Copper 1", "copper1", 4),
    BRONZE_4("Bronze 4", "bronze4", 5),
    BRONZE_3("Bronze 3", "bronze3", 6),
    BRONZE_2("Bronze 2", "bronze2", 7),
    BRONZE_1("Bronze 1", "bronze1", 8),
    SILVER_4("Silver 4", "silver4", 9),
    SILVER_3("Silver 3", "silver3", 10),
    SILVER_2("Silver 2", "silver2", 11),
    SILVER_1("Silver 1", "silver1", 12),
    GOLD_4("Gold 4", "gold4", 13),
    GOLD_3("Gold 3", "gold3", 14),
    GOLD_2("Gold 2", "gold2", 15),
    GOLD_1("Gold 1", "gold1", 16),
    PLATINUM_3("Platinum 3", "platinum3", 17),
    PLATINUM_2("Platinum 2", "platinum2", 18),
    PLATINUM_1("Platinum 1", "platinum1", 19),
    DIAMOND("Diamond", "diamond", 20);

    private static final String PROPERTY_PREFIX = "ranks.";

    private String _rankName;
    private String _propertyKey;
    private int _channelID;
    private int _rankNumber;

    R6Rank(String rankName, String propertyKey, int rankNumber) {
        _rankName = rankName;
        _propertyKey = propertyKey;
        _channelID = -1;
        _rankNumber = rankNumber;
    }

    /**
     * get the name of the rank
     *
     * @return can not be {@code null}
     */
    public String getRankName() {
        return _rankName;
    }

    /**
     * @return the corresponding rank number
     */
    public int getRankNumber() {
        return _rankNumber;
    }

    /**
     * get the channel id with the given {@link Environment}
     *
     * @param env to access the properties. Can not be {@code null}.
     * @return the channel id
     */
    public int getChannelID(Environment env) {
        assert env != null;

        //If already set, return the stored value
        if (_channelID != -1) return _channelID;

        String propertyKey = PROPERTY_PREFIX + _propertyKey;
        _channelID = Integer.valueOf(env.getProperty(propertyKey));
        return _channelID;
    }

    @Override
    public String toString() {
        return _rankName;
    }
}
