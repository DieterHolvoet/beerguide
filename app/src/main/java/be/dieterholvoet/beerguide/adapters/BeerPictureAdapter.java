package be.dieterholvoet.beerguide.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.helper.ImageHelper;
import be.dieterholvoet.beerguide.model.ImageStore;

/**
 * Created by Dieter on 5/02/2016.
 */

public class BeerPictureAdapter extends RecyclerView.Adapter<BeerPictureAdapter.BeerPictureViewHolder> {

    private static List<ImageStore> pictures;
    private Activity activity;

    public BeerPictureAdapter(List<ImageStore> pictures, Activity activity) {
        this.pictures = pictures;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    @Override
    public void onBindViewHolder(final BeerPictureViewHolder viewHolder, int i) {
        ImageStore picture = pictures.get(i);
        ImageHelper.loadInView(activity, viewHolder.img, 100, 150, picture.getUri());
    }

    @Override
    public BeerPictureAdapter.BeerPictureViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_picture, viewGroup, false);

        return new BeerPictureViewHolder(v, activity);
    }

    public static class BeerPictureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected ImageView img;
        protected Activity activity;

        public BeerPictureViewHolder(View v, Activity activity) {
            super(v);
            v.setOnClickListener(this);
            this.activity = activity;
            this.img = (ImageView)  v.findViewById(R.id.item_picture_image);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            ImageStore image = pictures.get(pos);
            ImageHelper.openInGallery(activity, image.getUri());
        }
    }

    // Source: http://stackoverflow.com/a/26310638/2637528
    public void removeAt(int position) {
        pictures.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pictures.size());
    }

    public ImageStore getAt(int position) {
        if(pictures.size() > 0) {
            return pictures.get(position);

        } else {
            Log.e("LOG", "Can't get picture from empty list");
            return null;
        }
    }

    public void setAt(int position, ImageStore image) {
        pictures.add(position, image);
        notifyItemChanged(position);
        notifyItemRangeChanged(position, pictures.size());
    }
}