package io.github.simonhauck.ts3r6bot.r6;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class R6TabSearchResult {

    private static final Logger LOG = LoggerFactory.getLogger(R6TabSearchResult.class);

    @SerializedName(value = "results")
    private List<R6TabPlayer> _results;

    @SerializedName(value = "totalresults")
    private int _totalResults;

    //------------------------------------------------------------------------------------------------------------------
    // Constructors
    //------------------------------------------------------------------------------------------------------------------

    /**
     * default constructor for r6 tab api
     */
    public R6TabSearchResult() {
    }

    //------------------------------------------------------------------------------------------------------------------
    // Public methods
    //------------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "R6TabSearchResult{" +
                "_results=" + _results +
                ", _totalResults=" + _totalResults +
                '}';
    }

    //------------------------------------------------------------------------------------------------------------------
    // Private methods
    //------------------------------------------------------------------------------------------------------------------

    //------------------------------------------------------------------------------------------------------------------
    // Get and Set methods
    //------------------------------------------------------------------------------------------------------------------

    /**
     * @return the list of all players. Can be null
     */
    public List<R6TabPlayer> getResults() {
        return _results;
    }

    /**
     * @return the amount of results in the list
     */
    public int getTotalResults() {
        return _totalResults;
    }

    public void setResults(List<R6TabPlayer> results) {
        _results = results;
    }

    public void setTotalResults(int totalResults) {
        _totalResults = totalResults;
    }
}
