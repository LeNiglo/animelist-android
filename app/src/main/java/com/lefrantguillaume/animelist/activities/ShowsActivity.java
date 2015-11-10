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
        setContentView(R.layout.activity_shows);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        this.viewPager = (ViewPager) findViewById(R.id.viewpager);
        this.pagerAdapter = new ShowsFragmentPagerAdapter(getSupportFragmentManager(),
                ShowsActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        this.tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Meteor instance
        MeteorSingleton.getInstance().setCallback(this);
        if (MeteorSingleton.getInstance().isLoggedIn()) {
            this.subscriptionId = MeteorSingleton.getInstance().subscribe("mylist");
        } else {
            Log.e("ShowsActivity", "Not LoggedIn !");
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        MeteorSingleton.getInstance().unsetCallback(this);
        MeteorSingleton.getInstance().logout();
        if (pagerAdapter != null)
            pagerAdapter.clear();
    }

    @Override
    public void onConnect(boolean b) {

    }

    @Override
    public void onDisconnect(int i, String s) {

    }

    @Override
    public void onDataAdded(String collection, String _id, String data) {
        if (collection != null) Log.i("ADDED", collection);

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

            Log.i("SHOW", showItem.toString());

            assert collection != null;
            switch (collection) {
                case "animes":
                    ((ShowFragment) this.pagerAdapter.getItem(0)).addToDataset(_id, showItem);
                    break;
                case "series":
                    ((ShowFragment) this.pagerAdapter.getItem(1)).addToDataset(_id, showItem);
                    break;
                default:
                    throw new Exception("collection is invalid: " + collection, new Throwable());
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

        try {
            assert collection != null;
            switch (collection) {
                case "animes":
                    ((ShowFragment) this.pagerAdapter.getItem(0)).updateDataset(_id);
                    break;
                case "series":
                    ((ShowFragment) this.pagerAdapter.getItem(1)).updateDataset(_id);
                    break;
                default:
                    throw new Exception("collection is invalid: " + collection, new Throwable());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDataRemoved(String collection, String _id) {
        if (collection != null) Log.i("REMOVED", collection);
        if (_id != null) Log.i("REMOVED", _id);

        try {
            assert collection != null;
            switch (collection) {
                case "animes":
                    ((ShowFragment) this.pagerAdapter.getItem(0)).removeFromDataset(_id);
                    break;
                case "series":
                    ((ShowFragment) this.pagerAdapter.getItem(1)).removeFromDataset(_id);
                    break;
                default:
                    throw new Exception("collection is invalid: " + collection, new Throwable());
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
                // Comportement du bouton "Refresh"
                MeteorSingleton.getInstance().unsubscribe(subscriptionId, new UnsubscribeListener() {
                    @Override
                    public void onSuccess() {
                        pagerAdapter.clear();
                        subscriptionId = MeteorSingleton.getInstance().subscribe("mylist");
                    }
                });
                return true;
            case R.id.action_sign_out:
                MeteorSingleton.getInstance().logout();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
