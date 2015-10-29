package com.lefrantguillaume.animelist.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.adapter.ShowAdapter;
import com.lefrantguillaume.animelist.model.ShowItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;

public class ShowsActivity extends Activity implements MeteorCallback {

    private String subscriptionId;
    private ListView mListView;
    private ShowAdapter adapter;
    private HashMap<String, ShowItem> showList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shows);

        showList = new HashMap<>();
        mListView = (ListView) findViewById(R.id.listview);
        adapter = new ShowAdapter(getApplicationContext(), showList);

        mListView.setAdapter(adapter);


        MeteorSingleton.getInstance().setCallback(this);
        if (MeteorSingleton.getInstance().isLoggedIn()) {
            this.subscriptionId = MeteorSingleton.getInstance().subscribe("mylist");
            Log.w("SUBSCRIPTION", this.subscriptionId);
        } else {
            Log.e("SUBSCRIPTION", "Not LoggedIn !");
        }

    }


    @Override
    public void onConnect(boolean b) {
        Log.i("WS", "Connected: " + b);

    }

    @Override
    public void onDisconnect(int i, String s) {
        Log.e("WS", "Disconnected");
        Log.w("WS", i + "\n" + s);
    }

    @Override
    public void onDataAdded(String collection, String _id, String data) {
        if (collection != null) Log.i("ADDED", collection);
        if (_id != null) Log.i("ADDED", _id);
        if (data != null) Log.i("ADDED", data);

        try {
            JSONObject obj = new JSONObject(data);

            ShowItem showItem = new ShowItem(
                    _id,
                    obj.getString("name"),
                    obj.getString("status"),
                    obj.getInt("season"),
                    obj.getInt("episode"),
                    obj.getString("owner"),
                    obj.getString("pic"),
                    obj.getString("link"),
                    new Date(),
                    new Date()
            );

            Log.i("SHOW", showItem.toString());
            this.showList.put(_id, showItem);

            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDataChanged(String collection, String _id, String updatedData, String removedData) {
        if (collection != null) Log.i("CHANGED", collection);
        if (_id != null) Log.i("CHANGED", _id);
        if (updatedData != null) Log.i("CHANGED", updatedData);
        if (removedData != null) Log.i("CHANGED", removedData);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onDataRemoved(String collection, String _id) {
        if (collection != null) Log.i("REMOVED", collection);
        if (_id != null) Log.i("REMOVED", _id);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onException(Exception e) {
        Log.e("mException", e.getMessage());
    }
}
