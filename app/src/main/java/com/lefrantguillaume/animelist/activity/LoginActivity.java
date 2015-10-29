package com.lefrantguillaume.animelist.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lefrantguillaume.animelist.R;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;


/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends Activity implements MeteorCallback {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        MeteorSingleton.createInstance(this, "ws://animelist.lefrantguillaume.com/websocket");
        MeteorSingleton.getInstance().setCallback(this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            MeteorSingleton.getInstance().loginWithUsername(username, password, new ResultListener() {
                @Override
                public void onSuccess(String res) {
                    Log.i("LOGIN", res);
                    Log.i("USERID", MeteorSingleton.getInstance().getUserId());

                    if (MeteorSingleton.getInstance().isLoggedIn()) {
                        Intent intent = new Intent(getApplicationContext(), ShowsActivity.class);
                        intent.putExtra("com.lefrantguillaume.animelist.LOGIN", res);
                        MeteorSingleton.getInstance().unsetCallback(LoginActivity.this);
                        startActivity(intent);
                    }
                }

                @Override
                public void onError(String code, String err, String res) {
                    Log.e("LOGIN", code);
                    Log.e("LOGIN", err);
                    mUsernameView.setError(code + ": " + err);
                    if (res != null) Log.w("LOGIN", res);
                }
            });
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
    public void onDataAdded(String s, String s1, String s2) {

    }

    @Override
    public void onDataChanged(String s, String s1, String s2, String s3) {

    }

    @Override
    public void onDataRemoved(String s, String s1) {

    }

    @Override
    public void onException(Exception e) {
        Log.e("mException", e.getMessage());
    }
}

