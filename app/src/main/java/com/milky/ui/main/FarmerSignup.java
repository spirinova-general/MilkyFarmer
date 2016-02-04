package com.milky.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.NotificationBroadcastReceiver;
import com.milky.utils.TextValidationMessage;
import com.milky.utils.UserPrefrences;
import com.milky.viewmodel.VAccount;

import java.util.Calendar;
import java.util.TimerTask;

public class FarmerSignup extends AppCompatActivity {
    private TextInputLayout _nameLayout, _lastnameLayout, _mobileLayout, _passwordLayout, otp_layout;
    private EditText _firstName, _lastName, _mobile, _password, otp;
    private DatabaseHelper _dbhelper;
    private SharedPreferences preferences;

    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        preferences = AppUtil.getInstance().getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);

        initResources();
        setActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setActionBar() {
        Toolbar _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);

        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView savetext = (TextView) mCustomView.findViewById(R.id.savetext);
        savetext.setText("Next");
        title.setText("Sign Up");
        LinearLayout saveManu = (LinearLayout) mCustomView.findViewById(R.id.saveManu);

        saveManu.setVisibility(View.VISIBLE);
        saveManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(_firstName.getText().toString()))
                    _nameLayout.setError(getResources().getString(R.string.field_cant_empty));
                else if ("".equals(_lastName.getText().toString()))
                    _lastnameLayout.setError(getResources().getString(R.string.field_cant_empty));
                else if ("".equals(_mobile.getText().toString()))
                    _mobileLayout.setError(getResources().getString(R.string.field_cant_empty));
                else if ("".equals(_password.getText().toString()))
                    _passwordLayout.setError(getResources().getString(R.string.field_cant_empty));
                else {
                    _nameLayout.setError(null);
                    _lastnameLayout.setError(null);
                    _mobileLayout.setError(null);
                    _passwordLayout.setError(null);
                    otp_layout.setError(null);
                    Calendar cal = Calendar.getInstance();
                    String date = String.valueOf(cal.get(Calendar.MONTH))
                            + "-" + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + "-" + String.valueOf(cal.get(Calendar.YEAR));
                    VAccount holder = new VAccount();
                    holder.setDateAdded(date);
                    holder.setDateModified(date);
                    holder.setFirstName(_firstName.getText().toString());
                    holder.setLastName(_lastName.getText().toString());
                    holder.setMobile(_mobile.getText().toString());
                    holder.setRate("0");
                    holder.setTax("0");
                    holder.setFarmerCode(Constants.generateOTP());
                    if (Constants.OTP.equals("")) {
                        otp_layout.setError("OTP expired, Get OTP again");
                    } else if (Constants.OTP.equals(otp.getText().toString().trim())) {
                        edit.putString(UserPrefrences.MOBILE_NUMBER, _mobile.getText().toString());
                        edit.putString(UserPrefrences.INSERT_BILL,"0");
                        edit.commit();

                        if (_dbhelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT))
                            Account.updateAccountDetails(_dbhelper.getWritableDatabase(), holder);
                        else

                            Account.insertAccountDetails(_dbhelper.getWritableDatabase(), holder);
                        startActivity(new Intent(FarmerSignup.this, MainActivity.class));
                        _dbhelper.close();
                        finish();
                    } else {
                        otp_layout.setError("Invalid OTP");
                    }

                }

            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    Button otpButton;

    private void initResources() {
        AppUtil.getInstance().startTimer();
        AppUtil.getInstance().showNotification(FarmerSignup.this, getResources().getString(R.string.app_name), "Your OTP for Milky is ", new Intent(FarmerSignup.this, NotificationBroadcastReceiver.class));

        _firstName = (EditText) findViewById(R.id.first_name);
        _lastName = (EditText) findViewById(R.id.last_name);
        _mobile = (EditText) findViewById(R.id.mobile);
        _password = (EditText) findViewById(R.id.password);

        edit = preferences.edit();
        _dbhelper = AppUtil.getInstance().getDatabaseHandler();

        _nameLayout = (TextInputLayout) findViewById(R.id.name_layout);
        _lastnameLayout = (TextInputLayout) findViewById(R.id.last_name_layout);
        _mobileLayout = (TextInputLayout) findViewById(R.id.mobile_layout);
        _passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        /*Validation for text input*/
        _firstName.addTextChangedListener(new TextValidationMessage(_firstName, _nameLayout, this, false));
        _lastName.addTextChangedListener(new TextValidationMessage(_lastName, _lastnameLayout, this, false));
        _mobile.addTextChangedListener(new TextValidationMessage(_mobile, _mobileLayout, this, true));
        _password.addTextChangedListener(new TextValidationMessage(_password, _passwordLayout, this, false));
        otp_layout = (TextInputLayout) findViewById(R.id.otp_layout);
        otp = (EditText) findViewById(R.id.otp);
        otpButton = (Button) findViewById(R.id.otpButton);
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                otpButton.setEnabled(false);
                otp_layout.setError(null);
//                ((LinearLayout) findViewById(R.id.textOtp)).setVisibility(View.VISIBLE);
                AppUtil.getInstance().cancelTimer(FarmerSignup.this);
                AppUtil.getInstance().startTimer();
                AppUtil.getInstance().showNotification(FarmerSignup.this, "Milky ", "Your OTP for Milky is ", new Intent(FarmerSignup.this, NotificationBroadcastReceiver.class));
            }
        });
    }




}
