package be.dieterholvoet.beerguide;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import be.dieterholvoet.beerguide.adapters.BeerListAdapter;
import be.dieterholvoet.beerguide.adapters.ItemTouchHelperAdapter;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.model.Beer;

/**
 * Created by Dieter on 10/01/2016.
 * Source: https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-b9456d2b1aaf#.8b4lyxyx3
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final RecyclerView recycler;
    private final ItemTouchHelperAdapter mAdapter;

    public SimpleItemTouchHelperCallback(RecyclerView recycler, ItemTouchHelperAdapter mAdapter) {
        this.recycler = recycler;
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN,
            swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;

        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(recycler, viewHolder.getAdapterPosition());
    }
}
