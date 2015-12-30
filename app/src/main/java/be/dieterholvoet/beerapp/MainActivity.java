package be.dieterholvoet.beerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import be.dieterholvoet.beerapp.fragments.BeersRecentFragment;
import be.dieterholvoet.beerapp.fragments.BeersFavoritesFragment;
import be.dieterholvoet.beerapp.fragments.BeersMoreFragment;
import be.dieterholvoet.beerapp.rest.BreweryDBResponse;
import be.dieterholvoet.beerapp.rest.BreweryDBResult;
import be.dieterholvoet.beerapp.rest.BreweryDBSuggestionsService;
import be.dieterholvoet.beerapp.rest.SearchBeerResultsAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView searchView;
    SimpleCursorAdapter mSearchViewAdapter;

    private String[] columns = new String[]{"_id", "BEER_NAME"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        searchView = (SearchView) findViewById(R.id.main_search);
        mSearchViewAdapter = new SearchBeerResultsAdapter(this, R.layout.search_suggestion_item, null, columns, null, -1000);
        searchView.setSuggestionsAdapter(mSearchViewAdapter);

        // Source: http://stackoverflow.com/a/24930574
        searchView.setOnClickListener(new SearchView.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.main_search:
                        searchView.onActionViewExpanded();
                        break;
                }
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) {
                    loadData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    loadData(newText);
                }
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                String beerName = cursor.getString(1);
                searchView.setQuery(beerName, false);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                onSuggestionSelect(position);
                return true;
            }
        });

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

    private void loadData(String searchText) {
        final String API_KEY = "63d5648e9125519e5f284d89a1e50f3e";

        // Specify endpoint and build the restadapter instance
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl("http://api.brewerydb.com/v2")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Now use restadapter to create an instance of your interface
        BreweryDBSuggestionsService searchService = restAdapter.create(BreweryDBSuggestionsService.class);

        // Populate the request parameters
        HashMap queryMap = new HashMap();
        queryMap.put("key", API_KEY);
        queryMap.put("type", "beer");
        queryMap.put("q", searchText);

        //implement the Callback<T> interface for retrieving the response
        /*searchService.searchBeers(queryMap, new Callback<BreweryDBResponse>() {
            @Override
            public void onResponse(Response<BreweryDBResponse> response, Retrofit retrofit) {
                MatrixCursor matrixCursor = convertToCursor(response.body().getData());
                mSearchViewAdapter.changeCursor(matrixCursor);
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    throw t;

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });*/

        Call<BreweryDBResponse> call = searchService.searchBeers(queryMap);
        call.enqueue(new Callback<BreweryDBResponse>() {
            @Override
            public void onResponse(Response<BreweryDBResponse> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    if(response.body().getData() == null) {
                        Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();

                    } else {
                        MatrixCursor matrixCursor = convertToCursor(response.body().getData());
                        mSearchViewAdapter.changeCursor(matrixCursor);
                    }


                } else {
                    Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_LONG).show();

                    try {
                        Toast.makeText(MainActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                try {
                    throw t;

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    private MatrixCursor convertToCursor(List<BreweryDBResult> BreweryDBResults) {
        MatrixCursor cursor = new MatrixCursor(columns);

        if(BreweryDBResults != null) {
            for(int i = 0; i < BreweryDBResults.size(); i++) {
                BreweryDBResult BreweryDBResult = BreweryDBResults.get(i);
                String[] row = new String[2];
                row[0] = Integer.toString(i);
                row[1] = BreweryDBResult.getName();
                cursor.addRow(row);
            }
        }

        return cursor;
    }
}
