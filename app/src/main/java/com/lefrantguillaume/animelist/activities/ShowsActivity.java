package com.lefrantguillaume.animelist.activities;

import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.adapters.ShowsFragmentPagerAdapter;
import com.lefrantguillaume.animelist.fragments.ShowFragment;
import com.lefrantguillaume.animelist.models.ShowItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Date;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.UnsubscribeListener;

public class ShowsActivity extends AppCompatActivity implements MeteorCallback, ShowFragment.OnFragmentInteractionListener {

    private String subscriptionId;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShowsFragmentPagerAdapter pagerAdapter;

    public static String POSITION = "POSITION";
    private static String[] allowedCollections = {"animes", "series"};

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CYCLE2", "create");
        setContentView(R.layout.activity_shows);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("CYCLE2", "start");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        pagerAdapter = new ShowsFragmentPagerAdapter(getSupportFragmentManager(), ShowsActivity.this);
        viewPager.setAdapter(pagerAdapter);
        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);

        // Meteor instance
        MeteorSingleton.getInstance().setCallback(this);
        if (MeteorSingleton.getInstance().isLoggedIn()) {
            this.subscriptionId = MeteorSingleton.getInstance().subscribe("mylist");
        } else {
            Log.e("ShowsActivity", "Not LoggedIn !");
            MeteorSingleton.getInstance().unsetCallback(this);
            finishActivity(1);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("CYCLE2", "restart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CYCLE2", "restart");
    }

    @Override
    protected void onStop() {
        Log.e("CYCLE2", "stop");
        MeteorSingleton.getInstance().unsetCallback(this);
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.e("CYCLE2", "pause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("CYCLE2", "destroy");
        new Thread(new Runnable() {
            @Override
            public void run() {
                MeteorSingleton.getInstance().logout();
                if (pagerAdapter != null)
                    pagerAdapter.clear();
            }
        }).run();
        super.onDestroy();
    }

    @Override
    public void onConnect(boolean b) {

    }

    @Override
    public void onDisconnect(int i, String s) {

    }

    @Override
    public void onDataAdded(String collection, String _id, String data) {
        //if (collection != null) Log.i("ADDED", collection);
        if (!Arrays.asList(allowedCollections).contains(collection))
            return;

        try {
            JSONObject obj = new JSONObject(data);

            String link = obj.has("link") ? obj.getString("link") : null;
            String pic = obj.has("pic") ? (obj.getString("pic").startsWith("http") ? obj.getString("pic") : "http://animelist.lefrantguillaume.com" + obj.getString("pic")) : null;

            ShowItem showItem = new ShowItem(
                    _id,
                    obj.getString("name"),
                    obj.getString("status"),
                    obj.getInt("season"),
                    obj.getInt("episode"),
                    obj.getString("owner"),
                    pic,
                    link,
                    new Date(),
                    new Date()
            );

            assert collection != null;
            switch (collection) {
                case "animes":
                    ((ShowFragment) this.pagerAdapter.getItem(0)).addToDataset(_id, showItem);
                    break;
                case "series":
                    ((ShowFragment) this.pagerAdapter.getItem(1)).addToDataset(_id, showItem);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDataChanged(String collection, String _id, String updatedData, String removedData) {
        if (collection != null) Log.i("CHANGED", collection);
        if (_id != null) Log.i("CHANGED", _id);
        if (updatedData != null) Log.i("CHANGED", updatedData);
        if (removedData != null) Log.i("CHANGED", removedData);
        if (!Arrays.asList(allowedCollections).contains(collection))
            return;

        try {
            assert collection != null;
            switch (collection) {
                case "animes":
                    ((ShowFragment) this.pagerAdapter.getItem(0)).updateDataset(_id);
                    break;
                case "series":
                    ((ShowFragment) this.pagerAdapter.getItem(1)).updateDataset(_id);
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRemoved(String collection, String _id) {
        if (collection != null) Log.i("REMOVED", collection);
        if (_id != null) Log.i("REMOVED", _id);
        if (!Arrays.asList(allowedCollections).contains(collection))
            return;

        try {
            assert collection != null;
            switch (collection) {
                case "animes":
                    ((ShowFragment) this.pagerAdapter.getItem(0)).removeFromDataset(_id);
                    break;
                case "series":
                    ((ShowFragment) this.pagerAdapter.getItem(1)).removeFromDataset(_id);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onException(Exception e) {
        Log.e("mException", e.getMessage());
        Snackbar.make(tabLayout, e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MeteorSingleton.getInstance().reconnect();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                MeteorSingleton.getInstance().unsubscribe(subscriptionId, new UnsubscribeListener() {
                    @Override
                    public void onSuccess() {
                        pagerAdapter.clear();
                        MeteorSingleton.getInstance().setCallback(ShowsActivity.this);
                        subscriptionId = MeteorSingleton.getInstance().subscribe("mylist");
                    }
                });
                return true;
            case R.id.action_sign_out:
                MeteorSingleton.getInstance().logout();
                pagerAdapter.clear();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
