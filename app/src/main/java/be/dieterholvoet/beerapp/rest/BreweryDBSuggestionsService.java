package be.dieterholvoet.beerapp.rest;

import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
 * Created by Dieter on 29/12/2015.
 */

public interface BreweryDBSuggestionsService {
    @GET("/v2/search/")
    Call<BreweryDBResponse> searchBeers(@QueryMap Map<String, String> options);
}
