package be.dieterholvoet.beerguide.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.SearchView;

import be.dieterholvoet.beerguide.MainActivity;
import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;

/**
 * Created by Dieter on 9/01/2016.
 */
public class SearchSuggestionListener implements SearchView.OnSuggestionListener {

    private SearchView view;
    private Activity context;

    public SearchSuggestionListener(Activity context, SearchView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {
        Cursor cursor = (Cursor) view.getSuggestionsAdapter().getItem(position);
        Intent intent = new Intent(context, NewBeerActivity.class);
        Bundle b = new Bundle();
        b.putString("beerName", cursor.getString(1));
        b.putString("bdbID", cursor.getString(2));
        intent.putExtras(b);
        context.startActivity(intent);
        context.finish();
        return true;
    }
}
