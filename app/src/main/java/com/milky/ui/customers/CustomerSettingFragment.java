package com.milky.ui.customers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.core.Account;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceclasses.AreaService;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceclasses.CustomersService;
import com.milky.service.databaseutils.serviceclasses.CustomersSettingService;
import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.service.databaseutils.serviceinterface.ICustomersSettings;
import com.milky.service.databaseutils.serviceinterface.IDelivery;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.adapters.BillingAdapter;
import com.milky.ui.main.CustomersActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.EnableEditableFields;
import com.milky.utils.TextValidationMessage;
import com.milky.service.core.Area;
import com.milky.service.core.Bill;
import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by Neha on 11/19/2015.
 */
public class CustomerSettingFragment extends Fragment {
    private EditText _mFirstName, _mLastName, _mAddress1, _mBalance, _mQuantuty, _mMobile, _mRate;
    private InputMethodManager inputMethodManager;
    private Button _mSave, _mCancel;
    private TextInputLayout _phone_textinput_layout;
    private AutoCompleteTextView _autocomplete_city_area;
    private int selectedAreaId = -1, tempAreaId = -1;
    private TextInputLayout name_layout, last_name_layout, balance_layout, flat_number_layout, street_layout, milk_quantity_layout, rate_layout;
    private Customers dataHolder;
    private DatabaseHelper _dbHelper;
    private String itemName;
    private String[] autoCompleteData;
    private String previousSelectedArea = "";
    private String[] mData;
    private List<Area> areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    private Calendar c;
    private TextView pick_date;
    private int custId = -1;
    private boolean updatedQty = false, updateRate = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        View view = inflater.inflate(R.layout.activity_customers_setting, container, false);
        inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        /** initialize all resources
         * */
        initResources(view);

        //throw new IllegalStateException("something");
       /*  * Set text field listeners*/
        _mAddress1.addTextChangedListener(new TextValidationMessage(_mAddress1, flat_number_layout, getActivity(), false, false, false, false, false));
        _mFirstName.addTextChangedListener(new TextValidationMessage(_mAddress1, name_layout, getActivity(), false, false, false, false, false));
        _mLastName.addTextChangedListener(new TextValidationMessage(_mAddress1, last_name_layout, getActivity(), false, false, false, false, false));

        _mBalance.addTextChangedListener(new TextValidationMessage(_mAddress1, balance_layout, getActivity(), false, false, false, true, false));

        _mMobile.addTextChangedListener(new TextValidationMessage(_mMobile, _phone_textinput_layout, getActivity(), true, false, false, false, false));

        _mQuantuty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().equals("."))
                        milk_quantity_layout.setError(getResources().getString(R.string.enter_valid_quantity));
                    else
                        milk_quantity_layout.setError(null);
                    updatedQty = true;


                } else {
                    milk_quantity_layout.setError(getResources().getString(R.string.field_cant_empty));
                    updatedQty = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (s.toString().equals("."))
                        milk_quantity_layout.setError(getResources().getString(R.string.enter_valid_quantity));
                    else
                        milk_quantity_layout.setError(null);
                    updatedQty = true;

                } else {
                    milk_quantity_layout.setError(getResources().getString(R.string.field_cant_empty));
                    updatedQty = false;
                }
            }
        });
        _mRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (s.toString().equals("."))
                        rate_layout.setError(getResources().getString(R.string.enter_valid_rate));
                    else
                        rate_layout.setError(null);
                    updateRate = true;
                } else {
                    rate_layout.setError(getResources().getString(R.string.field_cant_empty));
                    updateRate = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {

                    if (s.toString().equals("."))
                        rate_layout.setError(getResources().getString(R.string.enter_valid_rate));
                    else
                        rate_layout.setError(null);
                    updateRate = true;


                } else {
                    rate_layout.setError(getResources().getString(R.string.field_cant_empty));
                    updateRate = false;
                }
            }
        });

        /* ---------------------------------------------------*/


        return view;

    }


    private void initResources(View view)  {
        //try

        _mFirstName = (EditText) view.findViewById(R.id.first_name);
        _mLastName = (EditText) view.findViewById(R.id.last_name);
        _mRate = (EditText) view.findViewById(R.id.rate);
        _mAddress1 = (EditText) view.findViewById(R.id.flat_number);
        _mMobile = (EditText) view.findViewById(R.id.phone);
        // et_phone=(FormEditText)view.findViewById(R.id.et_phone);
        _mQuantuty = (EditText) view.findViewById(R.id.milk_quantity);
//        _mAddress2 = (EditText) view.findViewById(R.id.street);
        _mBalance = (EditText) view.findViewById(R.id.balance);
        _autocomplete_city_area = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_city_area);
//        _autocomplete_city_area.setFocusable(false);
//        _autocomplete_city_area.setFocusableInTouchMode(false);
        _mSave = (Button) view.findViewById(R.id.save);
        _mCancel = (Button) view.findViewById(R.id.cancel);
//        _mEdit = (FloatingActionButton) view.findViewById(R.id.editFab);
//        _mEdit.setVisibility(View.VISIBLE);
        _phone_textinput_layout = (TextInputLayout) view.findViewById(R.id.phone_textinput_layout);
        name_layout = (TextInputLayout) view.findViewById(R.id.name_layout);
        last_name_layout = (TextInputLayout) view.findViewById(R.id.last_name_layout);
        street_layout = (TextInputLayout) view.findViewById(R.id.street_layout);
        balance_layout = (TextInputLayout) view.findViewById(R.id.balance_layout);
        flat_number_layout = (TextInputLayout) view.findViewById(R.id.flat_number_layout);
        milk_quantity_layout = (TextInputLayout) view.findViewById(R.id.milk_quantity_layout);
        rate_layout = (TextInputLayout) view.findViewById(R.id.rate_layout);
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        pick_date = (TextView) view.findViewById(R.id.pick_date);
        custId = getActivity().getIntent().getIntExtra("cust_id", 0);

        pick_date.setBackgroundColor(getResources().getColor(R.color.gray_lighter));
        pick_date.setEnabled(false);

        ICustomers customersService = new CustomersService();
        Customers customer = customersService.getCustomerDetail(custId, true);
        CustomersSetting settingData = null;

        Calendar cal = Calendar.getInstance();
        Date today = Utils.GetDateWithoutTime(cal.getTime());
        Date startDate = Utils.FromDateString(customer.getStartDate());
        Date settingDate = null;
        if (Utils.BeforeOrEqualsDate(today, startDate)) {
            settingDate = startDate;
        } else {
            settingDate = today;
        }
        try {
            settingData = customersService.getCustomerSetting(customer, settingDate, false, true);
        }
        catch (Exception ex)
        {
            //All exception handling has to be taken care of later,
        }
        startDate = Utils.FromDateString(customer.getStartDate());
        cal.setTime(startDate);
        pick_date.setText(String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + "-" + Constants.MONTHS[cal.get(Calendar.MONTH)] + "-"
                + String.valueOf(cal.get(Calendar.YEAR)));

        _mMobile.setText(getActivity().getIntent().getStringExtra("mobile"));
        _mQuantuty.setText(String.valueOf(settingData.getGetDefaultQuantity()));
        _mFirstName.setText(getActivity().getIntent().getStringExtra("fname"));
        _mLastName.setText(getActivity().getIntent().getStringExtra("lname"));
        _mBalance.setText(String.valueOf(getActivity().getIntent().getDoubleExtra("balance", 0)));
        _mAddress1.setText(getActivity().getIntent().getStringExtra("address1"));
        _mRate.setText(String.valueOf(settingData.getDefaultRate()));
        Area area = new AreaService().getAreaById(getActivity().getIntent().getIntExtra("areaId", 0));
        if (!area.getLocality().equals(""))
            _autocomplete_city_area.setText(area.getLocality() + ", " + area.getArea() + ", " + area.getCity());
        else
            _autocomplete_city_area.setText(area.getArea() +
                    ", " + area.getCity());

        previousSelectedArea = _autocomplete_city_area.getText().toString();
        _mFirstName.setSelection(getActivity().getIntent().getStringExtra("fname").length());
        _mLastName.setSelection(getActivity().getIntent().getStringExtra("lname").length());
        _mRate.setSelection(String.valueOf(settingData.getDefaultRate()).length());
        _mMobile.setSelection(getActivity().getIntent().getStringExtra("mobile").length());

        _mBalance.setSelection(String.valueOf(getActivity().getIntent().getDoubleExtra("balance", 0)).length());
        _mAddress1.setSelection(getActivity().getIntent().getStringExtra("address1").length());
        //_mAddress2.setSelection(getActivity().getIntent().getStringExtra("address2").length());
        _autocomplete_city_area.setSelection(_autocomplete_city_area.getText().length());
        _mQuantuty.setSelection(String.valueOf(settingData.getGetDefaultQuantity()).length());

        _areacityList = new AreaService().getStoredAddresses();
        AreaCityAdapter adapter1 = new AreaCityAdapter(getActivity(), 0, R.id.address, _areacityList);
        _autocomplete_city_area.setAdapter(adapter1);
        _autocomplete_city_area.setSelection(_autocomplete_city_area.getText().length());
            // final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, autoCompleteData);

     /*   _autocomplete_city_area.addTextChangedListener(new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            adapter1.getFilter().filter(arg0);
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
        int arg3) {
            // TODO Auto-generated method stub

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub

        }
    });*/

/*
/*        String data = areaList.getItem(position).split(" ");*//*
        int index = Arrays.asList(mData).indexOf(data);*/
        //  _autocomplete_city_area.setAdapter(adapter);
        //setting the adapter data into the AutoCompleteTextView
        selectedAreaId = getActivity().getIntent().getIntExtra("areaId", 0);

        _autocomplete_city_area.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (!_areacityList.get(position).getLocality().equals(""))
//                    _autocomplete_city_area.setText(_areacityList.get(position).getLocality() + ", " + _areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
//                else
//                    _autocomplete_city_area.setText(_areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
                _autocomplete_city_area.setText(_areacityList.get(position).getCityArea());
                //  _autocomplete_city_area.append(_areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
                selectedAreaId = _areacityList.get(position).getAreaId();
                tempAreaId = _areacityList.get(position).getAreaId();
                _autocomplete_city_area.setSelection(_autocomplete_city_area.getText().length());
                // int selection = parent.getSelectedItemPosition();

             /*   for (int i = 0; i < areaList.size(); i++) {
                    if (areaList.get(i).getArea().equals(_autocomplete_city_area.getText().toString())) {
                     //int index = areaList.indexOf(_autocomplete_city_area.getText().toString());
                        selectedAreaId = areaList.get(i).getAreaId();
                        selectedCityId = areaList.get(i).getCityId();
                    }
                    else{
                        Toast.makeText(getActivity(),"Sorry i cant find it",Toast.LENGTH_SHORT).show();
                    }
                }*/
              /*  for(int i=0; i < areaList.size(); i++)
                    if(areaList[i].contains(_autocomplete_city_area.getText().toString()))
                        aPosition = i;*/
                // Toast.makeText(getActivity(),"area is"+_autocomplete_city_area.getText().toString(),Toast.LENGTH_SHORT).show();
                /*if(index>=0) {
                    selectedAreaId = areaList.get(index).getAreaId();
                    selectedCityId = areaList.get(index).getCityId();
                    }*/
            }
        });
//        disableKeyBoard();

        _mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //disable edit mode
//                EnableEditableFields.setIsEnabled(false);
//                disableKeyBoard();
//                _mEdit.setVisibility(View.VISIBLE);
//                _mBottomLayout.setVisibility(View.GONE);

//                disableKeyBoard();
//                _autocomplete_city_area.setFocusable(false);
//                _autocomplete_city_area.setFocusableInTouchMode(false);
                //Clear focus from first field
//                _mFirstName.clearFocus();
//                _mLastName.clearFocus();
//                _mBalance.clearFocus();
//                _mQuantuty.clearFocus();
//                _mMobile.clearFocus();
//                _mAddress1.clearFocus();
//                _mRate.clearFocus();
//                _autocomplete_city_area.clearFocus();
//                _mAddress2.clearFocus();

            }
        });
//        _mEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                _mFirstName.requestFocus();
//                inputMethodManager.showSoftInput(_mFirstName, InputMethodManager.SHOW_IMPLICIT);
//                _mBottomLayout.setVisibility(View.VISIBLE);
//                _mEdit.setVisibility(View.GONE);
//                _autocomplete_city_area.setFocusable(true);
//                _autocomplete_city_area.setFocusableInTouchMode(true);
//                _autocomplete_city_area.setEnabled(true);
//                _mFirstName.setSelection(_mFirstName.getText().length());
//                EnableEditableFields.setIsEnabled(true);
//            }
//        });
        final TextInputLayout autocomplete_layout = (TextInputLayout) view.findViewById(R.id.autocomplete_layout);
        /*
        * Save button onclick Event
        * */
        CustomersActivity.saveManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_mFirstName.getText().toString().equals(""))
                    name_layout.setError("Enter name!");
                else if (_mLastName.getText().toString().equals(""))
                    last_name_layout.setError("Enter name!");
                else if (_mRate.getText().toString().equals(""))
                    rate_layout.setError("Enter amount!");
                else if (_mRate.getText().toString().equals("."))
                    rate_layout.setError(getResources().getString(R.string.enter_valid_rate));
                else if (_mBalance.getText().toString().equals(""))
                    balance_layout.setError("Enter balance amount");
                else if (_mQuantuty.getText().toString().equals("."))
                    milk_quantity_layout.setError(getResources().getString(R.string.enter_valid_quantity));
                else if (_mBalance.getText().toString().equals("."))
                    balance_layout.setError(getResources().getString(R.string.enter_valid_balance));
                else if (_mAddress1.getText().toString().equals(""))
                    flat_number_layout.setError("Enter flat number!");

                else if ((!previousSelectedArea.equals(_autocomplete_city_area.getText().toString()) && tempAreaId == -1)) {

                    autocomplete_layout.setError("Select valid area!");


                } else if (_mMobile.getText().toString().equals(""))
                    _phone_textinput_layout.setError("Enter mobile number!");
                else if (_mQuantuty.getText().toString().equals(""))
                    milk_quantity_layout.setError("Enter milk quantity!");

                else if (!_mFirstName.getText().toString().equals("")
                        && !_mLastName.getText().toString().equals("") &&
                        !_mBalance.getText().toString().equals("") &&
                        !_mAddress1.getText().toString().equals("")
                        && !_mRate.getText().toString().equals("")
                        && selectedAreaId != -1
                        && !_mMobile.getText().toString().equals("") &&
                        !_mQuantuty.getText().toString().equals("")
                        ) {
                    ICustomers service = new CustomersService();
                    Customers holder = service.getCustomerDetail(getActivity().getIntent().getIntExtra("cust_id", 0));
                    holder.setFirstName(_mFirstName.getText().toString());
                    holder.setLastName(_mLastName.getText().toString());
                    holder.setBalance_amount(Double.parseDouble(_mBalance.getText().toString()));
                    holder.setAddress1(_mAddress1.getText().toString());
                    holder.setAreaId(selectedAreaId);
                    holder.setMobile(_mMobile.getText().toString());
                    holder.setDateAdded(getActivity().getIntent().getStringExtra("added_date"));
                    String currentDate = Constants.getCurrentDate();
                    holder.setDateModified(currentDate);
                    holder.setIsDeleted(0);
                    holder.setDirty(1);
                    holder.setDeletedOn("null");
                    //Update data into custmers table
                    //service.update(holder);

                    CustomersSetting setting = new CustomersSetting();
                    setting.setCustomerId(holder.getCustomerId());
//                    setting.setStartDate(holder.getStartDate());
                    setting.setStartDate(currentDate);
                    setting.setGetDefaultQuantity(Double.parseDouble(_mQuantuty.getText().toString()));
                    setting.setDefaultRate(Double.parseDouble(_mRate.getText().toString()));
                    //setting.setStartDate(new CustomersSettingService().getByCustId(custId, currentDate).getStartDate());

                    setting.setEndDate(Utils.ToDateString(Utils.GetMaxDate()));


                    setting.setDirty(1);
                    setting.setDateModified(currentDate);
                    setting.setIsDeleted(0);


                    SaveCustomer task = new SaveCustomer(getActivity(), holder, setting);
                    task.execute();
                    /*deliverService.insertOrUpdateCustomerSetting(setting);



                    Toast.makeText(getActivity(), "Customer edited successfully !", Toast.LENGTH_SHORT).show();
                    Constants.REFRESH_CALANDER = true;
                    Constants.REFRESH_CUSTOMERS = true;
                    Constants.REFRESH_BILL = true;
                    getActivity().finish();*/

                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.fill_require_fields), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }


    private void disableKeyBoard() {
        //Block default keyboard

        new EnableEditableFields(_mFirstName, getActivity(), inputMethodManager).blockDefaultKeys();
        new EnableEditableFields(_mLastName, getActivity(), inputMethodManager).blockDefaultKeys();
        // _autocomplete_city_area.setEnabled(false);
        new EnableEditableFields(_mRate, getActivity(), inputMethodManager).blockDefaultKeys();
        new EnableEditableFields(_mQuantuty, getActivity(), inputMethodManager).blockDefaultKeys();
        new EnableEditableFields(_mBalance, getActivity(), inputMethodManager).blockDefaultKeys();
        new EnableEditableFields(_mMobile, getActivity(), inputMethodManager).blockDefaultKeys();
        new EnableEditableFields(_mAddress1, getActivity(), inputMethodManager).blockDefaultKeys();
//        new EnableEditableFields(_mAddress2, getActivity(), inputMethodManager).blockDefaultKeys();
    }



    private class SaveCustomer extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        Customers customer;
        CustomersSetting customersSetting;
        Activity activity;
        public SaveCustomer(Activity activity, Customers customer, CustomersSetting setting){
        this.customer = customer;
        this.customersSetting = setting;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(activity, "", "Saving Customer...", false, true);

        }

        @Override
        protected Void doInBackground(Void... params) {
            //billService.RecalculateAllCurrentBills();
            //bills = billService.getAllGlobalBills(_reCalculateBills);
            ICustomers customersService = new CustomersService();
            customersService.update(customer);
            IDelivery deliverService = new DeliveryService();
            deliverService.insertOrUpdateCustomerSetting(customersSetting);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

            Toast.makeText(activity, "Customer edited successfully !", Toast.LENGTH_SHORT).show();
            Constants.REFRESH_CALANDER = true;
            Constants.REFRESH_CUSTOMERS = true;
            Constants.REFRESH_BILL = true;
          activity.finish();
        }
    }
}