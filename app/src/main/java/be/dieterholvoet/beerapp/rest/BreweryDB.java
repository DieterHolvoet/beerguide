package be.dieterholvoet.beerapp.rest;

import android.database.MatrixCursor;
import android.util.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import be.dieterholvoet.beerapp.model.BreweryDBBeer;
import be.dieterholvoet.beerapp.model.BreweryDBResponse;
import be.dieterholvoet.beerapp.model.SearchResponse;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Dieter on 30/12/2015.
 */

public class BreweryDB {
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

    public String[] getColumns() {
        return columns;
    }

    public List<BreweryDBBeer> searchBeers(String query) {
        final List<BreweryDBBeer>[] results = new List[1];
        final BreweryDB context = this;

        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", query);

        Call<SearchResponse> call = searchService.searchBeers(queryMap);
        call.enqueue(new Callback<SearchResponse>() {

            @Override
            public void onResponse(Response<SearchResponse> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    if(response.body().getData() == null) {
                        Log.e(LOG_TAG, response.message());

                    } else {
                        List<BreweryDBBeer> BreweryDBBeers = response.body().getData();
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

    public ArrayList<BreweryDBBeer> searchBeersSynchronous(String query) {
        ArrayList<BreweryDBBeer> results = new ArrayList<>();

        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", query);

        Call<SearchResponse> call = searchService.searchBeers(queryMap);
        ArrayList<BreweryDBBeer> data = (ArrayList<BreweryDBBeer>) executeAndTestResponse(call);

        return data;
    }

    public BreweryDBBeer getBeerByID(String beerID) {
        final BreweryDBBeer[] result = new BreweryDBBeer[1];
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);

        Log.e(LOG_TAG, "Getting beer by ID " + beerID);

        Call<BreweryDBResponse> call = searchService.getBeerByID(beerID, queryMap);
        call.enqueue(new Callback<BreweryDBResponse>() {

            @Override
            public void onResponse(Response<BreweryDBResponse> response, Retrofit retrofit) {
                Log.e(LOG_TAG, "Got response");
                Log.e(LOG_TAG, response.message());

                if (response.isSuccess()) {
                    result[0] = response.body().getData();

                    if (result[0] == null) {
                        Log.e(LOG_TAG, "Result is null");
                        Log.e(LOG_TAG, response.message());

                    } else {
                        Log.e(LOG_TAG, "Result is not null: " + response.body().getData().toString());
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
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                t.printStackTrace(pw);
                Log.e(LOG_TAG, sw.toString());
            }

        });

        return result[0];
    }

    public BreweryDBBeer getBeerByIDSynchronous(String beerID) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);

        Call<BreweryDBResponse> call = searchService.getBeerByID(beerID, queryMap);
        BreweryDBBeer data = (BreweryDBBeer) executeAndTestResponse(call);

        return data;
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

    public MatrixCursor convertToCursor(List<BreweryDBBeer> BreweryDBBeers) {
        MatrixCursor cursor = new MatrixCursor(columns);

        if(BreweryDBBeers != null) {
            for(int i = 0; i < BreweryDBBeers.size(); i++) {
                BreweryDBBeer BreweryDBBeer = BreweryDBBeers.get(i);
                ArrayList<Object> row = new ArrayList<>();

                row.add(i);
                row.add(BreweryDBBeer.getName());
                row.add(BreweryDBBeer.getId());

                if(BreweryDBBeer.getLabels() != null) {
                    row.add(BreweryDBBeer.getLabels().getIcon());
                } else {
                    row.add(null);
                }

                cursor.addRow(row.toArray(new Object[row.size()]));
            }
        }

        return cursor;
    }
}
