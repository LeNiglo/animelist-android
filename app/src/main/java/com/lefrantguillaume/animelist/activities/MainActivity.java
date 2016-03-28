package com.lefrantguillaume.animelist.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.controllers.ShowsAdapter;
import com.lefrantguillaume.animelist.controllers.NetController;
import com.lefrantguillaume.animelist.controllers.ShowsController;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getName();

    private SharedPreferences sharedPreferences;
    private NavigationView navigationView;
    private ShowsAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.sharedPreferences = getSharedPreferences(SplashActivity.APP_NAME, MODE_PRIVATE);
        if (this.sharedPreferences.getString(SplashActivity.APP_NAME + ".token", null) == null) {
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(headerView);

        TextView headerUsername = (TextView) headerView.findViewById(R.id.header_username);
        headerUsername.setText(this.sharedPreferences.getString(SplashActivity.APP_NAME + ".username", "username"));

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.shows_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ShowsAdapter(this, ShowsController.getInstance().getActiveList());
        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState == null) {
            refreshData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelableArrayList(SplashActivity.APP_NAME + ".showsParcel", (ArrayList<? extends Parcelable>) ShowsController.getInstance().getAll());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            List<ShowModel> showsParcel = savedInstanceState.getParcelableArrayList(SplashActivity.APP_NAME + ".showsParcel");
            if (showsParcel != null) {
                ShowsController.getInstance().clear();
                Log.i(TAG, "RestoreInstance, shows: " + showsParcel.size());
                for (ShowModel m : showsParcel) {
                    ShowsController.getInstance().addDocument(m);
                }

                mAdapter.clear();
                mAdapter.setmDataset(ShowsController.getInstance().getActiveList());
                mAdapter.notifyDataSetChanged();

                MenuItem menuItem = navigationView.getMenu().findItem(sharedPreferences.getInt(SplashActivity.APP_NAME + ".tab", R.id.nav_animes_running));
                onNavigationItemSelected(menuItem);
            }
        }
    }

    public void refreshData() {

        ShowsController.getInstance().clear();
        FutureCallback<JsonObject> cb = new FutureCallback<JsonObject>() {
            @Override
            public void onCompleted(Exception e, JsonObject result) {

                if (e != null)
                    e.printStackTrace();

                JsonArray shows = result.getAsJsonArray("shows");
                Gson gSon = new GsonBuilder().setDateFormat("yyyyMMddHHmmss").create();
                for (int i = 0; i < shows.size(); i++) {
                    ShowsController.getInstance().addDocument(gSon.fromJson(shows.get(i).getAsJsonObject(), ShowModel.class));
                }

                MenuItem menuItem = navigationView.getMenu().findItem(sharedPreferences.getInt(SplashActivity.APP_NAME + ".tab", R.id.nav_animes_running));
                onNavigationItemSelected(menuItem);
            }
        };
        NetController.loadShows(this, cb);
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

        if (item == null)
            return false;

        int id = item.getItemId();
        ShowsController.getInstance().setActive(id);
        item.setChecked(true);

        mAdapter.clear();
        mAdapter.setmDataset(ShowsController.getInstance().getActiveList());
        mAdapter.notifyDataSetChanged();

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

    public ShowsAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void onStop() {
        try {
            Ion.getDefault(this).cancelAll(this);
        } catch (CancellationException e) {
            e.printStackTrace();
        }
        super.onStop();
    }

}
