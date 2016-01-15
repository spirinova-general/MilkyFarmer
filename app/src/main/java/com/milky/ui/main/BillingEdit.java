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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

import java.text.ParseException;
import java.util.Calendar;

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
    private Button save, clear_bill;
    private TextView start_date, end_date, total_amount, clera_bill_text;

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
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(Constants._display_format.parse(intent.getStringExtra("end_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        rate.setText(intent.getStringExtra("totalPrice"));
        start_date.setEnabled(false);
        end_date.setEnabled(false);
        milk_quantity.setText(intent.getStringExtra("quantity"));
        milk_quantity.setFocusable(false);
        milk_quantity.setFocusableInTouchMode(false);
        clear_bill = (Button) findViewById(R.id.clear_bill);
        clera_bill_text = (TextView) findViewById(R.id.clera_bill_text);
        if (cal.get(Calendar.DAY_OF_MONTH) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
            clera_bill_text.setVisibility(View.GONE);
            clear_bill.setEnabled(true);
            clear_bill.setTextColor(getResources().getColor(R.color.white));
        } else {
            clera_bill_text.setText("You can clear this bill once the final bill is generated on " + cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            +" "+Constants.MONTHS[cal.get(Calendar.MONTH)]);
            clear_bill.setEnabled(false);
            clear_bill.setTextColor(getResources().getColor(R.color.gray_lighter));
        }

        balance_amount.setText(intent.getStringExtra("balance"));
        balance_amount.setFocusable(false);
        balance_amount.setFocusableInTouchMode(false);
        total_amount = (TextView) findViewById(R.id.total_amount);
        total_amount.setText(intent.getStringExtra("total"));
        tax.setFocusable(false);
        tax.setFocusableInTouchMode(false);
        tax.setText(Account.getDefautTax(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase()));
        AppUtil.getInstance().getDatabaseHandler().close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_fragment);
        intent = this.getIntent();
        setActionBar();
        getview();


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
        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);

        TextView title = (TextView) mCustomView.findViewById(R.id.title);

        title.setText(intent.getStringExtra("titleString") + "(Bill)");
        LinearLayout saveManu = (LinearLayout) mCustomView.findViewById(R.id.saveManu);

        saveManu.setVisibility(View.GONE);
        saveManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


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
