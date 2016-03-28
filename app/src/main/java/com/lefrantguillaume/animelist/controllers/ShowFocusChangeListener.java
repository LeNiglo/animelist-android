package com.lefrantguillaume.animelist.controllers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.activities.MainActivity;
import com.lefrantguillaume.animelist.activities.SplashActivity;
import com.lefrantguillaume.animelist.models.ShowModel;

/**
 * Created by leniglo on 27/03/16.
 */
public class ShowFocusChangeListener implements View.OnFocusChangeListener {

    private Activity activity;
    private String parameter;
    private ShowModel item;
    private static final String TAG = ShowFocusChangeListener.class.getName();

    public ShowFocusChangeListener(Activity activity, ShowModel item, String parameter) {
        this.activity = activity;
        this.item = item;
        this.parameter = parameter;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            EditText editText = (EditText) v;

            String value = editText.getText().toString();
            if (!value.equals(item.getParameter(this.parameter))) {
                submit(editText.getInputType(), value);
            }
        }
    }

    private void submit(int type, String value) {
        FutureCallback cb = new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null)
                    e.printStackTrace();

                Log.i(TAG, result);
            }
        };

        if (type == InputType.TYPE_CLASS_NUMBER) {
            try {
                NetController.sendChangement(activity, item, parameter, (Integer) Integer.parseInt(value), cb);
            } catch (NumberFormatException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            NetController.sendChangement(activity, item, parameter, value, cb);
        }

    }

}
