package be.dieterholvoet.beerguide;

import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;

import java.util.List;

import be.dieterholvoet.beerguide.model.BreweryDBResultBeer;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 30/12/2015.
 */

public class SearchSuggestionThread extends AsyncTask<Void, Void, Void> {
    SimpleCursorAdapter searchViewAdapter;
    SearchView searchView;
    MatrixCursor cursor;
    BreweryDB dao;
    String query;
    List<BreweryDBResultBeer> results;

    public SearchSuggestionThread(SearchView searchView, BreweryDB dao, String query) {
        this.searchView = searchView;
        this.searchViewAdapter = (SimpleCursorAdapter) searchView.getSuggestionsAdapter();
        this.cursor = (MatrixCursor) this.searchViewAdapter.getCursor();
        this.dao = dao;
        this.query = query;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.e("thread", "Starting search.");
        this.results = dao.searchBeersSynchronous(query);
        Log.e("thread", "Converting to cursor.");
        this.cursor = dao.convertToCursor(results);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("thread", "Search done!");
        this.searchViewAdapter.changeCursor(cursor);
    }
}
