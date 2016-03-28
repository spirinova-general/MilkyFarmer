package com.milky.ui.customers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.AreaCityTableManagement;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.GlobalSettingsService;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.TextValidationMessage;
import com.milky.utils.UserPrefrences;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomers;
import com.milky.viewmodel.VCustomersSetting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomerAddActivity extends AppCompatActivity {
    private Toolbar _mToolbar;
    private EditText _firstName;
    private EditText _lastName;
    private EditText _mAddress1;
    //    private EditText _address2;
    private EditText _rate;
    private EditText _mPhone;
    private EditText _mBalance;
    private EditText _mQuantuty;
    private Button _mSave, _mCancel;
    private LinearLayout _mBottomLayout;
    private int dataCount = 0;
    private CoordinatorLayout _mCoordinatorLayout;
    private AutoCompleteTextView _cityAreaAutocomplete;
    private DatabaseHelper _dbHelper;
    private TextInputLayout name_layout, rate_layout, balance_layout, autocomplete_layout, last_name_layout, flat_number_layout, street_layout, milk_quantity_layout, _phone_textinput_layout;
    private int selectedAreaId = 0;
    private ArrayList<VAreaMapper> _areacityList = new ArrayList<>();
    private String[] autoCompleteData;
    private Calendar c;
    private TextView _pickdate;
    private Calendar myCalendar;
    private int myear;
    private int mmonth;
    private int mday;
    private String pickedDate = "";
    static final int DATE_DIALOG_ID = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_customer_add);
        setActionBar();
        initResources();
        setCurrentDateOnView();
        addListenerOnButton();

      /* final boolean phone,final boolean rate,final boolean quantity,final boolean balance
        * Set text field listeners*/
        _mAddress1.addTextChangedListener(new TextValidationMessage(_mAddress1, flat_number_layout, this, false, false, false, false, false));
        _firstName.addTextChangedListener(new TextValidationMessage(_mAddress1, name_layout, this, false, false, false, false, false));
        _mQuantuty.addTextChangedListener(new TextValidationMessage(_mAddress1, milk_quantity_layout, this, false, false, true, false, false));
        _mBalance.addTextChangedListener(new TextValidationMessage(_mAddress1, balance_layout, this, false, false, false, true, false));
        _mPhone.addTextChangedListener(new TextValidationMessage(_mPhone, _phone_textinput_layout, this, true, false, false, false, false));
//        _address2.addTextChangedListener(new TextValidationMessage(_mPhone, street_layout, this, false));
        _lastName.addTextChangedListener(new TextValidationMessage(_mPhone, last_name_layout, this, false, false, false, false, false));
        _cityAreaAutocomplete.addTextChangedListener(new TextValidationMessage(_mPhone, autocomplete_layout, this, false, false, false, false, false));
        _rate.addTextChangedListener(new TextValidationMessage(_mPhone, rate_layout, this, false, true, false, false, false));


    }

    String formattedDate;

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        c = Calendar.getInstance();
        SimpleDateFormat df = Constants.work_format;
        formattedDate = df.format(c.getTime());
        getSupportActionBar().setSubtitle(formattedDate);
         /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        title.setText("New Customer");
        TextView subTitle = (TextView) mCustomView.findViewById(R.id.date);
//        subTitle.setVisibility(View.GONE);
//        title.setText("Add Customer");
        LinearLayout saveManu = (LinearLayout) mCustomView.findViewById(R.id.saveManu);

        saveManu.setVisibility(View.VISIBLE);
        saveManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (_firstName.getText().toString().equals(""))
                        name_layout.setError("Enter name!");
                    else if (_lastName.getText().toString().equals(""))
                        last_name_layout.setError("Enter name!");
                    else if (_rate.getText().toString().equals(""))
                        rate_layout.setError("Enter amount!");
                    else if (_rate.getText().toString().equals(".")) {
                        rate_layout.setError(getResources().getString(R.string.enter_valid_rate));
                    } else if (Float.parseFloat(_rate.getText().toString().trim()) <= 0) {
                        rate_layout.setError("Enter valid amount!");
                    } else if (_mBalance.getText().toString().equals("")) {
                        balance_layout.setError("Enter balance amount");
                    } else if (_mBalance.getText().toString().equals(".")) {
                        balance_layout.setError(getResources().getString(R.string.enter_valid_balance));
                    } else if (_mQuantuty.getText().toString().equals(".")) {
                        milk_quantity_layout.setError(getResources().getString(R.string.enter_valid_quantity));
                    } else if (_mAddress1.getText().toString().equals(""))
                        flat_number_layout.setError("Enter flat number!");
//                    else if (_address2.getText().toString().equals(""))
//                        street_layout.setError("Enter street !");

                    else if (selectedAreaId == 0)
                        autocomplete_layout.setError("Select valid area");
                    else if (_mPhone.getText().toString().equals(""))
                        _phone_textinput_layout.setError("Enter mobile number!");
                    else if (_mQuantuty.getText().toString().equals(""))
                        milk_quantity_layout.setError("Enter milk quantity!");
                    else if (Float.parseFloat(_mQuantuty.getText().toString().trim()) <= 0)
                        milk_quantity_layout.setError("Enter valid quantity!");

                    else if ((!_firstName.getText().toString().equals("")
                            && !_lastName.getText().toString().equals("") &&
                            !_mBalance.getText().toString().equals("") &&
                            !_mAddress1.getText().toString().equals("")
                            && selectedAreaId != 0
                            && !_mPhone.getText().toString().equals("") &&
                            !_mQuantuty.getText().toString().equals("") && !_rate.getText().toString().equals("")
                    )) {
                        c = Calendar.getInstance();
                        SimpleDateFormat df = Constants.work_format;
                        formattedDate = df.format(c.getTime());
                        Calendar deliveryDateTime = Calendar.getInstance();

                        try {
                            deliveryDateTime.setTime(df.parse(pickedDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        VCustomers holder = new VCustomers();
                        holder.setFirstName(_firstName.getText().toString());
                        holder.setLastName(_lastName.getText().toString());
                        holder.setAddress1(_mAddress1.getText().toString());
                        holder.setBalance_amount(Double.parseDouble(_mBalance.getText().toString()));
                        holder.setAreaId(selectedAreaId);
                        holder.setMobile(_mPhone.getText().toString());
                        holder.setGetDefaultQuantity(Double.parseDouble(_mQuantuty.getText().toString()));
                        holder.setDefaultRate(Double.parseDouble(_rate.getText().toString()));
//                        holder.setBalanceType("1");
                        holder.setDateAdded(formattedDate);
                        holder.setStartDate(pickedDate);
                        holder.setEndDate("2250" + "-" +
                                String.format("%02d", deliveryDateTime.get(Calendar.MONTH) + 13) + "-" +
                                String.format("%02d", deliveryDateTime.getActualMaximum(Calendar.DAY_OF_MONTH) + 5));
                        holder.setFirstName(_firstName.getText().toString());
                        holder.setLastName(_lastName.getText().toString());
                        holder.setBalance_amount(Double.parseDouble(_mBalance.getText().toString()));
//                        holder.setTotal(0.0);
//                        holder.setAddress2(_address2.getText().toString());
//                        holder.setCityId(selectedCityId);
                        autocomplete_layout.setError(null);
                        holder.setAreaId(selectedAreaId);
                        holder.setMobile(_mPhone.getText().toString());
                        holder.setDateAdded(formattedDate);
                        CustomersTableMagagement.insertCustomerDetail(_dbHelper.getWritableDatabase(), holder);

                        VCustomersSetting setting = new VCustomersSetting();
                        setting.setCustomerId(CustomersTableMagagement.getCustomerId(_dbHelper.getReadableDatabase()));
                        setting.setDefaultRate(holder.getDefaultRate());
                        setting.setStartDate(pickedDate);
                        setting.setEndDate(holder.getEndDate());
                        setting.setGetDefaultQuantity(holder.getGetDefaultQuantity());
                        setting.setDirty(0);

                        CustomerSettingTableManagement.insertCustomersSetting(_dbHelper.getWritableDatabase(), setting);
                        VBill bill = new VBill();
                        bill.setDirty(0);
                        bill.setPaymentMade(0);
                        bill.setTotalAmount(0);
                        bill.setEndDate(holder.getEndDate());
                        bill.setAdjustment(0);
                        bill.setBalance(0);
                        bill.setTax(GlobalSettingsService.getDefautTax(_dbHelper.getReadableDatabase()));
                        bill.setIsCleared(1);
                        bill.setDateModified(formattedDate);    
                        bill.setRollDate(GlobalSettingsService.getRollDate(_dbHelper.getReadableDatabase()));
                        bill.setBalanceType(1);
                        bill.setCustomerId(setting.getCustomerId());
                        bill.setStartDate(holder.getStartDate());
                        bill.setQuantity(holder.getGetDefaultQuantity());
                        bill.setDateAdded(formattedDate);

                        Calendar cal = Calendar.getInstance();
                        if ((cal.get(Calendar.DAY_OF_MONTH)) == cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                            bill.setIsOutstanding(0);
                            SharedPreferences preferences = AppUtil.getInstance().getPrefrences();
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString(UserPrefrences.INSERT_BILL, "0");
                            edit.apply();
                        } else
                            bill.setIsOutstanding(1);
                        BillTableManagement.insertBillData(_dbHelper.getWritableDatabase(), bill);
                        Constants.REFRESH_CUSTOMERS = true;
                        Constants.REFRESH_BILL = true;
                        Constants.REFRESH_CALANDER = true;
                        CustomerAddActivity.this.finish();
                    }
                } catch (NullPointerException npe) {
                } finally {
                    _dbHelper.close();
                }
            }
        });

        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    public void setCurrentDateOnView() {


        final Calendar c = Calendar.getInstance();

        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);

        // set current date into textview

//        _pickdate.setText(new StringBuilder()
//                // Month is 0 based, just add 1
//                .append( String.format("%02d",mmonth+1)).append("-").append( String.format("%02d",mday)).append("-")
//                .append(myear).append(" "));
        pickedDate = myear + "-" + String.format("%02d", mmonth + 1) + "-" + String.format("%02d", mday);
        _pickdate.setText(Constants._display_format.format(c.getTime()));

    }

    public void addListenerOnButton() {


        _pickdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                myear = c.get(Calendar.YEAR);
                mmonth = c.get(Calendar.MONTH);
                mday = c.get(Calendar.DAY_OF_MONTH);

                showDialog(DATE_DIALOG_ID);

            }

        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog _date = new DatePickerDialog(this, datePickerListener, myear, mmonth,
                        mday) {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < myear)
                            view.updateDate(myear, mmonth, dayOfMonth);

                        else if (monthOfYear < mmonth && year == myear)
                            view.updateDate(myear, mmonth, dayOfMonth);

//                       if (year == myear && monthOfYear == mmonth)
//                        view.updateDate(myear, mmonth, dayOfMonth);

                    }
                };
                return _date;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            myear = selectedYear;
            mmonth = selectedMonth;
            mday = selectedDay;
            Calendar c = Calendar.getInstance();
            pickedDate = myear + "-" + String.format("%02d", mmonth + 1) + "-" + String.format("%02d", mday);

            // set selected date into textview
            _pickdate.setText(new StringBuilder()

                    .append(String.format("%02d", mday))
                    .append("-").append(Constants.MONTHS[mmonth]).append("-")
                    .append(myear).append(" "));

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.delete_customer_menu, menu);

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

    private void initResources() {
        _firstName = (EditText) findViewById(R.id.first_name);
        _lastName = (EditText) findViewById(R.id.last_name);
        _mAddress1 = (EditText) findViewById(R.id.flat_number);
//        _address2 = (EditText) findViewById(R.id.street);
        _mPhone = (EditText) findViewById(R.id.phone);
        _mQuantuty = (EditText) findViewById(R.id.milk_quantity);
        _mBalance = (EditText) findViewById(R.id.balance);
        _mSave = (Button) findViewById(R.id.save);
        _mCancel = (Button) findViewById(R.id.cancel);
        _mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        _mBottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
        _mBottomLayout.setVisibility(View.GONE);
        _rate = (EditText) findViewById(R.id.rate);
        _mCancel.setVisibility(View.GONE);
        _pickdate = (TextView) findViewById(R.id.pick_date);
        _pickdate.setText(formattedDate);

        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        _cityAreaAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_city_area);
        /*Set defaul rate
        * */
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT))
            _rate.setText(GlobalSettingsService.getDefaultRate(_dbHelper.getReadableDatabase()));

        _phone_textinput_layout = (TextInputLayout) findViewById(R.id.phone_textinput_layout);
        rate_layout = (TextInputLayout) findViewById(R.id.rate_layout);
        name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        last_name_layout = (TextInputLayout) findViewById(R.id.last_name_layout);
        street_layout = (TextInputLayout) findViewById(R.id.street_layout);
        balance_layout = (TextInputLayout) findViewById(R.id.balance_layout);
        flat_number_layout = (TextInputLayout) findViewById(R.id.flat_number_layout);
        autocomplete_layout = (TextInputLayout) findViewById(R.id.autocomplete_layout);
        milk_quantity_layout = (TextInputLayout) findViewById(R.id.milk_quantity_layout);
//        ArrayList<String> areas = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
//        for (int i = 0; i < areas.size(); ++i) {
//            _areaList.add(AreaMapTableManagement.getAreabyAreaId(_dbHelper.getReadableDatabase(), areas.get(i)));
//        }
        _areacityList = AreaCityTableManagement.getFullAddress(_dbHelper.getReadableDatabase());
        autoCompleteData = new String[_areacityList.size()];
//        for (int i = 0; i < _areaList.size(); i++) {
//            VAreaMapper areacity = new VAreaMapper();
//            areacity.setArea(_areaList.get(i).getArea());
//            areacity.setAreaId(_areaList.get(i).getAreaId());
////            areacity.setCityId(_areaList.get(i).getCityId());
////            areacity.setCity(AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), _areaList.get(i).getCityId()));
////            areacity.setCityArea(areacity.getArea() + areacity.getCity());
//            areacity.setCity(_areaList.get(i).getCity());
//            areacity.setLocality(_areaList.get(i).getLocality());
//            areacity.setCityArea(areacity.getLocality()+areacity.getArea()+areacity.getCity());
//            _areacityList.add(areacity);
//        }
        _dbHelper.close();
        _cityAreaAutocomplete.setThreshold(1);//will start working from first character

        AreaCityAdapter adapter1 = new AreaCityAdapter(this, 0, R.id.address, _areacityList);
        _cityAreaAutocomplete.setAdapter(adapter1);

        _cityAreaAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                _cityAreaAutocomplete.setText(_areacityList.get(position).getCityArea());

                selectedAreaId = _areacityList.get(position).getAreaId();
//                selectedCityId = _areacityList.get(position).getCityId();
                _cityAreaAutocomplete.setSelection(_cityAreaAutocomplete.getText().length());
            }
        });
        _cityAreaAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    selectedAreaId = 0;
//                    selectedCityId = "";

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        _mSave.setText("Add");
//        ((FloatingActionButton) findViewById(R.id.editFab)).setVisibility(View.GONE);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //replaces the default 'Back' button action
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}


