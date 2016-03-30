package com.lefrantguillaume.animelist.controllers;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.koushikdutta.async.future.FutureCallback;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.models.ShowModel;

/**
 * Created by leniglo on 27/03/16.
 */
public class ShowFocusChangeListener implements View.OnFocusChangeListener {

    private final Activity activity;
    private final String parameter;
    private final ShowModel item;
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

    private void submit(int type, final String value) {
        FutureCallback<String> cb = new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e != null) {
                    e.printStackTrace();
                } else {
                    try {
                        if (Integer.parseInt(result) > 0) {
                            item.setParameter(parameter, value);
                            ShowsController.getInstance().changeItem(item);
                        } else {
                            Snackbar.make(activity.findViewById(R.id.app_bar), "Error while updating item.", Snackbar.LENGTH_LONG).show();
                        }
                    } catch (NumberFormatException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        if (type == InputType.TYPE_CLASS_NUMBER) {
            try {
                NetController.sendChangement(activity, item, parameter, Integer.parseInt(value), cb);
            } catch (NumberFormatException e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            NetController.sendChangement(activity, item, parameter, value, cb);
        }

    }

}
