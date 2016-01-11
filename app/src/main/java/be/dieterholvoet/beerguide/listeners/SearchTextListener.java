package be.dieterholvoet.beerguide.listeners;

import android.database.MatrixCursor;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.support.v4.widget.CursorAdapter;

import java.util.Timer;
import java.util.TimerTask;

import be.dieterholvoet.beerguide.tasks.SearchSuggestionTask;
import be.dieterholvoet.beerguide.rest.BreweryDB;

/**
 * Created by Dieter on 9/01/2016.
 */
public class SearchTextListener implements SearchView.OnQueryTextListener {
    // Source: http://stackoverflow.com/a/28625786/2637528
    private Timer timer = new Timer();
    private final long DELAY = 500; // in ms
    private SearchView view;
    private CursorAdapter adapter;
    private BreweryDB dao;

    public SearchTextListener(SearchView view) {
        this.dao = BreweryDB.getInstance();
        this.view = view;
        this.adapter = view.getSuggestionsAdapter();
    }

    private void loadSearchData(String query) {
        new SearchSuggestionTask(view, dao, query).execute();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return this.onQueryTextChange(query);
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if(timer != null) {
            timer.cancel();
            adapter.changeCursor(new MatrixCursor(dao.getColumns()));
        }

        if (newText.length() >= 2) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("LOG", "Search!");
                    loadSearchData(newText);
                }

            }, DELAY);
        }

        return true;
    }
}
