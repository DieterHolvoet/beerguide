package be.dieterholvoet.beerguide.rest;

import java.util.Map;

import be.dieterholvoet.beerguide.model.BreweryDBResponseBrewery;
import be.dieterholvoet.beerguide.model.BreweryDBResponseLookup;
import be.dieterholvoet.beerguide.model.BreweryDBResponseSearch;
import be.dieterholvoet.beerguide.model.BreweryDBResultBeer;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by Dieter on 29/12/2015.
 */

public interface BreweryDBSuggestionsService {
    @GET("v2/search/")
    Call<BreweryDBResponseSearch> searchBeers(@QueryMap Map<String, String> options);

    @GET("v2/beer/{beerID}")
    Call<BreweryDBResponseLookup> getBeerByID(@Path("beerID") String beerID, @QueryMap Map<String, String> options);

    @GET("v2/brewery/{breweryID}")
    Call<BreweryDBResponseBrewery> getBreweryByID(@Path("breweryID") String breweryID, @QueryMap Map<String, String> options);
}
