package be.dieterholvoet.beerguide.rest;

import android.content.Context;
import android.database.MatrixCursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.model.BreweryDBBrewery;
import be.dieterholvoet.beerguide.rest.model.BreweryDBResponseBrewery;
import be.dieterholvoet.beerguide.rest.model.BreweryDBResponseLookup;
import be.dieterholvoet.beerguide.rest.model.BreweryDBResultBeer;
import be.dieterholvoet.beerguide.rest.model.BreweryDBResponseSearch;
import be.dieterholvoet.beerguide.tasks.EndpointAvailabilityCheckTask;
import retrofit2.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dieter on 30/12/2015.
 */

public class BreweryDB {
    private static BreweryDB instance;

    private Retrofit restAdapter;
    private BreweryDBSuggestionsService searchService;

    private final String[] columns = new String[]{"_id", "BEER_NAME", "BREWERYDB_ID", "BEER_ICON"};
    private final String API_KEY = "63d5648e9125519e5f284d89a1e50f3e";
    private final String BASE_URL = "http://api.brewerydb.com/v2";
    private final String LOG_TAG = "DAO";

    private static boolean dailyLimitReached = false;

    public BreweryDB() {
        restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        searchService = restAdapter.create(BreweryDBSuggestionsService.class);
    }

    public static BreweryDB getInstance() {
        if(instance == null) {
            instance = new BreweryDB();
        }
        return instance;
    }

    public String[] getColumns() {
        return columns;
    }

    public List<BreweryDBResultBeer> searchBeers(String query) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", query);

        Call<BreweryDBResponseSearch> call = searchService.searchBeers(queryMap);
        BreweryDBResponseSearch response = (BreweryDBResponseSearch) executeAndTestResponse(call);

        if(response == null) {
            return null;

        } else {
            return response.getData();
        }
    }

    public BreweryDBBeer getBeerByID(String beerID) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        Call call = searchService.getBeerByID(beerID, queryMap);
        BreweryDBResponseLookup data = (BreweryDBResponseLookup) executeAndTestResponse(call);

        if(data == null) {
            return null;

        } else {
            return new BreweryDBBeer(data.getData());
        }
    }

    public BreweryDBBrewery getBreweryByID(String breweryID) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);

        Call call = searchService.getBreweryByID(breweryID, queryMap);
        BreweryDBResponseBrewery data = (BreweryDBResponseBrewery) executeAndTestResponse(call);

        if(data == null) {
            return null;

        } else {
            return new BreweryDBBrewery(data.getData());
        }
    }

    public Beer getBreweryDBData(Beer beer) {
        BreweryDBBeer bdb = beer.getBdb();
        if(bdb == null) {
            Log.e("LOG", "Can't get bdb data, bdb is null!");
            return beer;

        } else {
            if(bdb.getDescription() == null) {
                if(bdb.getBreweryDBID() == null) {
                    Log.e("API", "No BreweryDB ID provided.");

                } else {
                    Log.e("ERROR", "BEER ID: " + bdb.getBreweryDBID());
                    beer.setBdb(getBeerByID(bdb.getBreweryDBID()));
                }
            }

            return beer;
        }
    }

    public List<Beer> getBreweryDBData(List<Beer> beers) {
        for(int i = 0; i < beers.size(); i++) {
            beers.set(i, getBreweryDBData(beers.get(i)));
        }
        return beers;
    }

    private Object executeAndTestResponse(Call call) {
        Response response = null;

        try {
            response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response == null) {
            Log.e(LOG_TAG, "Response is null");

        } else {
            if (response.isSuccessful()) {
                if (response.body() == null) {
                    Log.e(LOG_TAG, "Result is null");
                    Log.e(LOG_TAG, response.message());

                } else {
                    return response.body();
                }

            } else {
                try {
                    String message = new JSONObject(response.errorBody().string()).getString("errorMessage");
                    if(message.indexOf("API limit") != -1) {
                        Log.e(LOG_TAG, "Daily API limit was reached. Try again tomorrow.");
                        dailyLimitReached = true;

                    } else {
                        Log.e(LOG_TAG, message);
                    }

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public MatrixCursor convertToCursor(List list) {
        MatrixCursor cursor = new MatrixCursor(columns);

        if(list != null) {
            if(list.get(0) instanceof BreweryDBResultBeer) {
                for(int i = 0; i < list.size(); i++) {
                    BreweryDBResultBeer result = (BreweryDBResultBeer) list.get(i);
                    ArrayList<Object> row = new ArrayList<>();

                    row.add(i);
                    row.add(result.getName());
                    row.add(result.getId());

                    if(result.getLabels() != null) {
                        row.add(result.getLabels().getIcon());
                    } else {
                        row.add(null);
                    }

                    cursor.addRow(row.toArray(new Object[row.size()]));
                }

            } else if(list.get(0) instanceof Beer) {
                for(int i = 0; i < list.size(); i++) {
                    Beer beer = (Beer) list.get(i);
                    BreweryDBBeer bdb = beer.getBdb();
                    ArrayList<Object> row = new ArrayList<>();

                    row.add(i);
                    row.add(bdb.getName());
                    row.add(bdb.getId());

                    if(bdb.getLabels() != null) {
                        row.add(bdb.getLabels().getIcon());

                    } else {
                        row.add(null);
                    }

                    cursor.addRow(row.toArray(new Object[row.size()]));
                }

            } else {
                Log.e(LOG_TAG, "Unknown list item type.");
            }
        }

        return cursor;
    }

    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if(!networkAvailable) {
            Log.e("API", "No network available.");
        }

        return networkAvailable;
    }

    public static void isEndpointAvailable(Context context) {
        new EndpointAvailabilityCheckTask(context).execute();
    }

    public static boolean isDailyLimitReached() {
        return dailyLimitReached;
    }
}
