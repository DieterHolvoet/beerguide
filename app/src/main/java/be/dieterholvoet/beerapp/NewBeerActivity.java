package be.dieterholvoet.beerapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import be.dieterholvoet.beerapp.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerapp.fragments.NewBeerAppearanceFragment;
import be.dieterholvoet.beerapp.fragments.NewBeerAromaFragment;
import be.dieterholvoet.beerapp.fragments.NewBeerRatingFragment;
import be.dieterholvoet.beerapp.fragments.NewBeerTasteFragment;
import be.dieterholvoet.beerapp.model.Beer;

public class NewBeerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Beer beer = new Beer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beer);

        // Get intent
        Bundle b = getIntent().getExtras();
        if(b != null) {
            beer.set_id(b.getInt("id"));
            beer.setName(b.getString("name"));
            setTitle(beer.getName());
        }

        // Initialize Toolbar
        toolbar = (Toolbar) findViewById(R.id.new_beer_toolbar);
        setSupportActionBar(toolbar);

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize viewPager
        viewPager = (ViewPager) findViewById(R.id.new_beer_viewpager);
        setupViewPager(viewPager);

        // Initialize tabLayout
        tabLayout = (TabLayout) findViewById(R.id.new_beer_tabs);

        // Workaround for setupWithViewPager
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new NewBeerAppearanceFragment(), "Appearance");
        adapter.addFrag(new NewBeerAromaFragment(), "Aroma");
        adapter.addFrag(new NewBeerTasteFragment(), "Taste");
        adapter.addFrag(new NewBeerRatingFragment(), "Rating");
        viewPager.setAdapter(adapter);
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }
}
