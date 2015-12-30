package be.dieterholvoet.beerapp;

import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.Log;

import be.dieterholvoet.beerapp.R;
import be.dieterholvoet.beerapp.rest.BreweryDB;

/**
 * Created by Dieter on 30/12/2015.
 */

class SearchSuggestionThread extends AsyncTask<Void, Void, Void> {
    SimpleCursorAdapter searchViewAdapter;
    SearchView searchView;
    MatrixCursor cursor;
    BreweryDB dao;
    String query;

    public SearchSuggestionThread(SearchView searchView, BreweryDB dao, String query) {
        this.searchView = searchView;
        this.searchViewAdapter = (SimpleCursorAdapter) searchView.getSuggestionsAdapter();
        this.cursor = (MatrixCursor) this.searchViewAdapter.getCursor();
        this.dao = dao;
        this.query = query;
        Log.e("thread", "constructor");
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.e("thread", "doInBackground");
        this.cursor = dao.convertToCursor(dao.searchBeersSynchronous(query));
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.e("thread", "onPostExecute");
        this.searchViewAdapter.changeCursor(cursor);
    }
}
