package io.github.simonhauck.ts3r6bot.r6;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface R6TabService {

    @GET("api/search.php")
    Call<R6TabSearchResult> searchPlayer(@Query("platform") String platform, @Query("search") String name);

    @GET("mainpage.php")
    Call<Void> triggerR6TabUpdate(@Query("page") String playerID, @Query("updatenow") boolean update);

}
