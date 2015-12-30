package be.dieterholvoet.beerapp.rest;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import be.dieterholvoet.beerapp.R;

/**
 * Created by Dieter on 29/12/2015.
 */

public class SearchBeerResultsAdapter extends SimpleCursorAdapter {
    private static final String tag = SearchBeerResultsAdapter.class.getName();
    private Context context = null;

    public SearchBeerResultsAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        this.context=context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById(R.id.beer_icon);
        TextView textView = (TextView)view.findViewById(R.id.beer_name);

        // Picasso.with(context).load(cursor.getString(2)).into(imageView);
        textView.setText(cursor.getString(1));
    }
}
