package be.dieterholvoet.beerguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.List;

import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerguide.bus.BeerListTaskEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.bus.RecentBeerListTaskEvent;
import be.dieterholvoet.beerguide.fragments.BeersRecentFragment;
import be.dieterholvoet.beerguide.fragments.BeersFavoritesFragment;
import be.dieterholvoet.beerguide.fragments.BeersMoreFragment;
import be.dieterholvoet.beerguide.listeners.SearchSuggestionListener;
import be.dieterholvoet.beerguide.listeners.SearchTextListener;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import be.dieterholvoet.beerguide.adapters.SearchBeerResultsAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView searchView;
    SimpleCursorAdapter mSearchViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Subscribe to events
        EventBus.getInstance().register(this);

        // Initialize Retrofit Builder & search service
        BreweryDB.getInstance();

        // Initialize drawerLayout
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Initialize navigationView
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Initialize Toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        // SearchView
        MatrixCursor cursor = new MatrixCursor(BreweryDB.getInstance().getColumns());
        String[] columns = BreweryDB.getInstance().getColumns();

        searchView = (SearchView) findViewById(R.id.main_search);
        mSearchViewAdapter = new SearchBeerResultsAdapter(this, R.layout.item_search_suggestion, cursor, columns, null, -1000);
        searchView.setSuggestionsAdapter(mSearchViewAdapter);

        // Source: http://stackoverflow.com/a/24930574
        searchView.setOnClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewExpanded();
            }
        });

        // Source: http://stackoverflow.com/a/14248893
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if (!queryTextFocused) {
                    searchView.onActionViewCollapsed();
                    // searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchTextListener(searchView));
        searchView.setOnSuggestionListener(new SearchSuggestionListener(this, searchView));

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));

        // Initialize FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewBeerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        // Initialize viewPager
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        setupViewPager(viewPager);

        // Initialize tabLayout
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);

        // Workaround for setupWithViewPager
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        // Add onTabSelectedListener
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        Toast.makeText(MainActivity.this, "One", Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(MainActivity.this, "Two", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(MainActivity.this, "Three", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BeersRecentFragment(), "Recent");
        adapter.addFrag(new BeersFavoritesFragment(), "Favorites");
        adapter.addFrag(new BeersMoreFragment(), "More");
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        EventBus.getInstance().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_beers) {

        } else if (id == R.id.nav_achievements) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe
    public void onBeerListTaskResult(BeerListTaskEvent event) {
        List<Beer> beers = event.getBeers();
    }
}
