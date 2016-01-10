package be.dieterholvoet.beerguide.rest;

import android.database.MatrixCursor;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.model.BreweryDBResponseLookup;
import be.dieterholvoet.beerguide.model.BreweryDBResultBeer;
import be.dieterholvoet.beerguide.model.BreweryDBResponseSearch;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

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
        final List<BreweryDBResultBeer>[] results = new List[1];
        final BreweryDB context = this;

        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", query);

        Call<BreweryDBResponseSearch> call = searchService.searchBeers(queryMap);
        call.enqueue(new Callback<BreweryDBResponseSearch>() {

            @Override
            public void onResponse(Response<BreweryDBResponseSearch> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    if(response.body().getData() == null) {
                        Log.e(LOG_TAG, response.message());

                    } else {
                        List<BreweryDBResultBeer> BreweryDBBeers = response.body().getData();
                        results[0] = BreweryDBBeers;
                    }


                } else {
                    try {
                        Log.e(LOG_TAG, response.errorBody().string());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    throw t;

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });

        return results[0];
    }

    public List<BreweryDBResultBeer> searchBeersSynchronous(String query) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", query);

        Call<BreweryDBResponseSearch> call = searchService.searchBeers(queryMap);
        BreweryDBResponseSearch response = (BreweryDBResponseSearch) executeAndTestResponse(call);

        return response.getData();
    }

    public BreweryDBBeer getBeerByID(String beerID) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);

        Call call = searchService.getBeerByID(beerID, queryMap);
        BreweryDBResponseLookup data = (BreweryDBResponseLookup) executeAndTestResponse(call);

        return new BreweryDBBeer(data.getData());
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
            if (response.isSuccess()) {
                if (response.body() == null) {
                    Log.e(LOG_TAG, "Result is null");
                    Log.e(LOG_TAG, response.message());

                } else {
                    return response.body();
                }

            } else {
                try {
                    Log.e(LOG_TAG, response.errorBody().string());

                } catch (IOException e) {
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
                Log.e("", "Unknown list item type.");
            }
        }

        return cursor;
    }
}
