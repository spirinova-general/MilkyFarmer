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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceclasses.AccountService;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceclasses.CustomersService;
import com.milky.service.databaseutils.serviceclasses.CustomersSettingService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.milky.ui.adapters.BillDetailDeliveryAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Bill;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillingEdit extends AppCompatActivity implements OnTaskCompleteListner {
    private Intent intent;
    private TextView milk_quantity;
    //    private EditText rate;
    private TextView start_date, payment_text;
    private TextView end_date;
    protected TextInputLayout _amount_layout;
    private AccountService accountService;
    private ListView deliveryList;
    private List<CustomersSetting> allDeliveryData;
    private Bill allBillData = null;
    int billId, custId;
    ICustomers customers = new CustomersService();
    private BillDetailDeliveryAdapter adpter;

    @Override
    protected void onResume() {
        super.onResume();
        getview();
    }

    private void getview() {
        milk_quantity = (TextView) findViewById(R.id.milk_quantity);
        TextView balance_amount = (TextView) findViewById(R.id.balance_amount);
        TextView tax = (TextView) findViewById(R.id.tax);
        start_date = (TextView) findViewById(R.id.start_date);
        billId = intent.getIntExtra("bill_id", 0);
        //All bill related data
        allBillData = new BillService().getBill(billId);

        end_date = (TextView) findViewById(R.id.end_date);
        start_date.setText(intent.getStringExtra("start_date"));
        end_date.setText(intent.getStringExtra("end_date"));
        _amount_layout = (TextInputLayout) findViewById(R.id.amount_layout);
        TextView payment_made = (TextView) findViewById(R.id.payment_amount);
        deliveryList = (ListView) findViewById(R.id.deliveryList);
        allDeliveryData = customers.getCustomerDetail(allBillData.getCustomerId(),true).customerSettings;
        adpter =new BillDetailDeliveryAdapter(allDeliveryData,this);
        deliveryList.setAdapter(adpter);
        setListViewHeightBasedOnChildren(deliveryList);
        payment_made.setText("(Rupees) " + String.valueOf(intent.getDoubleExtra("payment_made", 0)));

        Calendar cal = Calendar.getInstance();
        accountService = new AccountService();
        Button send_bill = (Button) findViewById(R.id.send_bill);
        try {
            cal.setTime(Constants._display_format.parse(intent.getStringExtra("end_date")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        send_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountService.getLeftSMS() >= 1)
                    new SendBillSMS().execute();
                else
                    Toast.makeText(BillingEdit.this, "Your SMS quota has expired. Please contact administrator !", Toast.LENGTH_LONG).show();
            }
        });
        start_date.setEnabled(false);
        end_date.setEnabled(false);
        milk_quantity.setText(String.valueOf(intent.getDoubleExtra("quantity", 0)) + " (Litres)");
        milk_quantity.setFocusable(false);
        milk_quantity.setFocusableInTouchMode(false);
        Button clear_bill = (Button) findViewById(R.id.clear_bill);
        payment_text = (TextView) findViewById(R.id.payment_text);
        TextView clera_bill_text = (TextView) findViewById(R.id.clera_bill_text);
        Calendar calendar = Calendar.getInstance();
        try {
            Date date = Constants.work_format.parse(new GlobalSettingsService().getRollDate());
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (cal.get(Calendar.DAY_OF_MONTH) >= calendar.get(Calendar.DAY_OF_MONTH) && cal.get(Calendar.MONTH) >= calendar.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) >= calendar.get(Calendar.YEAR) && intent.getIntExtra("clear", 0) == 1) {
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

        /*If bill is outstanding but not cleared yet*/
        if(allBillData.getIsOutstanding()==1 && allBillData.getCustomerId()==0)
        {
            clear_bill.setEnabled(true);
            clera_bill_text.setVisibility(View.VISIBLE);
            send_bill.setEnabled(true);
            payment_made.setVisibility(View.GONE);
            payment_text.setVisibility(View.GONE);
        }
        /* If bill is already cleared*/
        else if (intent.getIntExtra("clear", 0) == 1) {
            clear_bill.setVisibility(View.GONE);
            clera_bill_text.setVisibility(View.GONE);
            send_bill.setVisibility(View.GONE);
            payment_made.setVisibility(View.VISIBLE);
            payment_text.setVisibility(View.VISIBLE);
//            clera_bill_text.setText("You have made payment of Rupees "+payment_made.getText().toString());
        } else {
            payment_made.setVisibility(View.GONE);
            payment_text.setVisibility(View.GONE);
            clera_bill_text.setVisibility(View.GONE);
            clear_bill.setEnabled(false);
        }


        clear_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(BillingEdit.this);
                final AlertDialog dialog = alertBuilder.create();
                final LayoutInflater inflater = (LayoutInflater) BillingEdit.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                            Bill holder = new Bill();

                            double payment_made = Double.parseDouble(payment.getText().toString());
                            double bill_amount = intent.getDoubleExtra("total", 0);

                            double bill = payment_made - bill_amount;
                            if (bill_amount >= payment_made) {
                                holder.setBalance(bill_amount - payment_made);
                            } else {
                                holder.setBalance(bill);
                            }
                            holder.setPaymentMade(payment_made);
                            holder.setIsCleared(1);

                            holder.setStartDate(intent.getStringExtra("start_date_work_format"));
                            holder.setEndDate(intent.getStringExtra("end_date_work_format"));
                            holder.setTotalAmount(bill_amount);
                            holder.setRate(intent.getDoubleExtra("totalPrice", 0));
                            holder.setQuantity(intent.getDoubleExtra("quantity", 0));
                            holder.setAdjustment(0);
                            holder.setIsOutstanding(intent.getIntExtra("is_outstanding", 0));
                            holder.setCustomerId(intent.getIntExtra("custId", 0));
                            holder.setTax(intent.getDoubleExtra("tax", 0));
                            holder.setDateModified(Constants.getCurrentDate());

                            new BillService().update(holder);
                            dialog.dismiss();
                            Constants.REFRESH_CALANDER = true;
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

        balance_amount.setText("(Rupees) " + String.valueOf(intent.getDoubleExtra("balance", 0)));
        balance_amount.setFocusable(false);
        balance_amount.setFocusableInTouchMode(false);
//        if (balanceType == 1)
//            balance_amount.setHint("Balance due (Rs)");

        TextView total_amount = (TextView) findViewById(R.id.total_amount);
        total_amount.setText("(Rupees) " + String.valueOf(intent.getDoubleExtra("total", 0)));
        tax.setFocusable(false);
        tax.setFocusableInTouchMode(false);
        tax.setText(String.valueOf(new GlobalSettingsService().getData().getTax()) + " %");
        AppUtil.getInstance().getDatabaseHandler().close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_detail_screen);
        intent = this.getIntent();
        setActionBar();
        getview();

    }
    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (adpter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < adpter.getCount(); i++) {
            View listItem = adpter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adpter.getCount() - 1));
        listView.setLayoutParams(params);
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
            final Customers customers = new CustomersService().getCustomerDetail(getIntent().getIntExtra("custId", 0));


            new Thread(new Runnable() {
                public void run() {
                    IBill billService = new BillService();
                    Bill bill = billService.getBill(billId);
                    billService.SmsBill(bill.getId(), BillingEdit.this);

                    progressHandler.post(new Runnable() {
                        public void run() {
                            progress += (100);
                            androidProgressBar.setProgress(progress);
                            //Status update in textview
                            msg = "Sending message... ";
                            publishProgress(msg);
                        }
                    });
                    msg = "Sent message ";
                    publishProgress(msg, "done");
                }
            }).start();

            return msg;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            messageSent.setText(values[0]);
            ok.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

//            progressDialog.cancel();
//            dialog.dismiss();
        }
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

    @Override
    public void onTaskCompleted(String type, HashMap<String, String> listType) {

    }
}
