package com.milky.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DatabaseVersioControl;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.milky.utils.UserPrefrences;

public class SignUp extends AppCompatActivity {
    private Button _signUp, _signIn;
    LoginButton loginButton;
    CallbackManager callbackManager;
    private DatabaseHelper _dbHelper;

    @Override
    protected void onResume() {
        super.onResume();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(getApplicationContext(), getResources().getString(R.string.facebook_app_id));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initResources();
        if (preferences.contains(UserPrefrences.PRE_VERSION)) {
            if (preferences.getInt(UserPrefrences.PRE_VERSION, 0) < DatabaseVersioControl.DATABASE_VERSION) {
                edit.clear();
            }
        }
        edit.putInt(UserPrefrences.PRE_VERSION, DatabaseVersioControl.DATABASE_VERSION);
        edit.commit();
        if (preferences.contains(UserPrefrences.MOBILE_NUMBER)) {
            if (preferences.getString(UserPrefrences.MOBILE_NUMBER, "").length() > 0) {
                Intent i = new Intent(SignUp.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }

        _signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AppUtil.getInstance().getPrefrences().getBoolean(UserPrefrences.VALID_USER, false)) {
//                    Intent i = new Intent(SignUp.this, MainActivity.class);
//                    startActivity(i);
//                } else
//                    Toast.makeText(SignUp.this, "Please login with facebook !", Toast.LENGTH_SHORT).show();
                if (preferences.contains(UserPrefrences.MOBILE_NUMBER)) {
                    if (preferences.getString(UserPrefrences.MOBILE_NUMBER, "").length() > 0) {
                        Intent i = new Intent(SignUp.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SignUp.this, FarmerSignup.class);
                    startActivity(i);
                    finish();
                }
//                if(_dbHelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT))
//                {
//                    Intent i = new Intent(SignUp.this, MainActivity.class);
//                    startActivity(i);
//                }
//                else
//                {
//                    Intent i = new Intent(SignUp.this, FarmerSignup.class);
//                    startActivity(i);
//                }
//                _dbHelper.close();

            }
        });
        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AppUtil.getInstance().getPrefrences().getBoolean(UserPrefrences.VALID_USER, false)) {
//                    Intent i = new Intent(SignUp.this, MainActivity.class);
//                    startActivity(i);
//                } else
//                    Toast.makeText(SignUp.this, "Please login with facebook !", Toast.LENGTH_SHORT).show();
//                Toast.makeText(SignUp.this, "Please login with facebook !", Toast.LENGTH_SHORT).show();
                if (preferences.contains(UserPrefrences.MOBILE_NUMBER)) {
                    if (preferences.getString(UserPrefrences.MOBILE_NUMBER, "").length() > 0) {
                        Intent i = new Intent(SignUp.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(SignUp.this, FarmerSignup.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code


            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(SignUp.this, "Some error occured, Please try again later!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void initResources() {
        _signIn = (Button) findViewById(R.id.signin);
        _signUp = (Button) findViewById(R.id.signup);
        preferences = getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        callbackManager = CallbackManager.Factory.create();

        edit = preferences.edit();

        // Callback registration

    }

    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        edit.putBoolean(UserPrefrences.VALID_USER, true);
        edit.apply();
        Intent i = new Intent(SignUp.this, FarmerSignup.class);
        startActivity(i);
    }
}
