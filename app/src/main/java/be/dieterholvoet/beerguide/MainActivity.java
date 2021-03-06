package be.dieterholvoet.beerguide;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import be.dieterholvoet.beerguide.adapters.ViewPagerAdapter;
import be.dieterholvoet.beerguide.bus.BeerLookupTaskEvent;
import be.dieterholvoet.beerguide.bus.EndPointAvailableEvent;
import be.dieterholvoet.beerguide.bus.EventBus;
import be.dieterholvoet.beerguide.fragments.BeersFavoritesFragment;
import be.dieterholvoet.beerguide.fragments.BeersMoreFragment;
import be.dieterholvoet.beerguide.fragments.BeersRecyclerFragment;
import be.dieterholvoet.beerguide.helper.BeersRecyclerFragmentType;
import be.dieterholvoet.beerguide.helper.PermissionsHelper;
import be.dieterholvoet.beerguide.listeners.SearchSuggestionListener;
import be.dieterholvoet.beerguide.listeners.SearchTextListener;
import be.dieterholvoet.beerguide.rest.BreweryDB;
import be.dieterholvoet.beerguide.adapters.SearchBeerResultsAdapter;
import be.dieterholvoet.beerguide.tasks.BeerLookupTask;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SearchView searchView;
    private SimpleCursorAdapter mSearchViewAdapter;
    Snackbar snackbar;

    private Realm realm;
    private boolean firstLoadDone = false;

    private static final int PERMISSIONS_REQUEST_INTERNET = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request permissions
        PermissionsHelper.requestPermission(this, Manifest.permission.INTERNET, PERMISSIONS_REQUEST_INTERNET);

        // Subscribe to events
        EventBus.getInstance().register(this);

        // Initialize Retrofit Builder & search service
        BreweryDB.getInstance();

        // Initialize stuff
        initializeViewPager();
        initializeTabLayout();
        initializeDrawerLayout();
        initializeNavigationView();
        initializeFAB();
        initializeToolbar();
        initializeSnackbar();

        // Check for API availability
        BreweryDB.isEndpointAvailable(this);

        // Get Realm
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        EventBus.getInstance().unregister(this);
        realm.close();
        super.onDestroy();
    }

    private void initializeSnackbar() {
        snackbar = Snackbar.make(getRootView(), getResources().getString(R.string.dialog_loading_beers), Snackbar.LENGTH_INDEFINITE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void initializeViewPager() {
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        BeersRecyclerFragment fragmentAll = new BeersRecyclerFragment();
        Bundle fragmentAllArgs = new Bundle();
        fragmentAllArgs.putSerializable("type", BeersRecyclerFragmentType.ALL);
        fragmentAll.setArguments(fragmentAllArgs);

        BeersRecyclerFragment fragmentRecent = new BeersRecyclerFragment();
        Bundle fragmentRecentArgs = new Bundle();
        fragmentRecentArgs.putSerializable("type", BeersRecyclerFragmentType.ALL);
        fragmentRecent.setArguments(fragmentRecentArgs);

        adapter.addFrag(fragmentAll, getResources().getString(R.string.tab_all));
        adapter.addFrag(fragmentRecent, getResources().getString(R.string.tab_recent));
        adapter.addFrag(new BeersFavoritesFragment(), getResources().getString(R.string.tab_favorites));
        adapter.addFrag(new BeersMoreFragment(), getResources().getString(R.string.tab_more));

        viewPager.setOffscreenPageLimit(3);
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

    private void initializeToolbar() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            snackbar.show();
            new BeerLookupTask(this).execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                        .setMessage(getResources().getString(R.string.dialog_connection_failed))
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

    @Subscribe
    public void onBeerLookupResult(BeerLookupTaskEvent event) {
        snackbar.dismiss();
    }

    private View getRootView() {
        return findViewById(android.R.id.content);
    }

    /*
        NAVIGATION DRAWER & FAB - UNUSED FOR NOW
     */

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    private void initializeDrawerLayout() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

    private void initializeFAB() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewBeerActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
        fab.setVisibility(View.INVISIBLE);
    }
}
