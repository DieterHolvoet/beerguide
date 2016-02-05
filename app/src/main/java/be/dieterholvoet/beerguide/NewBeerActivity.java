package be.dieterholvoet.beerguide;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerguide.db.BeerDAO;
import be.dieterholvoet.beerguide.fragments.NewBeerAppearanceFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerInfoFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerRatingFragment;
import be.dieterholvoet.beerguide.fragments.NewBeerTasteFragment;
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
