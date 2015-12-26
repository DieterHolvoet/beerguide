package be.dieterholvoet.beerapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import be.dieterholvoet.beerapp.fragments.BeersAllFragment;
import be.dieterholvoet.beerapp.fragments.BeersAppearanceFragment;
import be.dieterholvoet.beerapp.fragments.BeersAromaFragment;
import be.dieterholvoet.beerapp.fragments.NewBeerAppearanceFragment;

public class NewBeerActivity extends AppCompatActivity {
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beer);

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
        adapter.addFrag(new BeersAllFragment(), "Appearance");
        adapter.addFrag(new NewBeerAppearanceFragment(), "Aroma");
        adapter.addFrag(new BeersAromaFragment(), "Taste");
        adapter.addFrag(new BeersAromaFragment(), "Rating");
        viewPager.setAdapter(adapter);
    }
}
