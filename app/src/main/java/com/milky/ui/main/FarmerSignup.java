package com.milky.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.milky.utils.TextValidationMessage;
import com.milky.utils.UserPrefrences;
import com.milky.viewmodel.VAccount;

import java.util.Calendar;

public class FarmerSignup extends AppCompatActivity {
    private TextInputLayout _nameLayout, _lastnameLayout, _mobileLayout;
    private EditText _firstName, _lastName, _mobile;
    private DatabaseHelper _dbhelper;
    private SharedPreferences preferences;

    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        initResources();
        setActionBar();
    }

    private void setActionBar() {
        Toolbar _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_layout, null);

        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView subTitle = (TextView) mCustomView.findViewById(R.id.date);
        subTitle.setVisibility(View.GONE);
        title.setText(
                "Profile");
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
                else {
                    _nameLayout.setError(null);
                    _lastnameLayout.setError(null);
                    _mobileLayout.setError(null);
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


                    if (_dbhelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT))
                        Account.updateAccountDetails(_dbhelper.getWritableDatabase(), holder);
                    else

                        Account.insertAccountDetails(_dbhelper.getWritableDatabase(), holder);
                    startActivity(new Intent(FarmerSignup.this, MainActivity.class));
                }

            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initResources() {
        _firstName = (EditText) findViewById(R.id.first_name);
        _lastName = (EditText) findViewById(R.id.last_name);
        _mobile = (EditText) findViewById(R.id.mobile);
        preferences = AppUtil.getInstance().getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);
        edit = preferences.edit();
        _dbhelper = AppUtil.getInstance().getDatabaseHandler();

        _nameLayout = (TextInputLayout) findViewById(R.id.name_layout);
        _lastnameLayout = (TextInputLayout) findViewById(R.id.last_name_layout);
        _mobileLayout = (TextInputLayout) findViewById(R.id.mobile_layout);

        /*Validation for text input*/
        new TextValidationMessage(_firstName, _nameLayout, this, false);
        new TextValidationMessage(_lastName, _lastnameLayout, this, false);
        new TextValidationMessage(_mobile, _mobileLayout, this, true);
    }


}
