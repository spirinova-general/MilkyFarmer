package com.milky.ui.customers;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceclasses.AreaService;
import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.adapters.AreaCitySpinnerAdapter;
import com.milky.ui.adapters.GlobalDeliveryAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Area;
import com.milky.viewmodel.VDelivery;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DeliveryActivity extends AppCompatActivity {
    private ListView _mCustomers;
    private Toolbar _mToolbar;
    public static GlobalDeliveryAdapter _mAdaapter;
    private DatabaseHelper _dbHelper;
    public static List<VDelivery> _mCustomersList;
    public static List<Delivery> _mDeliveryList = new ArrayList<>();
    public static List<VDelivery> selectedCustomersId;
    private DeliveryService deliveryService;
    private String selectedDate="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);
        initResources();
    }

    private void initResources() {
        _mCustomers = (ListView) findViewById(R.id.customersList);
        selectedCustomersId = new ArrayList<>();
        selectedDate =Constants.DELIVERY_DATE;
        setActionBar();
        _mDeliveryList.clear();
        deliveryService = new DeliveryService();
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {

            _mCustomersList = new DeliveryService().getCustomersDelivery(selectedDate);
            _mAdaapter = new GlobalDeliveryAdapter(this, 0, 0, String.valueOf(Constants.SELECTED_DAY), _mCustomersList);
            selectedCustomersId = _mCustomersList;
            _mCustomers.setItemsCanFocus(true);
            _mCustomers.setAdapter(_mAdaapter);
        }

        _dbHelper.close();
        _areaList.clear();
        _areacityList.clear();
//        ArrayList<String> areas = AreaCityTableManagement.getArea(_dbHelper.getReadableDatabase());
//        if (areas != null) {
//            for (int i = 0; i < areas.size(); ++i) {
//                _areaList.add(AreaCityTableManagement.getAreaById(_dbHelper.getReadableDatabase(), areas.get(i)));
//            }
//            VAreaMapper areacity = new VAreaMapper();
////            areacity.setArea("");
////            areacity.setAreaId("");
////            areacity.setCity("");
////            areacity.setCityArea("All");
////            _areacityList.add(areacity);
//            for (int i = 0; i < _areaList.size(); i++) {
//                areacity = new VAreaMapper();
//                areacity.setArea(_areaList.get(i).getArea());
//                areacity.setAreaId(_areaList.get(i).getAreaId());
//                areacity.setCity(_areaList.get(i).getCity());
//                areacity.setLocality(_areaList.get(i).getLocality());
//                areacity.setCityArea(areacity.getArea() + ", " + areacity.getCity());
//                _areacityList.add(areacity);
//
//
//            }
        _areacityList = new AreaService().getStoredAddresses();
        adp1 = new AreaCitySpinnerAdapter(DeliveryActivity.this, R.id.spinnerText, _areacityList);
//        }
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
        Calendar cal = Calendar.getInstance();
        try {
            Date date = Constants.work_format.parse(selectedDate);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        title.setText(Constants.MONTHS[cal.get(Calendar.MONTH)] + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR));
        ImageView deleteCustomer = (ImageView) mCustomView.findViewById(R.id.deleteCustomer);
        deleteCustomer.setVisibility(View.GONE);
        ((LinearLayout) mCustomView.findViewById(R.id.saveManu)).setVisibility(View.GONE);
        _mToolbar.setVisibility(View.VISIBLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(mCustomView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.bulk_edit) {
            if (selectedCustomersId.size() > 0)
                bulkEdit();
            else
                Toast.makeText(DeliveryActivity.this, "No customer is selected !", Toast.LENGTH_SHORT).show();
        }

        if (id == R.id.save) {
            if (_mDeliveryList.size() > 0)
                for (Delivery entry : _mDeliveryList) {
                    if (deliveryService.isHasDataForDay(entry.getDeliveryDate(), entry.getCustomerId()))
                        deliveryService.update(entry);
                    else
                        deliveryService.insert(entry);
                }
            Constants.REFRESH_CALANDER = true;
            Constants.REFRESH_BILL = true;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    List<Area> _areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    View searchView;
    AreaCitySpinnerAdapter adp1;
    boolean expended = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.customer_menu, menu);
        MenuItem mSpinnerItem2 = menu.findItem(R.id.action_search);
        searchView = mSpinnerItem2.getActionView();

        if (searchView instanceof SearchView) {
            final SearchView actionSearchView = (SearchView) searchView;
            final AutoCompleteTextView editSearch;
            editSearch = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            editSearch.setHintTextColor(getResources().getColor(R.color.gray_lighter));
            editSearch.setHint("Type Area Or Customer Name");
            editSearch.setTextSize(13);

            editSearch.setThreshold(1);
            AreaCityAdapter adapter1 = new AreaCityAdapter(this, 0, R.id.address, _areacityList);
            editSearch.setAdapter(adapter1);
            actionSearchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expended = true;
                }
            });
            editSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editSearch.setText(_areacityList.get(position).getCityArea());
                    Constants.selectedAreaId = _areacityList.get(position).getAreaId();
                    editSearch.setSelection(editSearch.getText().length());
                    if (_mAdaapter != null) {
                        _mAdaapter.getFilter().filter(editSearch.getText().toString());
                        _mAdaapter.clear();
                        _mCustomers.setAdapter(_mAdaapter);
                        _mAdaapter.notifyDataSetChanged();
                    }
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
                        Constants.selectedAreaId = 0;
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

    private AlertDialog dialog;

    private void bulkEdit() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(DeliveryActivity.this);
        dialog = alertBuilder.create();
        LayoutInflater inflater = (LayoutInflater) (DeliveryActivity.this).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = inflater.inflate(R.layout.edit_quantity_popup, null, false);
        dialog.setView(view1);

        final EditText quantity = (EditText) view1.findViewById(R.id.milk_quantity);
        quantity.setHint("Quantity");
        final TextView title = (TextView) view1.findViewById(R.id.title);
        title.setText(String.format("%s", "Bulk Edit"));
        final TextInputLayout quantity_layout = (TextInputLayout) view1.findViewById(R.id.quantity_layout);

        ((Button) view1.findViewById(R.id.clear)).setText("Save");
        ((Button) view1.findViewById(R.id.clear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity.getText().toString().equals("")) {
                    quantity_layout.setError("Enter quantity!");
                }
                if (quantity.getText().toString().equals(".")) {
                    quantity_layout.setError(getResources().getString(R.string.enter_valid_quantity));
                } else {
                    if (selectedCustomersId.size() > 0) {
                        for (int i = 0; i < selectedCustomersId.size(); ++i) {
                            Delivery delivery = new Delivery();
                            delivery.setDeliveryDate(selectedDate);
                            delivery.setQuantity(Double.parseDouble(quantity.getText().toString()));
                            delivery.setCustomerId(selectedCustomersId.get(i).getCustomerId());
                            delivery.setDateModified(Constants.getCurrentDate());
                            delivery.setDirty(0);
                            delivery.setSyncStatus(0);
                            if (deliveryService.isHasDataForDay(selectedDate, selectedCustomersId.get(i).getCustomerId()))
                                deliveryService.update(delivery);
                            else
                                deliveryService.insert(delivery);
                        }
                    }
                    dialog.hide();
                    Constants.REFRESH_CALANDER = true;
                    Constants.REFRESH_BILL = true;
                    finish();
                }

            }


        });
        ((Button) view1.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.hide();
            }
        });
            dialog.show();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
