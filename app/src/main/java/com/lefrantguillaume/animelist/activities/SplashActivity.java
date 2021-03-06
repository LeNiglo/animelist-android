package com.lefrantguillaume.animelist.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.controllers.NetController;

import java.util.concurrent.CancellationException;

public class SplashActivity extends Activity {

    public static final String APP_NAME = "com.lefrantguillaume.animelist";
    public static final String ROOT_URL = "https://animelist.lefrantguillaume.com";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences(SplashActivity.APP_NAME, MODE_PRIVATE);
        String username = sharedPreferences.getString(SplashActivity.APP_NAME + ".username", null);
        String token = sharedPreferences.getString(SplashActivity.APP_NAME + ".token", null);


        if (username != null && token != null) {
            ping();
        } else {
            clearAndLogin();
        }

    }

    private void ping() {
        FutureCallback<String> cb = new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null) {
                    e.printStackTrace();
                    // TODO Offline Mode here ?
                } else {
                    if (result != null && !result.equals("null")) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    } else {
                        clearAndLogin();
                    }
                }
            }
        };

        NetController.ping(this, cb);
    }

    private void clearAndLogin() {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.remove(SplashActivity.APP_NAME + ".token");
        ed.remove(SplashActivity.APP_NAME + ".tab");
        ed.apply();
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
    }

    @Override
    protected void onStop() {
        try {
            Ion.getDefault(this).cancelAll(this);
        } catch (CancellationException e) {
            ping();
        }
        super.onStop();
    }

}
