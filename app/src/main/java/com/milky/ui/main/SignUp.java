package com.milky.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DatabaseVersioControl;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.milky.utils.UserPrefrences;

public class SignUp extends AppCompatActivity {
    private Button _signUp, _signIn;
    private DatabaseHelper _dbHelper;
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initResources();

        if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {

                Intent i = new Intent(SignUp.this, MainActivity.class);
                startActivity(i);
                finish();

        }

        _signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AppUtil.getInstance().getPrefrences().getBoolean(UserPrefrences.VALID_USER, false)) {
//                    Intent i = new Intent(SignUp.this, MainActivity.class);
//                    startActivity(i);
//                } else
//                    Toast.makeText(SignUp.this, "Please login with facebook !", Toast.LENGTH_SHORT).show();
                if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
                        Intent i = new Intent(SignUp.this, MainActivity.class);
                        startActivity(i);
                        finish();

                } else {
                    Intent i = new Intent(SignUp.this, FarmerSignup.class);
                    startActivity(i);
                    finish();
                }
//                if(_dbHelper.isTableNotEmpty(TableNames.ACCOUNT))
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

                //comment to bye pass the farmer signup
                if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
                        Intent i = new Intent(SignUp.this, MainActivity.class);
                        startActivity(i);
                        finish();

                } else {
                    Intent i = new Intent(SignUp.this, FarmerSignup.class);
                    startActivity(i);
                    finish();
                }
                        //comment to bye pass the farmer signup end comment



            }
        });
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//
//
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//                Toast.makeText(SignUp.this, "Some error occured, Please try again later!", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
//        AppEventsLogger.deactivateApp(this);
    }

    private void initResources() {
        _signIn = (Button) findViewById(R.id.signin);
        _signUp = (Button) findViewById(R.id.signup);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent i = new Intent(SignUp.this, FarmerSignup.class);
        startActivity(i);
    }
}

