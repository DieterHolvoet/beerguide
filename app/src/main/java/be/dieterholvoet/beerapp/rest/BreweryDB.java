package be.dieterholvoet.beerapp.rest;

import android.database.MatrixCursor;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private final boolean DEBUGGING = true;

    public BreweryDB() {


        if(DEBUGGING) {
            // Source: http://stackoverflow.com/a/33328524
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.interceptors().add(logging);

            restAdapter = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

        } else {
            restAdapter = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        searchService = restAdapter.create(BreweryDBSuggestionsService.class);
    }

    public String[] getColumns() {
        return columns;
    }

    public List<BreweryDBResult> searchBeers(String query) {
        final List<BreweryDBResult>[] results = new List[1];
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
                        List<BreweryDBResult> BreweryDBResults = response.body().getData();
                        results[0] = BreweryDBResults;
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

    public List<BreweryDBResult> searchBeersSynchronous(String query) {
        final List<BreweryDBResult>[] results = new List[1];
        final BreweryDB context = this;

        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", query);

        Call<SearchResponse> call = searchService.searchBeers(queryMap);
        Response<SearchResponse> response = null;
        List<BreweryDBResult> data = null;

        try {
            response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.isSuccess()) {
            data = response.body().getData();

            if (data == null) {
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

        return data;
    }

    public BreweryDBResult getBeerByID(String beerID) {
        final BreweryDBResult[] result = new BreweryDBResult[1];
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);

        Log.e(LOG_TAG, "Getting beer by ID " + beerID);

        Call<BeerResponse> call = searchService.getBeerByID(beerID, queryMap);
        call.enqueue(new Callback<BeerResponse>() {

            @Override
            public void onResponse(Response<BeerResponse> response, Retrofit retrofit) {
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

    public BreweryDBResult getBeerByIDSynchronous(String beerID) {
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);

        Log.e(LOG_TAG, "Getting beer by ID " + beerID);

        Call<BeerResponse> call = searchService.getBeerByID(beerID, queryMap);
        Response<BeerResponse> response = null;
        BreweryDBResult data = null;

        try {
            response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.isSuccess()) {
            data = response.body().getData();

            if (data == null) {
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

        return data;
    }


    public MatrixCursor convertToCursor(List<BreweryDBResult> BreweryDBResults) {
        MatrixCursor cursor = new MatrixCursor(columns);

        if(BreweryDBResults != null) {
            for(int i = 0; i < BreweryDBResults.size(); i++) {
                BreweryDBResult BreweryDBResult = BreweryDBResults.get(i);
                ArrayList<Object> row = new ArrayList<>();

                row.add(i);
                row.add(BreweryDBResult.getName());
                row.add(BreweryDBResult.getId());

                if(BreweryDBResult.getLabels() != null) {
                    row.add(BreweryDBResult.getLabels().getIcon());
                } else {
                    row.add(null);
                }

                cursor.addRow(row.toArray(new Object[row.size()]));
            }
        }

        return cursor;
    }
}
