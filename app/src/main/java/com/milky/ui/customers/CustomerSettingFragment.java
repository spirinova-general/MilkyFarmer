package com.milky.ui.customers;

import android.content.Context;
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
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.AreaCityTableManagement;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.GlobalSettingsService;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.main.CustomersActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.EnableEditableFields;
import com.milky.utils.TextValidationMessage;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VCustomers;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Neha on 11/19/2015.
 */
public class CustomerSettingFragment extends Fragment {
    private EditText _mFirstName, _mLastName, _mAddress1, _mBalance, _mQuantuty, _mMobile, _mRate;
    private InputMethodManager inputMethodManager;
    private Button _mSave, _mCancel;
    private int dataCount = 0;
    private TextInputLayout _phone_textinput_layout;
    private AutoCompleteTextView _autocomplete_city_area;
    private int selectedAreaId = 0, tempAreaId = 0;
    private TextInputLayout name_layout, last_name_layout, balance_layout, flat_number_layout, street_layout, milk_quantity_layout, rate_layout;
    private VCustomers dataHolder;
    private DatabaseHelper _dbHelper;
    private String itemName;
    private String[] autoCompleteData;
    private String previousSelectedArea = "";
    private String[] mData;
    private ArrayList<VAreaMapper> areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    private Calendar c;
    private TextView pick_date;
    private String custId = "";
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


    private void initResources(View view) {
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
        pick_date.setText(getActivity().getIntent().getStringExtra("delivery_date"));
        pick_date.setBackgroundColor(getResources().getColor(R.color.gray_lighter));
        pick_date.setEnabled(false);

        /*Set defaul rate
        * */
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT))
            _mRate.setText(GlobalSettingsService.getDefaultRate(_dbHelper.getReadableDatabase()));

        /*
        * If to edit customer details
        * */

        _mMobile.setText(getActivity().getIntent().getStringExtra("mobile"));
        _mQuantuty.setText(getActivity().getIntent().getStringExtra("quantity"));
        _mFirstName.setText(getActivity().getIntent().getStringExtra("fname"));
        _mLastName.setText(getActivity().getIntent().getStringExtra("lname"));
        _mBalance.setText(getActivity().getIntent().getStringExtra("balance"));
//        _mAddress2.setText(getActivity().getIntent().getStringExtra("address2"));
        _mAddress1.setText(getActivity().getIntent().getStringExtra("address1"));
        _mRate.setText(getActivity().getIntent().getStringExtra("defaultrate"));
        if (!AreaCityTableManagement.getLocalityById(_dbHelper.getReadableDatabase(), getActivity().getIntent().getIntExtra("areaId",0)).equals(""))
            _autocomplete_city_area.setText(AreaCityTableManagement.getLocalityById(_dbHelper.getReadableDatabase(), getActivity().getIntent().getIntExtra("areaId",0))
                    + ", " +
                    AreaCityTableManagement.getAreaNameById(_dbHelper.getReadableDatabase(), getActivity().getIntent().getIntExtra("areaId",0)) +
                    ", " + AreaCityTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), getActivity().getIntent().getIntExtra("areaId",0)));
        else
            _autocomplete_city_area.setText(AreaCityTableManagement.getAreaNameById(_dbHelper.getReadableDatabase(), getActivity().getIntent().getIntExtra("areaId",0)) +
                    ", " + AreaCityTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), getActivity().getIntent().getIntExtra("areaId",0)));

        previousSelectedArea = _autocomplete_city_area.getText().toString();
        _mFirstName.setSelection(getActivity().getIntent().getStringExtra("fname").length());
        _mLastName.setSelection(getActivity().getIntent().getStringExtra("lname").length());
        _mRate.setSelection(String.valueOf(getActivity().getIntent().getDoubleExtra("defaultrate", 0)).length());
        _mMobile.setSelection(getActivity().getIntent().getStringExtra("mobile").length());

        _mBalance.setSelection(String.valueOf(getActivity().getIntent().getDoubleExtra("balance", 0)).length());
        _mAddress1.setSelection(getActivity().getIntent().getStringExtra("address1").length());
//        _mAddress2.setSelection(getActivity().getIntent().getStringExtra("address2").length());
        _autocomplete_city_area.setSelection(_autocomplete_city_area.getText().length());
        _mQuantuty.setSelection(String.valueOf(getActivity().getIntent().getDoubleExtra("quantity",0)).length());
        custId = getActivity().getIntent().getStringExtra("cust_id");

        //        areaList = AreaMapTableManagement.getAreaById(_dbHelper.getReadableDatabase(), Constants.ACCOUNT_ID);
//        ArrayList<String> areasId = AreaCityTableManagement.getArea(_dbHelper.getReadableDatabase());
//        for (int i = 0; i < areasId.size(); ++i) {
//            areaList.add(AreaCityTableManagement.getAreaById(_dbHelper.getReadableDatabase(), areasId.get(i)));
//        }
//        autoCompleteData = new String[areaList.size()];
////        for (int i = 0; i < areaList.size(); i++) {
////            // Get City for area
////            // autoCompleteData[i] = AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), _areaList.get(i).getArea()) + " " + _areaList.get(i).getCity();
////            autoCompleteData[i] = areaList.get(i).getArea()+" "+AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(),areaList.get(i).getCityId());
////        }
//        /*adapter1 = new AreaCityAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line, R.id.te1, areaList);
//        _autocomplete_city_area.setAdapter(adapter1);*/
//        for (int i = 0; i < areaList.size(); i++) {
//            VAreaMapper areacity = new VAreaMapper();
//            areacity.setArea(areaList.get(i).getArea());
//            areacity.setAreaId(areaList.get(i).getAreaId());
//            areacity.setLocality(areaList.get(i).getLocality());
//            areacity.setCity(areaList.get(i).getCity());
//            if(areacity.getLocality().equals(""))
//                areacity.setCityArea(areacity.getArea() + ", " + areacity.getCity());
//            else
//            areacity.setCityArea(areaList.get(i).getLocality()+", " + areacity.getArea() + ", " + areacity.getCity());
//            _areacityList.add(areacity);
//        }
        _areacityList = AreaCityTableManagement.getFullAddress(_dbHelper.getReadableDatabase());
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
        selectedAreaId = getActivity().getIntent().getIntExtra("areaId",0);

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

                else if ((!previousSelectedArea.equals(_autocomplete_city_area.getText().toString()) && tempAreaId==0)) {

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
                        && selectedAreaId!=0
                        && !_mMobile.getText().toString().equals("") &&
                        !_mQuantuty.getText().toString().equals("")
                        ) {
                    VCustomers holder = new VCustomers();
                    holder.setFirstName(_mFirstName.getText().toString());
                    holder.setLastName(_mLastName.getText().toString());
                    holder.setBalance_amount(Double.parseDouble(_mBalance.getText().toString()));
                    holder.setAddress1(_mAddress1.getText().toString());
//                    holder.setAddress2(_mAddress2.getText().toString());

                    holder.setAreaId(selectedAreaId);
                    holder.setMobile(_mMobile.getText().toString());
                    holder.setGetDefaultQuantity(Double.parseDouble(_mQuantuty.getText().toString()));
                    holder.setCustomerId(getActivity().getIntent().getIntExtra("cust_id",0));
                    holder.setDefaultRate(Double.parseDouble(_mRate.getText().toString()));
                    holder.setDateAdded(getActivity().getIntent().getStringExtra("added_date"));
                    holder.setStartDate(getActivity().getIntent().getStringExtra("start_delivery_date"));
                    String currentDate = Constants.getCurrentDate();
//                    holder.ba("1");
                    holder.setDateModified(currentDate);
                    Calendar c = Calendar.getInstance();
//                    holder.setEnd_date(2250 + "-" + String.format("%02d", c.get(Calendar.MONTH) + 13) + "-" +
//                            String.format("%02d", Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) + 5));
                    CustomersTableMagagement.updateCustomerDetail(_dbHelper.getWritableDatabase(), holder, getActivity().getIntent().getIntExtra("cust_id",0));
//                    if (ExtcalCustomerSettingTableManagement.isHasStartDate(extDb.getReadableDatabase(),
//                            getActivity().getIntent().getStringExtra("cust_id"), currentDate))
//                        ExtcalCustomerSettingTableManagement.updateData(extDb.getWritableDatabase(), holder);
//                    else {
//                        if (updatedQty) {
//                            String enddate = ExtcalCustomerSettingTableManagement.getOldEndDate(extDb.getReadableDatabase(), getActivity().getIntent().getStringExtra("cust_id"), currentDate);
//                            ExtcalCustomerSettingTableManagement.updateEndDateByArea(extDb.getWritableDatabase(), holder, enddate, currentDate);
//                            holder.setStart_date(currentDate);
//                            ExtcalCustomerSettingTableManagement.insertCustomersSetting(extDb.getWritableDatabase(), holder);
//                        } else if (updateRate)
//
//                            ExtcalCustomerSettingTableManagement.updateRate(extDb.getWritableDatabase(), holder);
//
//
//                        else
//
//                            ExtcalCustomerSettingTableManagement.updateAllData(extDb.getWritableDatabase(), holder);


                    }
//                    BillTableManagement.updateData(_dbHelper.getWritableDatabase(), holder);
//                    Toast.makeText(getActivity(), "Customer edited successfully !", Toast.LENGTH_SHORT).show();
//                    Constants.REFRESH_CALANDER = true;
//                    Constants.REFRESH_CUSTOMERS = true;
//                    Constants.REFRESH_BILL = true;
//                    getActivity().finish();
//
//                } else {
//                    Toast.makeText(getActivity(), getResources().getString(R.string.fill_require_fields), Toast.LENGTH_SHORT).show();
//
//                }
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


}