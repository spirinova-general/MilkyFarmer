package com.milky.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.core.GlobalSettings;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.serviceclasses.AccountService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.service.databaseutils.serviceinterface.IAccountService;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.TextValidationMessage;
import com.milky.utils.UserPrefrences;
import com.milky.service.core.Account;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class FarmerSignup extends AppCompatActivity implements OnTaskCompleteListner {
    private TextInputLayout _nameLayout, _lastnameLayout, _mobileLayout, _passwordLayout, otp_layout;
    private EditText _firstName, _lastName, _mobile, _password, otp;
    private DatabaseHelper _dbhelper;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;
    private Button otpButton;
    private AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);

//        Check if the file exists in mobile storage..
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "milky");
        if (!f.isDirectory()) {
            f.mkdirs();
        }
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
        ActionBar actionBar = getSupportActionBar();
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
                if (AppUtil.getInstance().isNetworkAvailable(FarmerSignup.this)) {
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

                        if (Constants.OTP.equals(""))
                            otp_layout.setError("OTP expired, Get OTP again");
                        else if (Constants.OTP.equals(otp.getText().toString().trim())) {

                            JSONObject jsonObject = new JSONObject();
                            progressBar = new ProgressDialog(FarmerSignup.this);
                            progressBar.setCancelable(true);
                            progressBar.setMessage("Signing Up ...");
                            //progress dialog type
                            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progressBar.setProgress(0);
                            progressBar.setMax(100);
                            progressBar.show();

                            Calendar cal = Calendar.getInstance();
                            //formating the current date and time
                            String startDate = Constants.api_format.format(cal.getTime());
                            //To get the maximum date of month
                            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
                            Date date = cal.getTime();
                            String endDate = Constants.api_format.format(date);
                            try {
                                //inserting the data to json object
                                jsonObject.put("FarmerCode", Constants.generateOTP());
                                jsonObject.put("FirstName", _firstName.getText().toString());
                                jsonObject.put("LastName", _lastName.getText().toString());
                                jsonObject.put("Mobile", _mobile.getText().toString());
                                jsonObject.put("Validated", "true");
                                jsonObject.put("Dirty", "0");
                                jsonObject.put("DateAdded", startDate);
                                jsonObject.put("DateModified", startDate);
                                jsonObject.put("StartDate", startDate);
                                jsonObject.put("EndDate", endDate);
                                jsonObject.put("UsedSms", "0");
                                jsonObject.put("TotalSms", "10");
                                jsonObject.put("Id", 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            HttpAsycTask dataTask = new HttpAsycTask();
                            dataTask.runRequest(ServerApis.ACCOUNT_API, jsonObject, FarmerSignup.this, true, null);
                        } else otp_layout.setError("Invalid OTP");
                    }
                } else
                    Toast.makeText(FarmerSignup.this, "Network is not available. ", Toast.LENGTH_SHORT).show();

            }
        });


        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initResources() {
        AppUtil.getInstance().startTimer();
        preferences = AppUtil.getInstance().getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);
        _firstName = (EditText) findViewById(R.id.first_name);
        _lastName = (EditText) findViewById(R.id.last_name);
        _mobile = (EditText) findViewById(R.id.mobile);
        _password = (EditText) findViewById(R.id.password);

        edit = preferences.edit();
        _dbhelper = AppUtil.getInstance().getDatabaseHandler();
        accountService = new AccountService();

        _nameLayout = (TextInputLayout) findViewById(R.id.name_layout);
        _lastnameLayout = (TextInputLayout) findViewById(R.id.last_name_layout);
        _mobileLayout = (TextInputLayout) findViewById(R.id.mobile_layout);
        _passwordLayout = (TextInputLayout) findViewById(R.id.password_layout);
        /*Validation for text input*/
        _firstName.addTextChangedListener(new TextValidationMessage(_firstName, _nameLayout, this, false, false, false, false, false));
        _lastName.addTextChangedListener(new TextValidationMessage(_lastName, _lastnameLayout, this, false, false, false, false, false));
        _mobile.addTextChangedListener(new TextValidationMessage(_mobile, _mobileLayout, this, true, false, false, false, false));
        _password.addTextChangedListener(new TextValidationMessage(_password, _passwordLayout, this, false, false, false, false, false));
        otp_layout = (TextInputLayout) findViewById(R.id.otp_layout);
        otp = (EditText) findViewById(R.id.otp);
        otpButton = (Button) findViewById(R.id.otpButton);
        otpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                otpButton.setEnabled(false);
                if (AppUtil.getInstance().isNetworkAvailable(FarmerSignup.this)) {
                    String mesg = null;
                    try {
                        Constants.OTP = Constants.generateOTP();
                        mesg = URLEncoder.encode("OTP to sign in KrushiVikas is: " + Constants.OTP, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    if (!_mobile.getText().toString().equals("") && _mobile.getText().length() == 10) {
                        _mobileLayout.setError(null);
                        Constants.SendSmsTouser(_mobile.getText().toString(), mesg,FarmerSignup.this);
                    } else
                        _mobileLayout.setError("Enter mobile number !");
                } else
                    Toast.makeText(FarmerSignup.this, "Network is not available. ", Toast.LENGTH_SHORT).show();


//                AppUtil.getInstance().showNotification(FarmerSignup.this, FarmerSignup.this.getResources().getString(R.string.app_name), "Your OTP for "+FarmerSignup.this.getResources().getString(R.string.app_name)+" is ", new Intent(FarmerSignup.this, NotificationBroadcastReceiver.class));
            }
        });
    }


    ProgressDialog progressBar;

    @Override
    public void onTaskCompleted(String type, HashMap<String, String> listType) {

        if (type.equals(ServerApis.ACCOUNT_API)) {
            Account holder = new Account();
            try {
                JSONObject result = Constants.API_RESPONCE;
                holder.setFarmerCode(result.getString("FarmerCode"));
                holder.setFirstName(result.getString("FirstName"));
                holder.setLastName(result.getString("LastName"));
                holder.setMobile(result.getString("Mobile"));
                if (result.getBoolean("Validated"))
                    holder.setValidated(0);
                else
                    holder.setValidated(1);
                holder.setDateAdded(result.getString("DateAdded"));
                holder.setDateModified(result.getString("DateModified"));
//                holder.setAccountStartDate(result.getString("StartDate"));
                holder.setEndDate(result.getString("EndDate"));
                holder.setUsedSms(result.getInt("UsedSms"));
                holder.setTotalSms(result.getInt("TotalSms"));
                holder.setServerAccountId(result.getInt("Id"));
                edit.putString(UserPrefrences.MOBILE_NUMBER, _mobile.getText().toString());
                edit.putString(UserPrefrences.INSERT_BILL, "0");
                edit.commit();

//                com.milky.service.databaseutils.Account.insertAccountDetails(_dbhelper.getWritableDatabase(), holder);
//                Insert Account details into table
                accountService.insert(holder);

                GlobalSettings settinsHolder = new GlobalSettings();
                Calendar cal = Calendar.getInstance();
                settinsHolder.setRollDate(String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) +
                        "-" + String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH)));
                settinsHolder.setTax(0);
                settinsHolder.setDefaultRate(0);
//                Insert global settings data..
               new GlobalSettingsService().insert(settinsHolder);
                startActivity(new Intent(FarmerSignup.this, MainActivity.class));
                this.finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            if (otpButton.getText().toString().equals("Get OTP"))
                otpButton.setText("Resend OTP");
            else {
                otp_layout.setError(null);

//                ((LinearLayout) findViewById(R.id.textOtp)).setVisibility(View.VISIBLE);
                AppUtil.getInstance().cancelTimer(FarmerSignup.this);
                AppUtil.getInstance().startTimer();
            }

        }
    }
}


