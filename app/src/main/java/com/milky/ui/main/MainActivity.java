package com.milky.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceclasses.AccountService;
import com.milky.service.databaseutils.serviceclasses.AreaService;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.adapters.AreaCitySpinnerAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Account;
import com.milky.service.core.Area;
import com.milky.service.core.Bill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleteListner {

    private DatabaseHelper _dbHelper;
    private View _headerView;
    protected ArrayList<Area> selectedareacityList;
    public static DrawerLayout mDrawerLayout;
    public static NavigationView mNavigationView;
    private AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      /*
      * init resources
      * */
        initResources();

        Constants.REFRESH_CALANDER = true;
        Constants.REFRESH_CUSTOMERS = true;
        Constants.REFRESH_BILL = true;

        /*
        * Set up ACTIONBAR
        * */
        supportActionBar();

        /**
         * Setup click events on the Navigation View Items.
         */
        _headerView = mNavigationView.inflateHeaderView(R.layout.nav_headers);

        TextView name = (TextView) _headerView.findViewById(R.id.farmer_name);
        Account dataHolder = accountService.getDetails();
        name.setText(String.format("%s %s", dataHolder.getFirstName(), dataHolder.getLastName()));

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_settings:
                        Intent i = new Intent(MainActivity.this, GlobalSetting.class);
                        startActivity(i);
                        break;
                    case R.id.nav_about:
                        startActivity(new Intent(MainActivity.this, About.class));
                        break;
                }
                mDrawerLayout.closeDrawers();

                return true;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
        //To get the current data

    }

    ActionBarDrawerToggle mDrawerToggle;

    List<Area> _areacityList = new ArrayList<>();

    public static String selectedArea = "";
    Menu menu = null;
    View searchView;
    boolean expended = false;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.main_menu, menu);
        this.menu = menu;
        final MenuItem mSpinnerItem2 = menu.findItem(R.id.action_search);
        searchView = mSpinnerItem2.getActionView();
        MenuItemCompat.expandActionView(mSpinnerItem2);

        if (searchView instanceof SearchView) {
            final SearchView actionSearchView = (SearchView) searchView;
            final AutoCompleteTextView editSearch;
            editSearch = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            editSearch.setHintTextColor(ContextCompat.getColor(this, R.color.gray_lighter));
            editSearch.setHint("Type Area Or Customer Name");
            editSearch.clearFocus();
            editSearch.setTextSize(13);
            actionSearchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expended = true;
                }
            });
            editSearch.setThreshold(1);
            AreaCityAdapter adapter1 = new AreaCityAdapter(this, 0, R.id.address, _areacityList);
            editSearch.setAdapter(adapter1);

            editSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editSearch.setText(_areacityList.get(position).getCityArea());
                    Constants.selectedAreaId = _areacityList.get(position).getAreaId();
                    selectedareacityList.add(_areacityList.get(position));
                    editSearch.setSelection(editSearch.getText().length());
                    if (CustomersFragment._mAdapter != null)
                        CustomersFragment._mAdapter.getFilter().filter(editSearch.getText().toString());
                }
            });

            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (CustomersFragment._mAdapter != null)
                        CustomersFragment._mAdapter.getFilter().filter(editSearch.getText().toString());
                    if (s.length() == 0) {
                        Constants.selectedAreaId = -1;
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            actionSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (TextUtils.isEmpty(editSearch.getText().toString())) {

                        /*if (getFragmentRefreshListener() != null) {
                            getFragmentRefreshListener().onRefresh();
                        }*/

                    } else {
                        if (CustomersFragment._mAdapter != null)
                            CustomersFragment._mAdapter.getFilter().filter(editSearch.getText().toString());

                    }
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();

        mNavigationView.removeHeaderView(_headerView);
        _headerView = mNavigationView.inflateHeaderView(R.layout.nav_headers);

        TextView name = (TextView) _headerView.findViewById(R.id.farmer_name);
        Account dataHolder = accountService.getDetails();
        name.setText(String.format("%s %s", dataHolder.getFirstName(), dataHolder.getLastName()));
        /*if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();
        }*/
        //_areaList.clear();
        _areacityList.clear();
        AreaService areaService = new AreaService();
        _areacityList = areaService.getStoredAddresses();

        /*for (int i = 0; i < _areacityList.size(); ++i) {
            _areaList.add(areaService.getAreaById(_areacityList.get(i).getAreaId()));
        }*/
        _dbHelper.close();

        adp1 = new AreaCitySpinnerAdapter(MainActivity.this, R.id.spinnerText
                , _areacityList);
    }


    private void supportActionBar() {
        Toolbar _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        final ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }

    }


    public static int POSITION = 0;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int page = POSITION;
        switch (page) {
            case 0:

                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.bulk_edit).setVisible(false);
                menu.findItem(R.id.sens_sms).setVisible(false);
                menu.findItem(R.id.save).setVisible(false);
                menu.findItem(R.id.refresh_bills).setVisible(false);


                break;
            case 1:
                menu.findItem(R.id.action_search).setVisible(true);
                menu.findItem(R.id.bulk_edit).setVisible(false);
                menu.findItem(R.id.sens_sms).setVisible(false);
                menu.findItem(R.id.save).setVisible(false);
                menu.findItem(R.id.refresh_bills).setVisible(false);

                break;
            case 2:
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.bulk_edit).setVisible(false);
                menu.findItem(R.id.sens_sms).setVisible(true);
                menu.findItem(R.id.save).setVisible(false);
                menu.findItem(R.id.refresh_bills).setVisible(true);

                break;
        }
        return true;
    }

    AreaCitySpinnerAdapter adp1;
    ProgressDialog progressBar;

    private void initResources() {/**
     *Setup the DrawerLayout and NavigationView
     */
        progressBar = new ProgressDialog(MainActivity.this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Checking in ...");
        //progress dialog type
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setVisibility(View.VISIBLE);
        selectedareacityList = new ArrayList<>();
//        MenuItem sync = (MenuItem) mNavigationView.findViewById(R.id.nav_sync);
//        sync.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_image));
        accountService = new AccountService();
        if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
            if (accountService.isAccountExpired()) {
                expiryDialog();
            }
        }

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new MainTabFragment()).commit();


        //check if global setting has been set
        if (0.0 == new GlobalSettingsService().getData().getDefaultRate()) {
            mDrawerLayout.openDrawer(mNavigationView);
            Toast.makeText(MainActivity.this, getResources().getString(R.string.set_global_rate), Toast.LENGTH_SHORT).show();
            _dbHelper.close();
        }
/*
String Area[] = new String[]{"Hadaspar", "Worli", "Baner", "Phase5", "Sector 71"};
int AreaID[] = new int[]{1, 2, 3, 4, 5};
String City[] = new String[]{"Pune", "Mumbai", "Mohali"};
int CityId[] = new int[]{1, 2, 3};

if (!_dbHelper.isTableNotEmpty(TableNames.AREA))
for (int i = 0; i < 5; i++) {
VAreaMapper holder = new VAreaMapper();
holder.setArea(Area[i]);
holder.setAreaId(String.valueOf(AreaID[i]));
if ((i == 0 || i == 2)) {
holder.setCity(City[0]);
holder.setCityId(String.valueOf(CityId[0]));
} else if (i == 1) {
holder.setCity(City[1]);
holder.setCityId(String.valueOf(CityId[1]));
} else {
holder.setCity(City[2]);
holder.setCityId(String.valueOf(CityId[2]));
}

holder.setAccountId(Constants.ACCOUNT_ID);
AreaCityTableManagement.insertAreaDetail(_dbHelper.getWritableDatabase(), holder);
}
_dbHelper.close();
for (int i = 0; i < City.length; i++) {
VAreaMapper holder = new VAreaMapper();
holder.setCityId(String.valueOf(CityId[i]));
holder.setCity(City[i]);
holder.setAccountId(Constants.ACCOUNT_ID);
AreaCityTableManagement.insertCityDetail(_dbHelper.getWritableDatabase(), holder);
}
_dbHelper.close();
*/
    }

    @Override
    public void onTaskCompleted(String type, HashMap<String, String> requestType) {

        if (Constants.TIME_OUT) {
            if (progressBar != null)
                progressBar.hide();
            else if (androidProgressBar != null) {
                androidProgressBar.setMax(100);
                androidProgressBar.setVisibility(View.GONE);
            }
            Constants.TIME_OUT = false;

        }
        /*if (type.equals(ServerApis.ACCOUNT_API)) {
            if (Constants.API_RESPONCE != null) {
//                try {
//                    JSONObject result = Constants.API_RESPONCE;
//                        holder.setFarmerCode(result.getString("FarmerCode"));
//                        holder.setFirstName(result.getString("FirstName"));
//                        holder.setLastName(result.getString("LastName"));
//                        holder.setMobile(result.getString("Mobile"));
//                        holder.setValidated(String.valueOf(result.getBoolean("Validated")));
//                        holder.setDirty(String.valueOf(result.getInt("Dirty")));
//                        holder.setDateAdded(result.getString("DateAdded"));
//                        holder.setDateModified(result.getString("DateModified"));
//                        holder.setAccountStartDate(result.getString("StartDate"));
//                    holder.setExpiryDate(result.getString("EndDate"));
//                    holder.setUsedSms(String.valueOf(result.getInt("UsedSms")));
//                    holder.setTotalSms(String.valueOf(result.getInt("TotalSms")));
//                    holder.setId(String.valueOf(result.getInt("Id")));

//                    Account.updateAllAccountDetails(_dbHelper.getWritableDatabase(), holder);
//                    if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
//                        if (Account.isAccountExpired(_dbHelper.getReadableDatabase())) {
//                            Toast.makeText(MainActivity.this, "Account is expired !", Toast.LENGTH_LONG).show();
//                        } else
//                            expirationDialog.dismiss();
//                    }
//                    progressBar.hide();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }

        } else if (type == ServerApis.SYNC) {
            if (requestType.get("Customer_List").equals("0")) {
//                CustomersTableMagagement.updateSyncedData(_dbHelper.getWritableDatabase());
            } else if (requestType.get("Bill_List").equals("0")) {
//                BillTableManagement.updateSyncedData(_dbHelper.getWritableDatabase());
            }
        }*/
        _dbHelper.close();
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public interface FragmentRefreshListener {
        void onRefresh();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*if (expended)
            if (getFragmentRefreshListener() != null) {
                getFragmentRefreshListener().onRefresh();
            }*/
    }

    //Expiry dialog


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.sens_sms:

                if (!_dbHelper.isTableNotEmpty(TableNames.CUSTOMER)) {
                    Toast.makeText(MainActivity.this, "Please add customer !", Toast.LENGTH_SHORT).show();
                } else if (AppUtil.getInstance().isNetworkAvailable(MainActivity.this)) {
                    IBill billService = new BillService();
                    final List<Bill> bills = billService.getAllGlobalBills(true);

                    if (bills.size() > 0) {
                        if (accountService.getRemainingSMS() >= bills.size())
                            new SendBillSMS(bills).execute();
                        else
                            Toast.makeText(MainActivity.this, "Your SMS quota is not sufficient. Please contact administrator !", Toast.LENGTH_LONG).show();
                    }

                } else
                    Toast.makeText(MainActivity.this, "No network available. ", Toast.LENGTH_SHORT).show();

                break;
            case R.id.refresh_bills:
                if(getFragmentRefreshListener()!=null){
                    Constants.REFRESH_BILL = true;
                    getFragmentRefreshListener().onRefresh();
                }
                break;


        }
        return super.onOptionsItemSelected(item);

    }


    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    private class SendBillSMS extends AsyncTask<Void, String, String> {
        AlertDialog dialog;
        Handler progressHandler = new Handler();
        int progress = 0;
        int messageCount = 0;
        String msg = "";
        List<Bill> bills;

        public SendBillSMS(List<Bill> bills)
        {
            this.bills = bills;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(GlobalSetting.this, "Please wait", "Sending SMS...");
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
            dialog = dialogBuilder.create();
            messageProgress(dialog);
        }

        @Override
        protected String doInBackground(Void... params) {
            new Thread(new Runnable() {
                public void run() {

                    messageCount = 0;
                    for(int i = 0; i< bills.size(); i++) {
                        Bill bill = bills.get(i);
                        IBill billService = new BillService();
                        billService.SmsBill(bill.getId(),MainActivity.this);
                        messageCount = i + 1;

                        progressHandler.post(new Runnable() {
                            public void run() {
                                progress += (100 / bills.size());
                                androidProgressBar.setProgress(progress);
                                //Status update in textview
                                msg = "Sending message: " + messageCount + "/" + bills.size();
                                publishProgress(msg);
                            }
                        });
                        try {
                            Thread.sleep(500);
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    msg = "Sent messages: " + messageCount + "/" + bills.size();
                    publishProgress(msg, "done");
                }
            }).start();

            return msg;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            messageSent.setText(values[0]);
            if( values.length > 1 && values[1] == "done")
            {
                ok.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

//            progressDialog.cancel();
//            dialog.dismiss();
        }
    }

//    //Sens SMS to customers
//    private void SendSmsTouser(String mob, final String sms) {
//
//        String append = "?mobile=" + mob + "&message=" + sms;
//        HttpAsycTask dataTask = new HttpAsycTask();
//        dataTask.runRequest(ServerApis.SMS_API_ROOT + append, null, this, false, null);
//    }

    private TextView messageSent;
    private ProgressBar androidProgressBar;
    private Button ok;

    private void messageProgress(final AlertDialog dialog) {
        final LayoutInflater inflater = (LayoutInflater)
                MainActivity.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
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

    AlertDialog expirationDialog;

    public void expiryDialog() {


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        expirationDialog = dialogBuilder.create();
//                    LayoutInflater inflater =  MainActivity.this(ge);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//                    LayoutInflater inflater =  MainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.account_expire, null, false);
        expirationDialog.setView(dialogView);
        TextView title = (TextView) dialogView.findViewById(R.id.title);
        title.setText("Account Expired");

        expirationDialog.setCancelable(false);
        Button cancelDialog = (Button) dialogView.findViewById(R.id.cancel_button);
        Button continueButton = (Button) dialogView.findViewById(R.id.continue_button);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expirationDialog.dismiss();
                MainActivity.this.finish();
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ExpirationDateUpdate().execute();
            }
        });

        expirationDialog.show();

//            }


    }

    class ExpirationDateUpdate extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            //mehod call to HttpAsyncTask class function with parametrs

            //Creats a jason object for data

//            asyncTask.runRequest(ServerApis.ACCOUNT_API, sendDataToServer(), FarmerSignup.this, true, requestedList);
//            new ServerCommunication().SendHttpPost(ServerApis.ACCOUNT_API, jsonObject);
            HttpAsycTask dataTask = new HttpAsycTask();
            dataTask.runRequest(ServerApis.API_ACCOUNT_ADD, accountService.getJsonData(), MainActivity.this, true, null);
            return null;
        }

        //sign up progress dialog
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBar.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }
}