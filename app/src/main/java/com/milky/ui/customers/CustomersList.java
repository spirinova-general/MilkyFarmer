package com.milky.ui.customers;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.milky.R;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.GlobalDeliveryAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;
import com.milky.viewmodel.VDelivery;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomersList extends AppCompatActivity {
    private ListView _mCustomers;
    private int _mDay;
    private String _mMonth;
    private Toolbar _mToolbar;
    private GlobalDeliveryAdapter _mAdaapter;
    private Button _save, _cancel;
    private DatabaseHelper _dbHelper;
    private LinearLayout _bottomLayout;
    public static List<VCustomersList> _mCustomersList, _mDeliveryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);
        initResources();
    }

    private void initResources() {
        _mCustomers = (ListView) findViewById(R.id.customersList);
        _save = (Button) findViewById(R.id.save);
        _cancel = (Button) findViewById(R.id.cancel);
        _bottomLayout = (LinearLayout) findViewById(R.id.bottom_Layout);
        String month = new DateFormatSymbols().getMonths()[(Constants.SELECTED_DAY).getMonth()];
        _mDay = Constants.SELECTED_DAY.getDay();
        _mMonth = month;
        setActionBar();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            _mCustomersList = CustomersTableMagagement.getAllCustomersBySelectedDate(_dbHelper.getReadableDatabase());
            _mAdaapter = new GlobalDeliveryAdapter(this, String.valueOf(Constants.SELECTED_DAY));
            _mCustomers.setItemsCanFocus(true);
            _mCustomers.setAdapter(_mAdaapter);
        }
        if ((_mCustomersList == null || _mCustomersList.size() == 0))
            _bottomLayout.setVisibility(View.GONE);
        else
            _bottomLayout.setVisibility(View.VISIBLE);
        _dbHelper.close();

    }

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(_mToolbar);
        _mToolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(String.valueOf(_mDay) + " " + _mMonth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.customers_menu, menu);
        menu.findItem(R.id.action_add_bill).setVisible(false);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                if (_mDeliveryList.size() > 0)
                    for (int i = 0; i < _mDeliveryList.size(); i++) {

                        if (DeliveryTableManagement.isHasData(_dbHelper.getReadableDatabase(),
                                _mDeliveryList.get(i).getCustomerId(), _mDeliveryList.get(i).getDeliverydate()))
                            DeliveryTableManagement.updateCustomerDetail(_dbHelper.getWritableDatabase(), _mDeliveryList.get(i));
                        else
                            DeliveryTableManagement.insertCustomerDetail(_dbHelper.getWritableDatabase(), _mDeliveryList.get(i));
                    }
                finish();
                break;

            case R.id.cancel:
                finish();
                break;
        }
    }
}
