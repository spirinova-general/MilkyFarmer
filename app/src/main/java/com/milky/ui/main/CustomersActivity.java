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
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.ui.customers.CustomerTabFragment;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

/**
 * Created by Neha on 11/19/2015.
 */
public class CustomersActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Toolbar _mToolbar;
    public static int POSITION = 0;

    private boolean _mIsToAddCustomer = false;
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
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_layout, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView subTitle = (TextView) mCustomView.findViewById(R.id.date);
        ImageView deleteCustomer = (ImageView) mCustomView.findViewById(R.id.deleteCustomer);
        deleteCustomer.setVisibility(View.GONE);

        saveManu = (LinearLayout) mCustomView.findViewById(R.id.saveManu);


        title.setText(_mIntent.getStringExtra("fname") + " " + _mIntent.getStringExtra("lname"));
        titleString = _mIntent.getStringExtra("fname") + " " + _mIntent.getStringExtra("lname");
        subTitle.setText("Date Added- " + _mIntent.getStringExtra("added_date"));
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initResources() {
        _mIntent = this.getIntent();
        _mIsToAddCustomer = _mIntent.getBooleanExtra("istoAddCustomer", false);
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
                        CustomerSettingTableManagement.updateDeletetdCustomer(AppUtil.getInstance().getDatabaseHandler().getWritableDatabase(), getIntent().getStringExtra("cust_id"), Constants.getCurrentDate());
                        DeliveryTableManagement.updateDeletedCustomer(AppUtil.getInstance().getDatabaseHandler().getWritableDatabase(), Constants.getCurrentDate(), getIntent().getStringExtra("cust_id"));
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


                break;
            case 1:
                saveManu.setVisibility(View.GONE);


                break;
            case 2:
                saveManu.setVisibility(View.GONE);

                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
