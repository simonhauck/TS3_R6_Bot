package io.github.simonhauck.ts3r6bot.r6;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.simonhauck.ts3r6bot.model.r6.R6Platform;
import io.github.simonhauck.ts3r6bot.model.r6.R6Player;
import io.github.simonhauck.ts3r6bot.model.r6.R6Rank;
import io.github.simonhauck.ts3r6bot.model.r6.R6Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

@Component(value = "r6tab")
public class R6TabApi implements IR6Api {

    private static final Logger LOG = LoggerFactory.getLogger(R6TabApi.class);
    private static final String BASE_URL = "https://r6tab.com/";

    private static final Gson GSON = new GsonBuilder()
            .setLenient()
            .create();

    private static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .build();

    //Service api
    private R6TabService _r6TabService;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    public R6TabApi() {
        _r6TabService = RETROFIT.create(R6TabService.class);
    }

    //------------------------------------------------------------------------------------------------------------------
    // IR6Api methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public R6Player getPlayerByName(String name, R6Platform platform, R6Region r6Region) {
        LOG.info("Requesting player data for name: " + name + " and platform: " + platform);

        //Get platform dependent string
        String searchPlatform = getPlatform(platform);

        //Create call
        Call<R6TabSearchResult> call = _r6TabService.searchPlayer(searchPlatform, name);

        //Execute call
        Response<R6TabSearchResult> requestResult = null;

        //Execute request synchronous
        try {
            requestResult = call.execute();
        } catch (IOException e) {
            LOG.error("Exception occurred in the request to r6TabApi", e);
            return null;
        }

        //Check if response is correct
        if (!requestResult.isSuccessful()) {
            LOG.warn("Request was not successful. Request returned with code: " + requestResult.code() + ". Cancel request");
            return null;
        }

        R6TabSearchResult result = requestResult.body();

        //Check if response could be parsed
        if (result == null) {
            LOG.error("Error: An unexpected response was received or no response was received. The search result is null!");
            return null;
        }

        //Check if a result was found
        if (result.getTotalResults() == 0) {
            LOG.info("No player found for name: " + name + " and platform: " + platform + ". The result list does not contain any elements");
            return null;
        } else {
            LOG.info("The result contains " + result.getTotalResults() + " elements.");
        }

        String cleanedSearchName = name.toLowerCase().trim();
        for (R6TabPlayer tabPlayer : result.getResults()) {
            String cleanedResultName = tabPlayer.getPlayerName().toLowerCase().trim();

            //Name matches the requested name
            if (cleanedResultName.equals(cleanedSearchName)) {
                LOG.debug("Corresponding player found. Parsing player: " + tabPlayer.toString());
                return parsePlayer(tabPlayer);
            }
        }

        LOG.warn("No corresponding Player found in the result for the name: " + cleanedSearchName);
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * trigger an update on the r6tab page for the given {@link R6Player}
     *
     * @param player can not be {@code null}
     */
    public void triggerR6TabUpdate(R6Player player) {
        assert player != null;

        LOG.debug("Trigger update for player: " + player.getPlayerName() + " and id: " + player.getPlayerID());
        Call<Void> call = _r6TabService.triggerR6TabUpdate(player.getPlayerID(), true);

        try {
            call.execute();
            LOG.debug("Update for player " + player.getPlayerName() + " successful.");
        } catch (IOException e) {
            LOG.error("Error while triggering the r6TabUpdate", e);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * get the search param for the given platform
     *
     * @param platform that should be searched on R6Tab
     * @return the value for the search param
     */
    private String getPlatform(R6Platform platform) {
        switch (platform) {
            case XBOX:
                return "xbl";
            case PLAYSTATION:
                return "psn";
            case UPLAY:
                return "uplay";
            default:
                LOG.error("No matching platform found!");
                throw new AssertionError("No matching platform found!");
        }
    }

    /**
     * parse a {@link R6TabPlayer} in a {@link R6Player} object
     *
     * @param r6TabPlayer can not be {@code null}
     * @param ts3User     can not be {@code null}
     * @return the parsed player. Can not be {@code null}
     */
    private R6Player parsePlayer(R6TabPlayer r6TabPlayer) {
        String playerName = r6TabPlayer.getPlayerName();
        R6Rank playerRank = getRank(Integer.parseInt(r6TabPlayer.getPlayerCurrentRank()));
        String playerID = r6TabPlayer.getPlayerID();

        R6Player player = new R6Player(playerName, playerRank, playerID);
        return player;
    }

    /**
     * @param rankNumber the rank number. Should match the number of {@link R6Rank}
     * @return the corresponding rank
     */
    private R6Rank getRank(int rankNumber) {
        R6Rank[] ranks = R6Rank.values();

        for (R6Rank rank : ranks) {
            if (rank.getRankNumber() == rankNumber) {
                return rank;
            }
        }

        LOG.error("No corresponding rank found. Given id: " + rankNumber);
        throw new AssertionError("No corresponding rank found. Given id: " + rankNumber);
    }

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------
}
