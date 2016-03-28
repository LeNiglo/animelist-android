package com.lefrantguillaume.animelist.controllers;

import android.app.Activity;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.activities.SplashActivity;
import com.lefrantguillaume.animelist.models.ShowModel;

/**
 * Created by leniglo on 28/03/16.
 */
public class NetController {

    private static final String TAG = NetController.class.getName();

    public static void sendChangement(Activity activity, ShowModel item, String parameter, Object value, FutureCallback<String> cb) throws RuntimeException {
        String token = activity.getSharedPreferences(SplashActivity.APP_NAME, Activity.MODE_PRIVATE).getString(SplashActivity.APP_NAME + ".token", null);

        if (token == null)
            throw new RuntimeException("Unable to find Authentification token.");

        JsonObject json = new JsonObject();
        if (value instanceof Integer) {
            json.addProperty(parameter, (Integer) value);
        } else if (value instanceof String) {
            json.addProperty(parameter, (String) value);
        } else {
            throw new RuntimeException("Invalid argument. Expecting Integer or String.");
        }

        Log.i(TAG, "Sending show changement " + parameter + "=" + value.toString());
        new Exception().printStackTrace();
        Ion.with(activity)
                .load("PATCH", SplashActivity.ROOT_URL + "/shows/" + item.getId())
                .setHeader("Authorization", "Bearer " + token)
                .setJsonObjectBody(json)
                .asString()
                .setCallback(cb);
    }

    public static void ping(Activity activity, FutureCallback<String> cb) throws RuntimeException {
        String token = activity.getSharedPreferences(SplashActivity.APP_NAME, Activity.MODE_PRIVATE).getString(SplashActivity.APP_NAME + ".token", null);

        if (token == null)
            throw new RuntimeException("Unable to find Authentification token.");

        Log.i(TAG, "Executing ping to test Auth");
        Ion.with(activity)
                .load("POST", SplashActivity.ROOT_URL + "/methods/ping")
                .setHeader("Authorization", "Bearer " + token)
                .asString()
                .setCallback(cb);
    }

    public static void loadShows(Activity activity, FutureCallback<JsonObject> cb) throws RuntimeException {
        String token = activity.getSharedPreferences(SplashActivity.APP_NAME, Activity.MODE_PRIVATE).getString(SplashActivity.APP_NAME + ".token", null);

        if (token == null)
            throw new RuntimeException("Unable to find Authentification token.");

        Log.i(TAG, "Loading shows list.");
        Ion.with(activity)
                .load("GET", SplashActivity.ROOT_URL + "/publications/myShows")
                .setHeader("Authorization", "Bearer " + token)
                .asJsonObject()
                .setCallback(cb);
    }

    public static void login(Activity activity, JsonObject authBody, FutureCallback<JsonObject> cb) throws RuntimeException {
        Log.i(TAG, "Executing login");
        Ion.with(activity)
                .load("POST", SplashActivity.ROOT_URL + "/users/login")
                .setJsonObjectBody(authBody)
                .asJsonObject()
                .setCallback(cb);
    }
}
