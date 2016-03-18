package com.milky.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.tyczj.extendedcalendarview.ExtcalCustomerSettingTableManagement;
import com.tyczj.extendedcalendarview.ExtcalDatabaseHelper;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Neha on 11/20/2015.
 */
public class BillingEdit extends AppCompatActivity implements OnTaskCompleteListner {
    private Intent intent;
    private Toolbar _mToolbar;
    private EditText milk_quantity;
    private EditText rate;
    private EditText balance_amount;
    private EditText tax, payment_made;
    private Button save, clear_bill, send_bill;
    private TextView start_date, end_date, total_amount, clera_bill_text;
    private LinearLayout _payment;
    private TextInputLayout _amount_layout;
    private DatabaseHelper _dbHelper;
    private int balanceType = 0;
    private ExtcalDatabaseHelper _exDb;

    @Override
    protected void onResume() {
        super.onResume();
        getview();
    }

    private void getview() {
        milk_quantity = (EditText) findViewById(R.id.milk_quantity);
        _exDb = new ExtcalDatabaseHelper(this);
        rate = (EditText) findViewById(R.id.rate);
        balance_amount = (EditText) findViewById(R.id.balance_amount);
        tax = (EditText) findViewById(R.id.tax);
        save = (Button) findViewById(R.id.save);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        start_date.setText(intent.getStringExtra("start_date"));
        end_date.setText(intent.getStringExtra("end_date"));
        _amount_layout = (TextInputLayout) findViewById(R.id.amount_layout);
        _payment = (LinearLayout) findViewById(R.id.payment);
        payment_made = (EditText) findViewById(R.id.payment_amount);
        payment_made.setText(intent.getStringExtra("payment_made"));
        if (intent.getStringExtra("balance_type").equals("1"))
            balanceType = 1;
        Calendar cal = Calendar.getInstance();
        send_bill = (Button) findViewById(R.id.send_bill);
        try {
            cal.setTime(Constants._display_format.parse(intent.getStringExtra("end_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        send_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Account.getLeftsmsCount(_dbHelper.getReadableDatabase())>=1)
                new SendBillSMS().execute();
                else
                    Toast.makeText(BillingEdit.this, "Your SMS quota has expired. Please contact administrator !", Toast.LENGTH_LONG).show();

            }
        });
        rate.setText(intent.getStringExtra("totalPrice"));
        start_date.setEnabled(false);
        end_date.setEnabled(false);
        milk_quantity.setText(intent.getStringExtra("quantity"));
        milk_quantity.setFocusable(false);
        milk_quantity.setFocusableInTouchMode(false);
        clear_bill = (Button) findViewById(R.id.clear_bill);
        clera_bill_text = (TextView) findViewById(R.id.clera_bill_text);
        //TODo changed roll date
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = Constants.work_format.parse(intent.getStringExtra("roll_date"));
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal.get(Calendar.DAY_OF_MONTH) >= calendar.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH)>=calendar.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) >= calendar.get(Calendar.YEAR) && intent.getStringExtra("clear").equals("1")) {
//        if ((cal.get(Calendar.DAY_OF_MONTH)) == 5) {
            clear_bill.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent_button_click));
            clera_bill_text.setVisibility(View.GONE);

            clear_bill.setEnabled(true);


            clear_bill.setTextColor(getResources().getColor(R.color.white));
        } else {
            clear_bill.setBackgroundColor(getResources().getColor(R.color.gray));
            clera_bill_text.setText("You can clear this bill once the final bill is generated on " + calendar.get(Calendar.DAY_OF_MONTH)
                    + " " + Constants.MONTHS[calendar.get(Calendar.MONTH)]);
            clear_bill.setEnabled(false);
        }
        /*If bill is already cleared*/
        if (intent.getStringExtra("clear").equals("0")) {
            clear_bill.setVisibility(View.GONE);
            clera_bill_text.setVisibility(View.GONE);
            send_bill.setVisibility(View.GONE);
            _payment.setVisibility(View.VISIBLE);
        } else
            _payment.setVisibility(View.GONE);


        clear_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Calendar c = Calendar.getInstance();
//                String day = c.get(Calendar.YEAR) + "-" + String.format("%02d", c.get(Calendar.MONTH)) + "-" + String.format("%02d", c.get(Calendar.DAY_OF_MONTH));
//                BillTableManagement.updateClearBills(_dbHelper.getWritableDatabase(), day, getIntent().getStringExtra("custId"));

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BillingEdit.this);
                final AlertDialog dialog = alertBuilder.create();
                LayoutInflater inflater = (LayoutInflater) BillingEdit.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view1 = inflater.inflate(R.layout.edit_quantity_popup, null, false);
                dialog.setView(view1);

                final EditText payment = (EditText) view1.findViewById(R.id.milk_quantity);
                final TextInputLayout quantity_layout = (TextInputLayout) view1.findViewById(R.id.quantity_layout);
                payment.setHint("Enter payment (Rs)");

                ((Button) view1.findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (payment.getText().toString().equals("")) {
                            quantity_layout.setError("Enter quantity!");
                        } else {
                            VBill holder = new VBill();

                            float payment_made = Float.parseFloat(payment.getText().toString());
                            float bill_amount = Float.parseFloat(intent.getStringExtra("total"));

                            float bill = payment_made - bill_amount;
                            if (bill_amount >= payment_made) {
                                holder.setBalance(String.valueOf(round(bill_amount - payment_made, 2)));
                                holder.setBalanceType("0");
                            } else {
                                holder.setBalance(String.valueOf(round(bill, 2)));
                                holder.setBalanceType("1");
                            }
                            holder.setPaymentMode(String.valueOf(round(payment_made, 2)));


                            holder.setStartDate(intent.getStringExtra("start_date_work_format"));
                            holder.setBillMade(String.valueOf(round(bill_amount, 2)));
                            holder.setRate(intent.getStringExtra("totalPrice"));
//                            BillTableManagement.updateBillData(_dbHelper.getWritableDatabase(), holder);
                            Calendar c = Calendar.getInstance();
                            String day = c.get(Calendar.YEAR) + "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", c.get(Calendar.DAY_OF_MONTH));

                            CustomersTableMagagement.updateBalance(_dbHelper.getWritableDatabase(), holder.getBalance(), intent.getStringExtra("custId"), holder.getBalanceType());
//                            CustomerSettingTableManagement.updateBalance(_dbHelper.getWritableDatabase(), holder.getBalance(), intent.getStringExtra("custId"), holder.getBalanceType(), day);
                            //TODO ExtCal SETTINGS DB
                            ExtcalCustomerSettingTableManagement.updateBalance(_exDb.getWritableDatabase(), holder.getBalance(), intent.getStringExtra("custId"), holder.getBalanceType(), day);

                            BillTableManagement.updateClearBills(_dbHelper.getWritableDatabase(), day, getIntent().getStringExtra("custId"), holder);
                            dialog.dismiss();
                            Constants.REFRESH_CALANDER=true;
                            BillingEdit.this.finish();
                        }

                    }
                });
                ((Button) view1.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (dialog != null)
                    dialog.show();

            }
        });

        balance_amount.setText(intent.getStringExtra("balance"));
        balance_amount.setFocusable(false);
        balance_amount.setFocusableInTouchMode(false);
//        if (balanceType == 1)
//            balance_amount.setHint("Balance due (Rs)");

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

    private class SendBillSMS extends AsyncTask<Void, String, String> {
        AlertDialog dialog;
        Handler progressHandler = new Handler();
        int progress = 0;

        String msg = "";
        int finalI = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(GlobalSetting.this, "Please wait", "Sending SMS...");
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BillingEdit.this);
            dialog = dialogBuilder.create();
            messageProgress(dialog);
        }

        private TextView messageSent;
        private ProgressBar androidProgressBar;
        private Button ok;

        private void messageProgress(final AlertDialog dialog) {
            final LayoutInflater inflater = (LayoutInflater)
                    BillingEdit.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.message_progress, null, false);
            dialog.setView(dialogView);
            dialog.setCancelable(false);
            messageSent = (TextView) dialogView.findViewById(R.id.messageSent);
            androidProgressBar = (ProgressBar) dialogView.findViewById(R.id.horizontal_progress_bar);
            ok = (Button) dialogView.findViewById(R.id.ok);
            ok.setVisibility(View.INVISIBLE);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {


            new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i < 1; ++i) {
                        String mesg = null;
                        try {
                            mesg = URLEncoder.encode("Dear " + intent.getStringExtra("titleString") + ", " + "Your milk bill from" +start_date.getText().toString()+" to "+end_date.getText().toString()+" is Rs. "+ intent.getStringExtra("total")
                                    +". Total quantity "+milk_quantity.getText().toString()+" litres. Rate is "+ rate.getText().toString()+"/litre.", "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        SendSmsTouser(CustomersTableMagagement.getCustomerMobileNo(_dbHelper.getReadableDatabase(), getIntent().getStringExtra("custId")), mesg);


                        finalI = i + 1;
                        progressHandler.post(new Runnable() {
                            public void run() {
                                progress += (100 / 1);
                                androidProgressBar.setProgress(progress);
                                //Status update in textview

                                if (finalI != 1)
                                    msg = "Sending message: " + finalI + "/" + 1;
                                else
                                    msg = "Sent message: " + finalI + "/" + 1;
                                publishProgress(msg);

                            }
                        });
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }).start();

            return msg;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            messageSent.setText(values[0]);


            Account.updateSMSCount(_dbHelper.getWritableDatabase(), 1);
            ok.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

//            progressDialog.cancel();
//            dialog.dismiss();
        }
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
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

    String url = "";

    private void SendSmsTouser(String mob, final String sms) {

        String append = "?mobile=" + mob + "&message=" + sms;
        HttpAsycTask dataTask = new HttpAsycTask();
        url = ServerApis.SMS_API_ROOT + append ;
        dataTask.runRequest(ServerApis.SMS_API_ROOT + append, null, this, false, null);
    }

    @Override
    public void onTaskCompleted(String type, HashMap<String, String> listType) {

    }
}
