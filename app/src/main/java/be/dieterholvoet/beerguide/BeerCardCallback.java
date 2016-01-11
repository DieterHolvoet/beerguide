package be.dieterholvoet.beerguide;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import be.dieterholvoet.beerguide.adapters.BeerListAdapter;
import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 10/01/2016.
 */

public class BeerCardCallback extends ItemTouchHelper.SimpleCallback {
    RecyclerView recycler;

    public BeerCardCallback(int dragDirs, int swipeDirs, RecyclerView recycler) {
        super(dragDirs, swipeDirs);
        this.recycler = recycler;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
        final BeerListAdapter adapter = (BeerListAdapter) recycler.getAdapter();
        final RecyclerView.LayoutManager lm = recycler.getLayoutManager();
        final Snackbar snackbar = Snackbar.make(recycler, "Beer removed.", Snackbar.LENGTH_LONG);
        final int position = viewHolder.getAdapterPosition();
        final Beer beer = adapter.getAt(position);
        adapter.removeAt(position);
        beer.delete();

        snackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                adapter.setAt(position, beer);
                beer.save();
                lm.scrollToPosition(position);
                Log.e("BEER", "Removal cancelled.");
            }
        });

        snackbar.show();
    }
}
