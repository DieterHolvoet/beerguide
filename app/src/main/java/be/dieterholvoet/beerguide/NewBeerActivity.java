package be.dieterholvoet.beerguide;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import be.dieterholvoet.beerguide.adapters.BeerPictureAdapter;
import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerguide.bus.BeerLookupTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.fragments.NewBeerAppearanceFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerInfoFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerRatingFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerTasteFragment;
import be.dieterholvoet.beerguide.helper.ImageStore;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import be.dieterholvoet.beerguide.tasks.BeerLookupTask;
import io.realm.Realm;

public class NewBeerActivity extends AppCompatActivity {
    private final String SAVEDINSTANCESTATE_KEY = "currentBeer";
    private final String LOG = "NewBeerActivity";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private Beer beer = new Beer();
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

        // setContentView
        setContentView(R.layout.activity_new_beer);

        // Get Realm
        realm = Realm.getDefaultInstance();

        // Initialize stuff
        initializeToolbar();
        initializeViewPager();
        initializeTabLayout();

        // Get data
        String bdbid = null;

        /*
        Get data from savedInstanceState
        Case:
            - Activity was terminated by system (screen rotation, app in background, ...)
        */
        if(savedInstanceState != null) {
            if(savedInstanceState.getString("bdbID") != null) {
                Log.d(LOG, "Getting data from savedInstanceState");
                bdbid = savedInstanceState.getString("bdbID");
            }
        }

        /*
        Get data from intent
        Case:
            - Tapped on saved beer in MainActivity
        */
        final Bundle b = getIntent().getExtras();
        if(b != null) {
            Log.d(LOG, "Getting data from intent bundle");
            setTitle(b.getString("beerName"));
            bdbid = b.getString("bdbID");
        }

        if(bdbid == null) {
            Log.e("BEER", "Haven't received a BreweryDBID.");

        } else {
            if(BeerDAO.getByBreweryDBID(realm, bdbid) == null) {
                Log.e("BEER", "Setting up a temporary, unmanaged Beer object with the correct BreweryDBID.");
                beer = new Beer();
                beer.setBdb(new BreweryDBBeer(bdbid));

                new BeerLookupTask(this, beer).execute();

            } else {
                Log.e("BEER", "Getting beer object from database.");
                beer = BeerDAO.getByBreweryDBID(realm, bdbid);
                EventBus.getInstance().post(new BeerLookupTaskEvent(new ArrayList<Beer>(Arrays.asList(beer))));
            }
        }

        // Test data
        // beer.log();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));

        // This makes the new screen slide up as it fades in
        // while the current screen slides up as it fades out.
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("bdbID", beer.getBdb().getBreweryDBID());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageStore.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            BeerDAO.savePictures(realm, beer);

            ((RecyclerView) findViewById(R.id.beer_info_photo_list)).setAdapter(new BeerPictureAdapter(beer.getPictures(), this));
        }
    }

    private void initializeViewPager() {
        this.viewPager = (ViewPager) findViewById(R.id.new_beer_viewpager);
        this.adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new NewBeerInfoFragment(), getResources().getString(R.string.tab_info));
        adapter.addFrag(new NewBeerAppearanceFragment(), getResources().getString(R.string.tab_appearance));
        adapter.addFrag(new NewBeerTasteFragment(), getResources().getString(R.string.tab_taste));
        adapter.addFrag(new NewBeerRatingFragment(), getResources().getString(R.string.tab_rating));

        viewPager.setAdapter(adapter);
    }

    private void initializeTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.new_beer_tabs);

        // Workaround for setupWithViewPager
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.new_beer_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
