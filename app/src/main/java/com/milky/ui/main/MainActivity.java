package com.milky.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.milky.ui.adapters.AreaCitySpinnerAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
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

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_settings:
                        Intent i = new Intent(MainActivity.this, GlobalSetting.class);
                        startActivity(i);

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
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

    }

    ArrayList<VAreaMapper> _areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    View view1, searchView;
    Spinner spinner;
    int selectedPosition = 0;
    public static String selectedAreaId = "";
    public static String selectedArea = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.customers_menu, menu);
        MenuItem mSpinnerItem1 = menu.findItem(R.id.areaSpinner);
        MenuItem mSpinnerItem2 = menu.findItem(R.id.action_search);
        view1 = mSpinnerItem1.getActionView();
        searchView = mSpinnerItem2.getActionView();
        if (view1 instanceof Spinner) {
            spinner = (Spinner) view1;
            spinner.setAdapter(adp1);
            spinner.setSelection(selectedPosition);
            spinner.setGravity(Gravity.CENTER);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {

                    selectedPosition = arg2;
                    selectedAreaId = _areacityList.get(arg2).getAreaId();
                    selectedArea = _areacityList.get(arg2).getArea() + ", " + _areacityList.get(arg2).getCity();
                    if (getFragmentRefreshListener() != null) {
                        getFragmentRefreshListener().onRefresh();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub


                }
            });

        }
        if (searchView instanceof SearchView) {
            SearchView actionSearchView = (SearchView) searchView;
            final EditText editSearch;
            actionSearchView.setIconifiedByDefault(false);
            editSearch = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            editSearch.setHintTextColor(getResources().getColor(R.color.white));
            actionSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    if (TextUtils.isEmpty(editSearch.getText().toString())) {

                        if (getFragmentRefreshListener() != null) {
                            getFragmentRefreshListener().onRefresh();
                        }

                    } else {
                        if (getFragmentRefreshListener() != null) {
                            getFragmentRefreshListener().onRefresh();
                        }
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
        if (getFragmentRefreshListener() != null) {
            getFragmentRefreshListener().onRefresh();
        }
        _areaList.clear();
        _areacityList.clear();
        areas = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
        for (int i = 0; i < areas.size(); ++i) {
            _areaList.add(AreaMapTableManagement.getAreabyAreaId(_dbHelper.getReadableDatabase(), areas.get(i)));
        }
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
                menu.findItem(R.id.areaSpinner).setVisible(false);
                menu.findItem(R.id.save).setVisible(false);


                break;
            case 1:
                menu.findItem(R.id.action_search).setVisible(true);
                menu.findItem(R.id.areaSpinner).setVisible(true);
                menu.findItem(R.id.save).setVisible(false);
                break;
            case 2:
                menu.findItem(R.id.action_search).setVisible(false);
                menu.findItem(R.id.areaSpinner).setVisible(false);
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
        }

        String Area[] = new String[]{"Hadaspar", "Worli", "Baner", "Phase5", "Sector 71"};
        int AreaID[] = new int[]{1, 2, 3, 4, 5};
        String City[] = new String[]{"Pune", "Mumbai", "Mohali"};
        int CityId[] = new int[]{1, 2, 3};

        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
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
        for (int i = 0; i < City.length; i++) {
            VAreaMapper holder = new VAreaMapper();
            holder.setCityId(String.valueOf(CityId[i]));
            holder.setCity(City[i]);
            holder.setAccountId(Constants.ACCOUNT_ID);
            AreaCityTableManagement.insertCityDetail(_dbHelper.getWritableDatabase(), holder);
        }


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


        }


        List<NameValuePair> nameValuePair = new ArrayList<>();
        if (jsonObject.length() > 0)
            nameValuePair.add(new BasicNameValuePair("", jsonObject.toString()));


        return nameValuePair;
    }

}