package be.dieterholvoet.beerguide.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import be.dieterholvoet.beerguide.NewBeerActivity;
import be.dieterholvoet.beerguide.R;
import be.dieterholvoet.beerguide.adapters.BeerPictureAdapter;
import be.dieterholvoet.beerguide.bus.BeerLookupTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.helper.FileHelper;
import be.dieterholvoet.beerguide.helper.Helper;
import be.dieterholvoet.beerguide.helper.ImageHelper;
import be.dieterholvoet.beerguide.helper.PermissionsHelper;
import be.dieterholvoet.beerguide.model.ImageStore;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import io.realm.Realm;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Dieter on 26/12/2015.
 */

public class NewBeerInfoFragment extends Fragment {
    private final int REQUEST_CAMERA_PERMISSION = 884;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_GALLERY_IMAGE = 750;

    private final String LOG = "NewBeerInfoFragment";

    NewBeerActivity activity;
    View view;
    Beer beer;
    ImageStore tempImage;
    Realm realm;

    FloatingActionsMenu FAB;
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
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
        realm = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getInstance().register(this);
        checkIfPicturesExist();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getInstance().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_new_beer_info, null);

        this.name = (TextView) view.findViewById(R.id.beer_info_name);
        this.abv = (TextView) view.findViewById(R.id.beer_info_abv_value);
        this.ibu = (TextView) view.findViewById(R.id.beer_info_ibu_value);
        this.srm = (TextView) view.findViewById(R.id.beer_info_srm_value);
        this.og = (TextView) view.findViewById(R.id.beer_info_og_value);
        this.brewery = (TextView) view.findViewById(R.id.beer_info_brewery_value);
        this.style = (TextView) view.findViewById(R.id.beer_info_style_value);
        this.description = (TextView) view.findViewById(R.id.beer_info_description_value);

        this.img = (ImageView) view.findViewById(R.id.beer_info_img);

        this.activity = (NewBeerActivity) getActivity();
        this.beer = activity.getBeer();

        initializeFAB();
        checkIfPicturesExist();
        initializeRecyclerView();
        loadData();

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

    private void checkIfPicturesExist() {
        for(int i = 0; i < beer.getPictures().size(); i++) {
            ImageStore picture = beer.getPictures().get(i);
            final int pos = i;

            if(!picture.getFile().exists()) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        beer.getPictures().remove(pos);
                    }
                });
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public void loadData() {
        final String PLACEHOLDER = "N/A";

        if(beer.getBdb() == null) {
            Log.e("BEER", "BDB in activity is null");

        } else {
            BreweryDBBeer bdb = beer.getBdb();

            if(bdb.getSrm() != null) {
                this.srm.setText(bdb.getSrm().getName() == null ? PLACEHOLDER : bdb.getSrm().getName());
            }

            if(bdb.getBrewery() != null) {
                this.brewery.setText(bdb.getBrewery().getName() == null ? PLACEHOLDER : bdb.getBrewery().getName());
            }

            if(bdb.getStyle() != null) {
                this.style.setText(bdb.getStyle().getName() == null ? PLACEHOLDER : bdb.getStyle().getName());
            }

            if(bdb.getLabels() == null) {
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

        if(ImageHelper.hasCamera(getContext())) {
            this.cameraFAB = new FloatingActionButton(getContext());
            this.cameraFAB.setImageResource(R.drawable.camera);
            this.cameraFAB.setColorNormalResId(R.color.colorPrimary);
            this.cameraFAB.setColorPressedResId(R.color.colorPrimary);
            this.cameraFAB.setTitle("Nieuwe foto nemen");
            this.cameraFAB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Helper.isExternalStoragePresent()) {
                        new Permissive.Request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .whenPermissionsGranted(new PermissionsGrantedListener() {
                                    @Override
                                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                        NewBeerInfoFragment.this.tempImage = ImageHelper.takePicture(getActivity(), REQUEST_IMAGE_CAPTURE, NewBeerInfoFragment.this);
                                    }
                                })
                                .whenPermissionsRefused(new PermissionsRefusedListener() {
                                    @Override
                                    public void onPermissionsRefused(String[] permissions) {
                                        NewBeerInfoFragment.this.FAB.collapse();
                                    }
                                })
                                .execute(getActivity());

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
                new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .whenPermissionsGranted(new PermissionsGrantedListener() {
                            @Override
                            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                ImageHelper.pickFromGallery(NewBeerInfoFragment.this, REQUEST_GALLERY_IMAGE);
                            }
                        })
                        .whenPermissionsRefused(new PermissionsRefusedListener() {
                            @Override
                            public void onPermissionsRefused(String[] permissions) {
                                NewBeerInfoFragment.this.FAB.collapse();
                            }
                        })
                        .execute(getActivity());
            }
        });

        this.FAB = (FloatingActionsMenu) view.findViewById(R.id.beer_info_fab_menu);
        this.FAB.addButton(cameraFAB);
        this.FAB.addButton(galleryFAB);
    }

    private void initializeRecyclerView() {
        this.recycler = (RecyclerView) view.findViewById(R.id.beer_info_photo_list);
        this.recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        this.recycler.setAdapter(new BeerPictureAdapter(this.beer.getPictures(), getActivity()));
    }

    @Subscribe
    public void onBeerLookupResult(BeerLookupTaskEvent event) {
        this.beer = event.getBeers().get(0);
        loadData();
    }

    public FloatingActionsMenu getFAB() {
        return FAB;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) {
            switch(requestCode) {

                case REQUEST_IMAGE_CAPTURE:
                    if(this.tempImage != null) {
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                beer.addPicture(tempImage);
                            }
                        });

                        ImageHelper.addToGallery(getActivity(), this.tempImage.getUri());
                        this.tempImage = null;
                        this.recycler.setAdapter(new BeerPictureAdapter(beer.getPictures(), getActivity()));
                        this.FAB.collapse();

                    } else {
                        Log.e(LOG, "tempImage is null");
                    }

                    break;

                case REQUEST_GALLERY_IMAGE:
                    String path = FileHelper.getPath(getContext(), data.getData());
                    String extension = path.substring(path.lastIndexOf(".") + 1);
                    this.tempImage = new ImageStore(extension);

                    try {
                        FileHelper.copyFile(new File(path), this.tempImage.getFile());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            beer.addPicture(NewBeerInfoFragment.this.tempImage);
                        }
                    });

                    ImageHelper.addToGallery(getActivity(), this.tempImage.getUri());
                    this.tempImage = null;
                    this.recycler.setAdapter(new BeerPictureAdapter(beer.getPictures(), getActivity()));
                    int length = beer.getPictures().size();
                    this.FAB.collapse();

                    break;
            }
        }
    }
}
