package be.dieterholvoet.beerapp;

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

import be.dieterholvoet.beerapp.fragments.BeersRecentFragment;
import be.dieterholvoet.beerapp.fragments.BeersFavoritesFragment;
import be.dieterholvoet.beerapp.fragments.BeersMoreFragment;
import be.dieterholvoet.beerapp.rest.BreweryDB;
import be.dieterholvoet.beerapp.rest.SearchBeerResultsAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    SearchView searchView;
    SimpleCursorAdapter mSearchViewAdapter;
    BreweryDB dao;

    private void loadSearchData(String query) {
        MatrixCursor cursor = new MatrixCursor(dao.getColumns());
        mSearchViewAdapter.changeCursor(cursor);
        new SearchSuggestionThread(searchView, dao, query).execute();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Retrofit Builder & search service
        dao = new BreweryDB();

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
        mSearchViewAdapter = new SearchBeerResultsAdapter(this, R.layout.search_suggestion_item, null, dao.getColumns(), null, -1000);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.isEmpty()) {
                    loadSearchData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    loadSearchData(newText);
                }
                return true;
            }
        });

        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {

            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
                Intent intent = new Intent(MainActivity.this, NewBeerActivity.class);
                Bundle b = new Bundle();

                b.putString("name", cursor.getString(1));
                b.putString("id", cursor.getString(2));

                intent.putExtras(b);
                startActivity(intent);
                finish();
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
}
