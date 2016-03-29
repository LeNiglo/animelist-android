package com.lefrantguillaume.animelist.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.controllers.NetController;

import java.util.concurrent.CancellationException;


/**
 * A login screen that offers login via email/password.
 */
// TODO add a remember password checkbox and skip auth if password is remembered.
public class LoginActivity extends Activity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        this.sharedPreferences = getSharedPreferences(SplashActivity.APP_NAME, MODE_PRIVATE);

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

        String prefUsername = this.sharedPreferences.getString(SplashActivity.APP_NAME + ".username", null);
        if (prefUsername != null) {
            mEmailView.setText(prefUsername);
            mPasswordView.requestFocus();
        } else {
            mEmailView.requestFocus();
        }

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for an email address / username.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {

            JsonObject authBody = new JsonObject();
            authBody.addProperty("password", password);

            if (isEmailValid(email)) {
                authBody.addProperty("email", email);
            } else {
                authBody.addProperty("username", email);
            }

            FutureCallback<JsonObject> cb = new FutureCallback<JsonObject>() {
                @Override
                public void onCompleted(Exception e, JsonObject result) {

                    if (e != null)
                        e.printStackTrace();

                    if (result.has("error")) {
                        String error = result.get("error").getAsString();
                        if (error.contains("pass")) {
                            mPasswordView.setError(error);
                            mPasswordView.requestFocus();
                        } else {
                            mEmailView.setError(error);
                            mEmailView.requestFocus();
                        }
                    } else {
                        SharedPreferences.Editor ed = sharedPreferences.edit();
                        ed.putString(SplashActivity.APP_NAME + ".username", email);
                        ed.putString(SplashActivity.APP_NAME + ".token", result.get("token").getAsString());
                        ed.apply();

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
            };

            NetController.login(this, authBody, cb);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
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

