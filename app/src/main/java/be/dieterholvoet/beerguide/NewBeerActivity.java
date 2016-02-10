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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.util.List;

import be.dieterholvoet.beerguide.adapters.BeerPictureAdapter;
import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.fragments.NewBeerAppearanceFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerInfoFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerRatingFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerTasteFragment;
import be.dieterholvoet.beerguide.helper.ImageStore;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.model.BreweryDBBeer;

public class NewBeerActivity extends AppCompatActivity {
    private final String SAVEDINSTANCESTATE_KEY = "currentBeer";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Beer beer = new Beer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setContentView
        setContentView(R.layout.activity_new_beer);

        // Get data from savedInstanceState
        if(savedInstanceState != null) {
            beer = (Beer) savedInstanceState.getSerializable(SAVEDINSTANCESTATE_KEY);
        }

        // Get data from intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            BreweryDBBeer bdb = new BreweryDBBeer();
            bdb.setBreweryDBID(b.getString("bdbID"));
            bdb.setName(b.getString("beerName"));
            beer.setBdb(bdb);

            setTitle(b.getString("beerName"));
        }

        // Get data from database
        Beer dbBeer = BeerDAO.getByBreweryDBID(beer.getBdb().getBreweryDBID());

        if(dbBeer == null) {
            Log.e("BEER", "Beer not yet in database");

        } else {
            beer = dbBeer;
        }

        // Test data
        Log.e("BEER", "Name: " + beer.getBdb().getName());
        Log.e("BEER", "ID: " + beer.getBdb().getBreweryDBID());

        if(beer.getRating() == null) {
            Log.e("BEER", "Rating is null");
        } else {
            Log.e("BEER", "Rating: " + beer.getRating().getRating());
        }

        if(beer.getBdb().getStyle() == null) {
            Log.e("BEER", "Style (and category) is null");

        } else {
            Log.e("BEER", "Category: " + beer.getBdb().getStyle().getCategory().getName());
        }

        // Initialize stuff
        initializeToolbar();
        initializeViewPager();
        initializeTabLayout();
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
        outState.putSerializable(SAVEDINSTANCESTATE_KEY, beer);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageStore.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(beer.exists()) {
                beer.setPicturesFromDB();
            }

            ((RecyclerView) findViewById(R.id.beer_info_photo_list)).setAdapter(new BeerPictureAdapter(beer.getPictures(), this));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    private void initializeViewPager() {
        viewPager = (ViewPager) findViewById(R.id.new_beer_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
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
