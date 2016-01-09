package be.dieterholvoet.beerapp.rest;

import java.util.Map;

import be.dieterholvoet.beerapp.model.BreweryDBResponse;
import be.dieterholvoet.beerapp.model.SearchResponse;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by Dieter on 29/12/2015.
 */

public interface BreweryDBSuggestionsService {
    @GET("v2/search/")
    Call<SearchResponse> searchBeers(@QueryMap Map<String, String> options);

    @GET("v2/beer/{beerID}")
    Call<BreweryDBResponse> getBeerByID(@Path("beerID") String beerID, @QueryMap Map<String, String> options);
}
