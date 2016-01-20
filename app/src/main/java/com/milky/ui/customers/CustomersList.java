package com.milky.ui.customers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.AccountAreaMapping;
import com.milky.service.databaseutils.AreaMapTableManagement;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.adapters.AreaCitySpinnerAdapter;
import com.milky.ui.adapters.GlobalDeliveryAdapter;
import com.milky.ui.main.CustomersFragment;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VCustomersList;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class CustomersList extends AppCompatActivity {
    private ListView _mCustomers;
    private int _mDay;
    private String _mMonth;
    private Toolbar _mToolbar;
    public static GlobalDeliveryAdapter _mAdaapter;
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

        _bottomLayout = (LinearLayout) findViewById(R.id.bottom_Layout);
        String month = new DateFormatSymbols().getMonths()[(Constants.SELECTED_DAY).getMonth()];
        _mDay = Constants.SELECTED_DAY.getDay();
        _mMonth = month;
        setActionBar();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            _mCustomersList = CustomerSettingTableManagement.getAllCustomersBySelectedDate(_dbHelper.getReadableDatabase(), "");
            _mAdaapter = new GlobalDeliveryAdapter(this, 0,0,String.valueOf(Constants.SELECTED_DAY),_mCustomersList);
            _mCustomers.setItemsCanFocus(true);
            _mCustomers.setAdapter(_mAdaapter);
        }

        _dbHelper.close();

        _areaList.clear();
        _areacityList.clear();

        ArrayList<String> areas = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
        if (areas != null) {
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
            adp1 = new AreaCitySpinnerAdapter(CustomersList.this, R.id.spinnerText
                    , _areacityList);
        }

    }

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);

        title.setText(Constants.DELIVERY_DATE);
        ImageView deleteCustomer = (ImageView) mCustomView.findViewById(R.id.deleteCustomer);
        deleteCustomer.setVisibility(View.GONE);

        _mToolbar.setVisibility(View.VISIBLE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.save) {
            if (_mDeliveryList.size() > 0)
                for (int i = 0; i < _mDeliveryList.size(); i++) {

                    if (DeliveryTableManagement.isHasData(_dbHelper.getReadableDatabase(),
                            _mDeliveryList.get(i).getCustomerId(), _mDeliveryList.get(i).getStart_date()))
                        DeliveryTableManagement.updateCustomerDetail(_dbHelper.getWritableDatabase(), _mDeliveryList.get(i));
                    else
                        DeliveryTableManagement.insertCustomerDetail(_dbHelper.getWritableDatabase(), _mDeliveryList.get(i));
                }
            _dbHelper.close();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {

    }

    ArrayList<VAreaMapper> _areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    View view1, searchView;
    Spinner spinner;
    int selectedPosition = 0;
    public static String selectedArea = "";
    AreaCitySpinnerAdapter adp1;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.customers_menu, menu);
//        MenuItem mSpinnerItem1 = menu.findItem(R.id.areaSpinner);
        MenuItem mSpinnerItem2 = menu.findItem(R.id.action_search);
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
//
//                    if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
//                        if (arg2 == 0) {
//                            _mCustomersList = CustomerSettingTableManagement.getAllCustomersBySelectedDate(_dbHelper.getReadableDatabase(), "");
//                            _mAdaapter = new GlobalDeliveryAdapter(CustomersList.this, String.valueOf(Constants.SELECTED_DAY));
//                            _mCustomers.setItemsCanFocus(true);
//                            _mCustomers.setAdapter(_mAdaapter);
//                        } else {
//                            _mCustomersList = CustomerSettingTableManagement.getAllCustomersBySelectedDate(_dbHelper.getReadableDatabase(), selectedAreaId);
//                            _mAdaapter = new GlobalDeliveryAdapter(CustomersList.this, String.valueOf(Constants.SELECTED_DAY));
//                            _mCustomers.setItemsCanFocus(true);
//                            _mCustomers.setAdapter(_mAdaapter);
//                        }
//                    }
//                    _dbHelper.close();
//
//                }
//                @Override
//                public void onNothingSelected(AdapterView<?> arg0) {
//                    // TODO Auto-generated method stub
//
//
//                }
//            });
//
//        }
        if (searchView instanceof SearchView) {
            final SearchView actionSearchView = (SearchView) searchView;
            final AutoCompleteTextView editSearch;
            editSearch = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            editSearch.setHintTextColor(getResources().getColor(R.color.gray_lighter));
            editSearch.setHint("Type Area Or Customer Name");
            editSearch.setTextSize(13);

            editSearch.setThreshold(1);
            AreaCityAdapter adapter1 = new AreaCityAdapter(this, 0, R.id.te1, _areacityList);
            editSearch.setAdapter(adapter1);

            editSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editSearch.setText(_areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
                    Constants.selectedAreaId = _areacityList.get(position).getAreaId();
                    Constants.selectedCityId = _areacityList.get(position).getCityId();
                    editSearch.setSelection(editSearch.getText().length());
                    if (_mAdaapter != null)
                        _mAdaapter.getFilter().filter(editSearch.getText().toString());
                    _mAdaapter.clear();
                    _mCustomers.setAdapter(_mAdaapter);
                    _mAdaapter.notifyDataSetChanged();
                }
            });

            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (_mAdaapter != null)
                        _mAdaapter.getFilter().filter(editSearch.getText().toString());
                    _mAdaapter.clear();
                    _mCustomers.setAdapter(_mAdaapter);
                    _mAdaapter.notifyDataSetChanged();
                    if (s.length() == 0) {
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

                        if (_mAdaapter != null)
                            _mAdaapter.getFilter().filter(editSearch.getText().toString());

                    _mCustomers.setAdapter(_mAdaapter);
                    _mAdaapter.notifyDataSetChanged();
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
}
