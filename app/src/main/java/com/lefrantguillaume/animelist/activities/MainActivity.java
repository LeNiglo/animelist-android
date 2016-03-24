package com.lefrantguillaume.animelist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.controllers.ShowsController;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private SharedPreferences sharedPreferences;
    private String authToken;
    private ShowsController showsController;
    private ListView showsList;
    private NavigationView navigationView;
    private ArrayAdapter<ShowModel> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.sharedPreferences = getSharedPreferences(SplashActivity.APP_NAME, MODE_PRIVATE);
        authToken = this.sharedPreferences.getString(SplashActivity.APP_NAME + ".token", null);
        if (authToken == null) {
            finish();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO ADD ITEM
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(headerView);

        TextView headerUsername = (TextView) headerView.findViewById(R.id.header_username);
        headerUsername.setText(this.sharedPreferences.getString(SplashActivity.APP_NAME + ".username", "username"));

        showsController = new ShowsController();
        showsList = (ListView) findViewById(R.id.shows_list);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, showsController.getActiveList());
        showsList.setAdapter(arrayAdapter);
        refreshData();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(SplashActivity.APP_NAME + ".showsParcel", (ArrayList<? extends Parcelable>) showsController.getAll());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            List<ShowModel> showsParcel = savedInstanceState.getParcelableArrayList(SplashActivity.APP_NAME + ".showsParcel");
            assert showsParcel != null;
            Log.i(MainActivity.class.getCanonicalName(), "RestoreInstance, shows: " + showsParcel.size());
            for (ShowModel m : showsParcel) {
                this.showsController.addDocument(m);
            }

            arrayAdapter.clear();
            arrayAdapter.addAll(showsController.getActiveList());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    protected void refreshData() {

        showsController.clear();
        Ion.with(this)
                .load("GET", SplashActivity.ROOT_URL + "/publications/myShows")
                .setHeader("Authorization", "Bearer " + authToken)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (e != null)
                            e.printStackTrace();

                        JsonArray shows = result.getAsJsonArray("shows");
                        Gson gSon = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();
                        for (int i = 0; i < shows.size(); i++) {
                            showsController.addDocument(gSon.fromJson(shows.get(i).getAsJsonObject(), ShowModel.class));
                        }

                        MenuItem menuItem = navigationView.getMenu().findItem(sharedPreferences.getInt(SplashActivity.APP_NAME + ".tab", R.id.nav_animes_running));
                        onNavigationItemSelected(menuItem);
                    }
                });
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

        switch (id) {
            case R.id.action_logout:
                logoutClient();
                break;
            case R.id.action_refresh:
                refreshData();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        resetMenuItems();

        int id = item.getItemId();
        showsController.setActive(id);
        item.setChecked(true);

        arrayAdapter.clear();
        arrayAdapter.addAll(showsController.getActiveList());
        arrayAdapter.notifyDataSetChanged();

        SharedPreferences.Editor ed = this.sharedPreferences.edit();
        ed.putInt(SplashActivity.APP_NAME + ".tab", id);
        ed.apply();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void logoutClient() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.remove(SplashActivity.APP_NAME + ".token");
        ed.remove(SplashActivity.APP_NAME + ".tab");
        ed.apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void resetMenuItems() {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            for (int j = 0; j < navigationView.getMenu().getItem(i).getSubMenu().size(); j++) {
                navigationView.getMenu().getItem(i).getSubMenu().getItem(j).setChecked(false);
            }
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Ion.getDefault(this).cancelAll(this);
    }

}
