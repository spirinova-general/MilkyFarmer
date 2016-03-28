package com.milky.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.ui.customers.CustomerTabFragment;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Neha on 11/19/2015.
 */
public class CustomersActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Toolbar _mToolbar;
    public static int POSITION = 0;
    private FloatingActionButton fabDelete;
    public static Intent _mIntent;
    public static String titleString = "";

    @Override
    protected void onResume() {
        super.onResume();
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new CustomerTabFragment()).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_main_activity);

        initResources();
        setActionBar();

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new CustomerTabFragment()).commit();


    }

    public static LinearLayout saveManu;

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_withsubtitle_layout, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView subTitle = (TextView) mCustomView.findViewById(R.id.date);
        ImageView deleteCustomer = (ImageView) mCustomView.findViewById(R.id.deleteCustomer);
        deleteCustomer.setVisibility(View.GONE);

        saveManu = (LinearLayout) mCustomView.findViewById(R.id.saveManu);

        title.setText(_mIntent.getStringExtra("fname") + " " + _mIntent.getStringExtra("lname"));
        titleString = _mIntent.getStringExtra("fname") + " " + _mIntent.getStringExtra("lname");
        Calendar shownDate = Calendar.getInstance();

        try {
            shownDate.setTime(Constants.work_format.parse(_mIntent.getStringExtra("added_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String date = Constants._display_format.format(shownDate.getTime());
        subTitle.setText("Date Added- " + Constants._display_format.format(shownDate.getTime()));
        subTitle.setVisibility(View.VISIBLE);
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initResources() {
        _mIntent = this.getIntent();
        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CustomersActivity.this);
                builder.setTitle("Delete Customer ?");
                builder.setMessage("Want to delete " + getIntent().getStringExtra("fname") + " " + getIntent().getStringExtra("lname")
                        + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CustomersTableMagagement.updatedeletedCustomerDetail(AppUtil.getInstance().getDatabaseHandler().getWritableDatabase(), getIntent().getStringExtra("cust_id"), Constants.getCurrentDate());
//                        ExtcalCustomerSettingTableManagement.updateDeletetdCustomer(exDb.getWritableDatabase(), getIntent().getStringExtra("cust_id"), Constants.getCurrentDate());
//                        DeliveryTableManagement.updateDeletedCustomer(exDb.getWritableDatabase(), Constants.getCurrentDate(), getIntent().getStringExtra("cust_id"));
                        BillTableManagement.updateDeletedOn(AppUtil.getInstance().getDatabaseHandler().getWritableDatabase(), getIntent().getIntExtra("cust_id",0));
                        POSITION=0;
                        Constants.REFRESH_BILL=true;
                        Constants.REFRESH_CALANDER=true;
                        Constants.REFRESH_CUSTOMERS=true;
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       int page = POSITION;
        switch (page) {
            case 0:
                saveManu.setVisibility(View.VISIBLE);
                fabDelete.setVisibility(View.VISIBLE);


                break;
            case 1:
                saveManu.setVisibility(View.GONE);
                fabDelete.setVisibility(View.GONE);

                break;
            case 2:
                saveManu.setVisibility(View.GONE);
                fabDelete.setVisibility(View.GONE);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            POSITION=0;
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        POSITION=0;
        finish();
    }
}
