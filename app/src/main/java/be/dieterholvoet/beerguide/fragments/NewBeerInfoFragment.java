package be.dieterholvoet.beerguide.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.adapters.BeerPictureAdapter;
import be.dieterholvoet.beerguide.bus.BeerLookupTaskEvent;
import be.dieterholvoet.beerguide.bus.EndPointAvailableEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.helper.Helper;
import be.dieterholvoet.beerguide.helper.ImageStore;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import be.dieterholvoet.beerguide.tasks.BeerLookupTask;

/**
 * Created by Dieter on 26/12/2015.
 */

public class NewBeerInfoFragment extends Fragment {
    NewBeerActivity activity;
    PackageManager pm;
    View view;
    Beer beer;

    FloatingActionsMenu menuMultipleActions;
    FloatingActionButton cameraFAB;
    FloatingActionButton galleryFAB;

    TextView name;
    TextView abv;
    TextView ibu;
    TextView srm;
    TextView og;
    TextView brewery;
    TextView style;
    TextView description;

    ImageView img;
    RecyclerView recycler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_new_beer_info, null);
        this.activity = (NewBeerActivity) getActivity();
        this.pm = getContext().getPackageManager();
        this.beer = activity.getBeer();

        this.name = (TextView) view.findViewById(R.id.beer_info_name);
        this.abv = (TextView) view.findViewById(R.id.beer_info_abv_value);
        this.ibu = (TextView) view.findViewById(R.id.beer_info_ibu_value);
        this.srm = (TextView) view.findViewById(R.id.beer_info_srm_value);
        this.og = (TextView) view.findViewById(R.id.beer_info_og_value);
        this.brewery = (TextView) view.findViewById(R.id.beer_info_brewery_value);
        this.style = (TextView) view.findViewById(R.id.beer_info_style_value);
        this.description = (TextView) view.findViewById(R.id.beer_info_description_value);

        this.img = (ImageView) view.findViewById(R.id.beer_info_img);

        initializeFAB();
        initializeRecyclerView();
        loadText();

        switch(getResources().getConfiguration().orientation) {
            // Portrait
            case 1:
                break;

            // Landscape
            case 2:
                break;
        }

        return this.view;
    }

    @Override
    public void onAttach(Context context) {
        // Fix for getActivity returning null
        super.onAttach(context);
        BreweryDB.isEndpointAvailable(context);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getInstance().unregister(this);
    }

    private void loadText() {
        final String PLACEHOLDER = "N/A";

        if(beer.getBdb() == null) {
            Log.e("BEER", "BDB in activity is null");

        } else {
            BreweryDBBeer bdb = beer.getBdb();

            if(bdb.getSrm() == null) {
                Log.e("BEER", "SRM in activity is null");

            } else {
                this.srm.setText(bdb.getSrm().getName() == null ? PLACEHOLDER : bdb.getSrm().getName());
            }

            if(bdb.getBrewery() == null) {
                Log.e("BEER", "Brewery in activity is null");

            } else {
                this.brewery.setText(bdb.getBrewery().getName() == null ? PLACEHOLDER : bdb.getBrewery().getName());
            }

            if(bdb.getStyle() == null) {
                Log.e("BEER", "Style in activity is null");

            } else {
                this.style.setText(bdb.getStyle().getName() == null ? PLACEHOLDER : bdb.getStyle().getName());
            }

            if(bdb.getLabels() == null) {
                Log.e("BEER", "Labels in activity are null");
                Picasso.with(activity)
                        .load(R.drawable.beer_placeholder)
                        .resize(200, 267)
                        .centerCrop()
                        .into(img);

            } else {
                Picasso.with(activity)
                        .load(bdb.getLabels().getMedium())
                        .placeholder(R.drawable.beer_placeholder)
                        .resize(200, 267)
                        .centerCrop()
                        .into(img);
            }

            this.name.setText(bdb.getName() == null ? PLACEHOLDER : bdb.getName());
            this.abv.setText(bdb.getAbv() == null ? PLACEHOLDER : bdb.getAbv() + "%");
            this.ibu.setText(bdb.getIbu() == null ? PLACEHOLDER : bdb.getIbu());
            this.og.setText(bdb.getOg() == null ? PLACEHOLDER : bdb.getOg());
            this.description.setText(bdb.getDescription() == null ? PLACEHOLDER : bdb.getDescription());
        }
    }

    private void initializeFAB() {

        if(this.pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            this.cameraFAB = new FloatingActionButton(getContext());
            this.cameraFAB.setImageResource(R.drawable.camera);
            this.cameraFAB.setColorNormalResId(R.color.colorPrimary);
            this.cameraFAB.setColorPressedResId(R.color.colorPrimary);
            this.cameraFAB.setTitle("Nieuwe foto nemen");
            this.cameraFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Helper.isExternalStoragePresent()) {
                        ImageStore image = new ImageStore();
                        beer.addPicture(image.dispatchTakePictureIntent(activity), getActivity());
                        activity.setBeer(beer);
                        image.addToGallery(activity);

                    } else {
                        Toast.makeText(getContext(), "External storage not available.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        this.galleryFAB = new FloatingActionButton(getContext());
        this.galleryFAB.setImageResource(R.drawable.folder);
        this.galleryFAB.setColorNormalResId(R.color.colorPrimary);
        this.galleryFAB.setColorPressedResId(R.color.colorPrimary);
        this.galleryFAB.setTitle("Importeren uit galerij");
        this.galleryFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Gallery FAB", Toast.LENGTH_SHORT).show();
            }
        });

        this.menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.beer_info_fab_menu);
        this.menuMultipleActions.addButton(cameraFAB);
        this.menuMultipleActions.addButton(galleryFAB);
    }

    private void initializeRecyclerView() {
        this.recycler = (RecyclerView) view.findViewById(R.id.beer_info_photo_list);
        List<ImageStore> pictures;

        if(this.beer.exists()) {
            pictures = this.beer.getPicturesFromDB();
            Log.e("LOG", "Getting pictures from db, " + pictures.size() + " found.");

        } else {
            Log.e("LOG", "Beer not yet in database. Starting with empty picure list.");
            pictures = new ArrayList<>();
        }

        this.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.recycler.setAdapter(new BeerPictureAdapter(pictures, getActivity()));
    }

    @Subscribe
    public void onEndPointAvailableResult(EndPointAvailableEvent event) {
        if(event.isAvailable()) {
            Log.e("LOG", "Endpoint available!");
            new BeerLookupTask(getActivity(), beer).execute();

        } else {
            Log.e("LOG", "Endpoint not available.");
        }
    }

    @Subscribe
    public void onBeerLookupResult(BeerLookupTaskEvent event) {
        this.beer = event.getBeer();
        Log.e("LOG", "Lookup result!");
        loadText();
    }
}
