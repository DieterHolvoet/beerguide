package be.dieterholvoet.beerguide;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Dieter on 10/01/2016.
 */
public class BeerCardDecoration extends RecyclerView.ItemDecoration {
    // Source: http://stackoverflow.com/a/30794046/2637528

    private int mItemOffset;

    public BeerCardDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public BeerCardDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        Log.e("POS", "Position: " + position);

        if(position % 2 == 0) {
            outRect.set(mItemOffset, mItemOffset, mItemOffset / 2, mItemOffset);

        } else {
            outRect.set(mItemOffset / 2, mItemOffset, mItemOffset / 2, mItemOffset);
        }

    }
}