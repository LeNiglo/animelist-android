package com.lefrantguillaume.animelist.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefrantguillaume.animelist.R;

import java.util.Set;

import im.delight.android.ddp.MeteorCallback;
import im.delight.android.ddp.MeteorSingleton;
import im.delight.android.ddp.ResultListener;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements MeteorCallback {

    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;
    private ImageView mLogo;
    private CheckBox mRememberMe;
    private View mProgressView;
    private View mLoginFormView;
    private boolean authTask;
    private SharedPreferences mPrefs;

    public static final String ROOT_URL = "animelist.lefrantguillaume.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("CYCLE1", "create");
        setContentView(R.layout.activity_login);

        mPrefs = getSharedPreferences(getString(R.string.preferences_file_key), MODE_PRIVATE);

        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.username);
        mRememberMe = (CheckBox) findViewById(R.id.remember_me);
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

        Button mUsernameSignInButton = (Button) findViewById(R.id.username_sign_in_button);
        mUsernameSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLogo = (ImageView) findViewById(R.id.logo);
        mLogo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + ROOT_URL)));
            }
        });
        mProgressView = findViewById(R.id.login_progress);
        mLoginFormView = findViewById(R.id.login_form);
        authTask = false;

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("CYCLE1", "start");
        showProgress(authTask = false);

        // Meteor instance
        if (!MeteorSingleton.hasInstance()) {
            MeteorSingleton.createInstance(this, "ws://" + ROOT_URL + "/websocket");
        }
        MeteorSingleton.getInstance().setCallback(this);

        String prefUsername = mPrefs.getString(getString(R.string.preferences_file_key) + ".username", null);
        String prefPassword = mPrefs.getString(getString(R.string.preferences_file_key) + ".password", null);

        if (prefUsername != null) {
            mUsernameView.setText(prefUsername);
            if (prefPassword != null) {
                attemptLogin(prefUsername, prefPassword);
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("CYCLE1", "restart");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("CYCLE1", "restart");
    }

    @Override
    protected void onStop() {
        Log.e("CYCLE1", "stop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.e("CYCLE1", "pause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.e("CYCLE1", "destroy");
        super.onDestroy();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptLogin() {
        if (!MeteorSingleton.getInstance().isConnected()) {
            Snackbar.make(mLoginFormView, getString(R.string.error_meteor_connection), Snackbar.LENGTH_INDEFINITE)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MeteorSingleton.getInstance().reconnect();
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
            return false;
        }

        if (authTask) {
            return false;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            return this.attemptLogin(username, password);
        }
    }

    private boolean attemptLogin(final String username, final String password) {
        showProgress(authTask = true);
        MeteorSingleton.getInstance().loginWithUsername(username, password, new ResultListener() {

            @Override
            public void onSuccess(String res) {
                Log.i("Login", res);

                SharedPreferences.Editor ed = mPrefs.edit();
                ed.putString(getString(R.string.preferences_file_key) + ".username", username);
                if (mRememberMe.isChecked()) {
                    ed.putString(getString(R.string.preferences_file_key) + ".password", password);
                } else {
                    ed.remove(getString(R.string.preferences_file_key) + ".password");
                    mPasswordView.setText("");
                }
                ed.apply();

                if (MeteorSingleton.getInstance().isLoggedIn()) {
                    startAnimeList();
                }
            }

            @Override
            public void onError(String code, String err, String res) {
                showProgress(authTask = false);

                SharedPreferences.Editor ed = mPrefs.edit();
                ed.remove(getString(R.string.preferences_file_key) + ".password");
                ed.apply();

                Snackbar.make(mLoginFormView.getRootView(), code, Snackbar.LENGTH_SHORT).show();
                if (err.contains("password")) {
                    mPasswordView.setError(err);
                    mPasswordView.requestFocus();
                } else {
                    mUsernameView.setError(err);
                    mUsernameView.requestFocus();
                }
            }
        });
        return true;
    }

    private void startAnimeList() {
        MeteorSingleton.getInstance().unsetCallback(LoginActivity.this);
        Intent intent = new Intent(getApplicationContext(), ShowsActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onConnect(boolean b) {
    }

    @Override
    public void onDisconnect(int i, String res) {
        showProgress(authTask = false);
        Snackbar.make(mLoginFormView.getRootView(), res, Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MeteorSingleton.getInstance().reconnect();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
        Log.e("WS", "Disconnected // " + res);
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
        Snackbar.make(mLoginFormView.getRootView(), e.getMessage(), Snackbar.LENGTH_INDEFINITE)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MeteorSingleton.getInstance().reconnect();
                    }
                })
                .setActionTextColor(Color.RED)
                .show();
    }
}

