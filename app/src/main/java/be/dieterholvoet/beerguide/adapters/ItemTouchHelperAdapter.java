package be.dieterholvoet.beerguide.adapters;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Dieter on 24/01/2016.
 */

public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(RecyclerView recycler, int position);
}
