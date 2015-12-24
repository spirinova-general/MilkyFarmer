package com.milky.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.milky.R;
import com.milky.ui.customers.FarmerSignup;

public class SignUp extends AppCompatActivity {
private Button _signUp,_signIn;
    private ImageView fb_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initResources();
        _signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, MainActivity.class);
                startActivity(i);
            }
        });
        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this,MainActivity.class);
                startActivity(i);
            }
        });
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(SignUp.this, FarmerSignup.class));
            }
        });
    }
    private void initResources()
    {
        _signIn = (Button) findViewById(R.id.signin);
        _signUp = (Button) findViewById(R.id.signup);
        fb_login = (ImageView) findViewById(R.id.fb_login);
    }

}
