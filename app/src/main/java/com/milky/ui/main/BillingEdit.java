package com.milky.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import com.milky.R;
import com.milky.service.databaseutils.GlobalSettingTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;

/**
 * Created by Neha on 11/20/2015.
 */
public class BillingEdit extends AppCompatActivity {
    private Intent intent;
    private Toolbar _mToolbar;
    private EditText milk_quantity;
    private EditText rate;
    private EditText balance_amount;
    private EditText tax;
    private Button save;
    private TextView start_date, end_date, total_amount;

    private void getview() {
        milk_quantity = (EditText) findViewById(R.id.milk_quantity);
        rate = (EditText) findViewById(R.id.rate);
        balance_amount = (EditText) findViewById(R.id.balance_amount);
        tax = (EditText) findViewById(R.id.tax);
        save = (Button) findViewById(R.id.save);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);

        start_date.setText(intent.getStringExtra("start_date"));
        end_date.setText(intent.getStringExtra("end_date"));
        rate.setText(intent.getStringExtra("amount"));
        start_date.setEnabled(false);
        end_date.setEnabled(false);
        milk_quantity.setText(intent.getStringExtra("quantity"));
        milk_quantity.setFocusable(false);
        milk_quantity.setFocusableInTouchMode(false);

        balance_amount.setText(intent.getStringExtra("balance"));
        balance_amount.setFocusable(false);
        balance_amount.setFocusableInTouchMode(false);
        total_amount = (TextView) findViewById(R.id.total_amount);
        total_amount.setText(intent.getStringExtra("total"));
        tax.setFocusable(false);
        tax.setFocusableInTouchMode(false);
        tax.setText(GlobalSettingTableManagement.getDefaultTax(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_fragment);
        intent = this.getIntent();
        setActionBar();
        getview();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


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
        subTitle.setVisibility(View.GONE);
        title.setText(intent.getStringExtra("titleString"));
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
