package be.dieterholvoet.beerguide;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
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
        setContentView(R.layout.activity_new_beer);

        // Get data from savedInstanceState
        if(savedInstanceState != null) {
            beer = (Beer) savedInstanceState.getSerializable(SAVEDINSTANCESTATE_KEY);
        }

        // Get data from intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            beer = (Beer) b.getSerializable("currentBeer");
            setTitle(beer.getBdb().getName());
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SAVEDINSTANCESTATE_KEY, beer);
    }

    private void initializeViewPager() {
        viewPager = (ViewPager) findViewById(R.id.new_beer_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NewBeerInfoFragment(), "Info");
        adapter.addFrag(new NewBeerAppearanceFragment(), "Appearance");
        adapter.addFrag(new NewBeerTasteFragment(), "Taste");
        adapter.addFrag(new NewBeerRatingFragment(), "Rating");

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
