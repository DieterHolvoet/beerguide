package be.dieterholvoet.beerguide.tasks;

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

public class SearchSuggestionTask extends AsyncTask<Void, Void, Void> {
    SimpleCursorAdapter searchViewAdapter;
    SearchView searchView;
    MatrixCursor cursor;
    BreweryDB dao;
    String query;
    List<BreweryDBResultBeer> results;

    public SearchSuggestionTask(SearchView searchView, BreweryDB dao, String query) {
        this.searchView = searchView;
        this.searchViewAdapter = (SimpleCursorAdapter) searchView.getSuggestionsAdapter();
        this.cursor = (MatrixCursor) this.searchViewAdapter.getCursor();
        this.dao = dao;
        this.query = query;
    }

    @Override
    protected Void doInBackground(Void... params) {
        this.results = dao.searchBeers(query);

        if(results == null) {
            cancel(true);
        } else {
            this.cursor = dao.convertToCursor(results);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        this.searchViewAdapter.changeCursor(cursor);
    }
}
