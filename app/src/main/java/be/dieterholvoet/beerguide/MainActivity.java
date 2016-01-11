package be.dieterholvoet.beerguide;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
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

import com.squareup.otto.Subscribe;

import java.util.List;

import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerguide.bus.BeerListTaskEvent;
import be.dieterholvoet.beerguide.bus.EndPointAvailableEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.fragments.BeersRecentFragment;
import be.dieterholvoet.beerguide.fragments.BeersFavoritesFragment;
import be.dieterholvoet.beerguide.fragments.BeersMoreFragment;
import be.dieterholvoet.beerguide.listeners.SearchSuggestionListener;
import be.dieterholvoet.beerguide.listeners.SearchTextListener;
import be.dieterholvoet.beerguide.model.Beer;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import be.dieterholvoet.beerguide.adapters.SearchBeerResultsAdapter;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private SimpleCursorAdapter mSearchViewAdapter;

    private boolean firstLoadDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Subscribe to events
        EventBus.getInstance().register(this);

        // Initialize Retrofit Builder & search service
        BreweryDB.getInstance();

        // Initialize stuff
        initializeFAB();
        initializeViewPager();
        initializeTabLayout();
        initializeDrawerLayout();
        initializeNavigationView();
        initializeToolbar();

        // Check for API availability
        BreweryDB.isEndpointAvailable(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getInstance().unregister(this);
        super.onDestroy();
    }

    private void initializeFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewBeerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private void initializeSearchView() {
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
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchTextListener(searchView));
        searchView.setOnSuggestionListener(new SearchSuggestionListener(this, searchView));

        EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.colorPrimary));
        searchEditText.setHintTextColor(getResources().getColor(R.color.colorPrimary));
    }

    private void initializeViewPager() {
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new BeersRecentFragment(), "Recent");
        adapter.addFrag(new BeersFavoritesFragment(), "Favorites");
        adapter.addFrag(new BeersMoreFragment(), "More");

        viewPager.setAdapter(adapter);
    }

    private void initializeTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);

        // Workaround for setupWithViewPager
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    private void initializeDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initializeNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_beers) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Subscribe
    public void onBeerListTaskResult(BeerListTaskEvent event) {
        List<Beer> beers = event.getBeers();
    }

    @Subscribe
    public void onEndPointAvailableResult(EndPointAvailableEvent event) {
        if(!firstLoadDone) {
            if(event.isAvailable()) {
                Log.e("MAIN", "Initializing searchview");
                initializeSearchView();

            } else {
                Log.e("MAIN", "Not initializing searchview");
                new AlertDialog.Builder(this)
                        .setTitle("Uh-oh!")
                        .setMessage("Can't connect to the server. Either there is no internet connection available, or the daily request limit of this app is reached. " +
                                "The app will be started in offline mode and search functionality will be disabled.")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setIcon(R.drawable.alert_circle)
                        .show();
            }

            firstLoadDone = true;
        }
    }
}
