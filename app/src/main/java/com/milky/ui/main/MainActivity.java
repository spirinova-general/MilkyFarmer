package com.milky.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.AccountAreaMapping;
import com.milky.service.databaseutils.AreaCityTableManagement;
import com.milky.service.databaseutils.AreaMapTableManagement;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.adapters.AreaCitySpinnerAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.UserPrefrences;
import com.milky.viewmodel.VAccount;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VCustomersList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTaskCompleteListner {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Toolbar _mToolbar;
    private DatabaseHelper _dbHelper;
    private View _headerView;
    private ArrayList<VAreaMapper> selectedareacityList = new ArrayList<>();
    public static DrawerLayout mDrawerLayout;
    public static NavigationView mNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
      /*
      * init resources
      * */
        initResources();

        /*
        * Set up ACTIONBAR
        * */
        supportActionBar();

        /**
         * Setup click events on the Navigation View Items.
         */
        _headerView = mNavigationView.inflateHeaderView(R.layout.nav_headers);
//        mNavigationView.addHeaderView(_headerView);

        TextView name = (TextView) _headerView.findViewById(R.id.farmer_name);
        VAccount dataHolder = Account.getFarmerName(_dbHelper.getReadableDatabase());
        name.setText(dataHolder.getFirstName() + " " + dataHolder.getLastName());
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_settings:
                        Intent i = new Intent(MainActivity.this, GlobalSetting.class);
                        startActivity(i);

                        break;
//                    case R.id.nav_sign_out:
//                        SharedPreferences preferences = AppUtil.getInstance().getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);
//                        SharedPreferences.Editor edit = preferences.edit();
//                        edit.clear();
//                        edit.apply();
//                        finish();
//
//                        break;
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

    }

    ActionBarDrawerToggle mDrawerToggle;

    ArrayList<VAreaMapper> _areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    View view1, searchView;
    Spinner spinner;
    int selectedPosition = 0;
    public static String selectedArea = "";
    Menu menu = null;
    boolean expended = false;

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.customers_menu, menu);
        this.menu = menu;
//        final MenuItem mSpinnerItem1 = menu.findItem(R.id.areaSpinner);
        final MenuItem mSpinnerItem2 = menu.findItem(R.id.action_search);

//        view1 = mSpinnerItem1.getActionView();
        searchView = mSpinnerItem2.getActionView();
//        if (view1 instanceof Spinner) {
//            spinner = (Spinner) view1;
//            spinner.setAdapter(adp1);
//            spinner.setSelection(selectedPosition);
//            spinner.setGravity(Gravity.CENTER);
//
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//
//                @Override
//                public void onItemSelected(AdapterView<?> arg0, View arg1,
//                                           int arg2, long arg3) {
//
//                    selectedPosition = arg2;
//                    selectedAreaId = _areacityList.get(arg2).getAreaId();
//                    selectedArea = _areacityList.get(arg2).getArea() + ", " + _areacityList.get(arg2).getCity();
//                    if (getFragmentRefreshListener() != null) {
//                        getFragmentRefreshListener().onRefresh();
//                    }
//
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> arg0) {
//                    // TODO Auto-generated method stub
//
//
//                }
//            });
//
//        }
        MenuItemCompat.expandActionView(mSpinnerItem2);


        if (searchView instanceof SearchView) {
            final SearchView actionSearchView = (SearchView) searchView;
            final AutoCompleteTextView editSearch;
            editSearch = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            editSearch.setHintTextColor(getResources().getColor(R.color.gray_lighter));
            editSearch.setHint("Type Area Or Customer Name");
            editSearch.setTextSize(13);
            actionSearchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expended = true;
//                    mSpinnerItem1.setVisible(false);
                }
            });
            editSearch.setThreshold(1);
            AreaCityAdapter adapter1 = new AreaCityAdapter(this, 0, R.id.te1, _areacityList);
            editSearch.setAdapter(adapter1);

            editSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editSearch.setText(_areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
                    Constants.selectedAreaId = _areacityList.get(position).getAreaId();
                    Constants.selectedCityId = _areacityList.get(position).getCityId();
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
                    if(s.length()==0)
                    {
                        Constants.selectedAreaId = "";
                        Constants.selectedCityId = "";
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

                        if (getFragmentRefreshListener() != null) {
                            getFragmentRefreshListener().onRefresh();
                        }

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

    ArrayList<String> areas;

    @Override
    protected void onResume() {
        super.onResume();

        mNavigationView.removeHeaderView(_headerView);
        _headerView = mNavigationView.inflateHeaderView(R.layout.nav_headers);

        TextView name = (TextView) _headerView.findViewById(R.id.farmer_name);
        VAccount dataHolder = Account.getFarmerName(_dbHelper.getReadableDatabase());
        name.setText(dataHolder.getFirstName() + " " + dataHolder.getLastName());
        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();
        }
        _areaList.clear();
        _areacityList.clear();
        areas = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
        for (int i = 0; i < areas.size(); ++i) {
            _areaList.add(AreaMapTableManagement.getAreabyAreaId(_dbHelper.getReadableDatabase(), areas.get(i)));
        }
        _dbHelper.close();
        VAreaMapper areacity = new VAreaMapper();
        areacity.setArea("");
        areacity.setAreaId("");
        areacity.setCityId("");
        areacity.setCity("");
        areacity.setCityArea("All");
        _areacityList.add(areacity);
        for (int i = 0; i < _areaList.size(); i++) {
            areacity = new VAreaMapper();
            areacity.setArea(_areaList.get(i).getArea());
            areacity.setAreaId(_areaList.get(i).getAreaId());
            areacity.setCityId(_areaList.get(i).getCityId());
            areacity.setCity(AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), _areaList.get(i).getCityId()));
            areacity.setCityArea(areacity.getArea() + ", " + areacity.getCity());
            _areacityList.add(areacity);


        }
        _dbHelper.close();
        adp1 = new AreaCitySpinnerAdapter(MainActivity.this, R.id.spinnerText
                , _areacityList);
        AppUtil.getTotalQuantity();

    }


    private void supportActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        final ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
    }


    public static int POSITION = 0;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int page = POSITION;
        switch (page) {
            case 0:
                menu.findItem(R.id.action_search).setVisible(false);
//                menu.findItem(R.id.areaSpinner).setVisible(false);
                menu.findItem(R.id.save).setVisible(false);


                break;
            case 1:
                menu.findItem(R.id.action_search).setVisible(true);
//                menu.findItem(R.id.areaSpinner).setVisible(true);
                menu.findItem(R.id.save).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.action_search).setVisible(false);
//                menu.findItem(R.id.areaSpinner).setVisible(false);
                menu.findItem(R.id.save).setVisible(false);
                break;
        }
        return true;
    }

    AreaCitySpinnerAdapter adp1;

    private void initResources() {/**
     *Setup the DrawerLayout and NavigationView
     */
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        mNavigationView.setVisibility(View.VISIBLE);
        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView, new MainTabFragment()).commit();


        //check if global setting has been set
        if ("0".equals(Account.getDefaultRate(_dbHelper.getReadableDatabase()))) {
            mDrawerLayout.openDrawer(mNavigationView);
            Toast.makeText(MainActivity.this, getResources().getString(R.string.set_global_rate), Toast.LENGTH_SHORT).show();
            _dbHelper.close();
        }

        String Area[] = new String[]{"Hadaspar", "Worli", "Baner", "Phase5", "Sector 71"};
        int AreaID[] = new int[]{1, 2, 3, 4, 5};
        String City[] = new String[]{"Pune", "Mumbai", "Mohali"};
        int CityId[] = new int[]{1, 2, 3};

        if (!_dbHelper.isTableNotEmpty(TableNames.TABLE_AREA))
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


    }

    private JSONArray jsonDataArray;

    @Override
    public void onTaskCompleted(JSONArray result, String type, HashMap<String, String> requestType) {


        if (ServerApis.STATUS == 1) {
            if (type == ServerApis.SYNC) {
                if (requestType.get("Customer_List").equals("0")) {
                    CustomersTableMagagement.updateSyncedData(_dbHelper.getWritableDatabase());
                } else if (requestType.get("Bill_List").equals("0")) {
                    BillTableManagement.updateSyncedData(_dbHelper.getWritableDatabase());
                }
            }
        } else {


        }
        _dbHelper.close();
    }

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    private FragmentRefreshListener fragmentRefreshListener;

    public interface FragmentRefreshListener {
        void onRefresh();
    }

    public void SyncNow() {

        HttpAsycTask dataTask = new HttpAsycTask();
        dataTask.runRequest(ServerApis.SYNC, getAllDataToSync(), MainActivity.this, true, requestedList);
    }

    public static HashMap<String, String> requestedList = new HashMap<>();

    private List<NameValuePair> getAllDataToSync() {
        JSONArray jsonArray;
        JSONObject jsonObject = new JSONObject();
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            ArrayList<VCustomersList> custList = CustomersTableMagagement.getAllCustomers(_dbHelper.getReadableDatabase());
            if (custList.size() == 0) {

                requestedList.put("Customer_List", "1");

            } else {
                jsonArray = new JSONArray();
                for (int i = 0; i < custList.size(); ++i) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("FirstName", custList.get(i).getFirstName());
                        obj.put("LastName", custList.get(i).getLastName());
                        obj.put("Mobile", custList.get(i).getMobile());
                        obj.put("Address1", custList.get(i).getAddress1());
                        obj.put("Address2", custList.get(i).getAddress2());
                        obj.put("Balance", custList.get(i).getBalance_amount());
                        obj.put("DateAdded", custList.get(i).getDateAdded());
                        obj.put("DateModified", custList.get(i).getDateModified());
                        obj.put("AccountId", "5");
                        obj.put("AreaId", "1");
                        obj.put("Dirty", "0");

                        jsonArray.put(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    jsonObject.put("Customer_List", jsonArray);
                    requestedList.put("Customer_List", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            _dbHelper.close();
        }
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER_BILL)) {
            ArrayList<VCustomersList> billList = BillTableManagement.getCustomersBill(_dbHelper.getReadableDatabase());
            if (billList.size() == 0) {

                requestedList.put("Customer_List", "1");

            } else {
                jsonArray = new JSONArray();
                for (int i = 0; i < billList.size(); ++i) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("CustomerId", billList.get(i).getCustomerId());
                        obj.put("StartDate", billList.get(i).getStart_date());
                        obj.put("EndDate", billList.get(i).getEnd_date());
                        obj.put("Quantity", billList.get(i).getQuantity());
                        obj.put("Balance", "0");
                        obj.put("DateAdded", billList.get(i).getDateAdded());
                        obj.put("DateModified", billList.get(i).getDateModified());
                        obj.put("AccountId", "5");
                        obj.put("AreaId", "1");
                        obj.put("Dirty", "0");
                        obj.put("Adjustment", billList.get(i).getAdjustment());
                        obj.put("TOTAL_AMOUNT", billList.get(i).getBalance_amount());
                        obj.put("TAX", billList.get(i).getTax());
                        obj.put("IsCleared", billList.get(i).getIsCleared());
                        obj.put("PaymentMade", billList.get(i).getPaymentMade());

                        jsonArray.put(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    jsonObject.put("Bill_List", jsonArray);
                    requestedList.put("Bill_List", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            _dbHelper.close();

        }


        List<NameValuePair> nameValuePair = new ArrayList<>();
        if (jsonObject.length() > 0)
            nameValuePair.add(new BasicNameValuePair("", jsonObject.toString()));


        return nameValuePair;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (expended)
            if (getFragmentRefreshListener() != null) {
                getFragmentRefreshListener().onRefresh();
            }
    }
}