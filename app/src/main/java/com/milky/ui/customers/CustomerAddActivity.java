package com.milky.ui.customers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.milky.service.databaseutils.AccountAreaMapping;
import com.milky.service.databaseutils.AreaMapTableManagement;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.ui.adapters.AutocompleteAreaArrayAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.TextValidationMessage;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VCustomersList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomerAddActivity extends AppCompatActivity {
    private Toolbar _mToolbar;
    private EditText _firstName;
    private EditText _lastName;
    private EditText _mAddress1;
    private EditText _address2;
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
    private String selectedCityId = "", selectedAreaId = "";
    private ArrayList<VAreaMapper> _areaList = new ArrayList<>(), _areacityList = new ArrayList<>();
    private String[] autoCompleteData;
    private Calendar c;
    private AreaCityAdapter adapter1;
    private AutocompleteAreaArrayAdapter adapter;
    private TextView _pickdate;
    private Calendar myCalendar;
    private int myear;
    private int mmonth;
    private int mday;
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

      /*
        * Set text field listeners*/
        _mAddress1.addTextChangedListener(new TextValidationMessage(_mAddress1, flat_number_layout, this, false));
        _firstName.addTextChangedListener(new TextValidationMessage(_mAddress1, name_layout, this, false));
        _mQuantuty.addTextChangedListener(new TextValidationMessage(_mAddress1, milk_quantity_layout, this, false));
        _mBalance.addTextChangedListener(new TextValidationMessage(_mAddress1, balance_layout, this, false));
        _mPhone.addTextChangedListener(new TextValidationMessage(_mPhone, _phone_textinput_layout, this, true));
        _address2.addTextChangedListener(new TextValidationMessage(_mPhone, street_layout, this, false));
        _lastName.addTextChangedListener(new TextValidationMessage(_mPhone, last_name_layout, this, false));
        _cityAreaAutocomplete.addTextChangedListener(new TextValidationMessage(_mPhone, autocomplete_layout, this, false));
        _rate.addTextChangedListener(new TextValidationMessage(_mPhone, rate_layout, this, false));

    }

    String formattedDate;

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        c = Calendar.getInstance();
        SimpleDateFormat df = Constants.format;
        formattedDate = df.format(c.getTime());
        getSupportActionBar().setSubtitle(formattedDate);
         /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar_layout, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);
        TextView subTitle = (TextView) mCustomView.findViewById(R.id.date);
        subTitle.setVisibility(View.GONE);
        title.setText("Add Customer");
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
                    else if (_mBalance.getText().toString().equals(""))
                        balance_layout.setError("Enter balance amount");
                    else if (_mAddress1.getText().toString().equals(""))
                        flat_number_layout.setError("Enter flat number!");
                    else if (_address2.getText().toString().equals(""))
                        street_layout.setError("Enter street !");

                    else if (selectedCityId.equals("") && selectedAreaId.equals(""))
                        autocomplete_layout.setError("Select valid area");
                    else if (_mPhone.getText().toString().equals(""))
                        _phone_textinput_layout.setError("Enter mobile number!");
                    else if (_mQuantuty.getText().toString().equals(""))
                        milk_quantity_layout.setError("Enter milk quantity!");
                    else if ((!_firstName.getText().toString().equals("")
                            && !_lastName.getText().toString().equals("") &&
                            !_mBalance.getText().toString().equals("") &&
                            !_mAddress1.getText().toString().equals("")
                            && !_address2.getText().toString().equals("")
                            && !selectedCityId.equals("") && !selectedAreaId.equals("")
                            && !_mPhone.getText().toString().equals("") &&
                            !_mQuantuty.getText().toString().equals("") && !_rate.getText().toString().equals("")
                    )) {
                        VCustomersList holder = new VCustomersList();
                        holder.setFirstName(_firstName.getText().toString());
                        holder.setLastName(_lastName.getText().toString());
                        holder.setBalance_amount(_mBalance.getText().toString());
                        holder.setAddress1(_mAddress1.getText().toString());
                        holder.setAddress2(_address2.getText().toString());
                        holder.setCityId(selectedCityId);
                        autocomplete_layout.setError(null);
                        holder.setAreaId(selectedAreaId);
                        holder.setMobile(_mPhone.getText().toString());
                        holder.setQuantity(_mQuantuty.getText().toString());
                        holder.setAccountId(Constants.ACCOUNT_ID);
                        holder.setRate(_rate.getText().toString());
                        c = Calendar.getInstance();
                        SimpleDateFormat df = Constants.format;
                        formattedDate = df.format(c.getTime());
                        holder.setDateAdded(formattedDate);
                        holder.setStart_date(_pickdate.getText().toString().trim());
                        holder.setEnd_date(String.format("%02d", c.get(Calendar.MONTH) + 1) + "-" +
                                String.format("%02d",Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)) + "-" + String.format("%02d", c.get(Calendar.YEAR)));
                        holder.setCustomerId(String.valueOf(System.currentTimeMillis()));
                        CustomersTableMagagement.insertCustomerDetail(_dbHelper.getWritableDatabase(), holder);
                        CustomerSettingTableManagement.insertCustomersSetting(_dbHelper.getWritableDatabase(), holder);

                        holder.setTax(Account.getDefautTax(_dbHelper.getReadableDatabase()));
                        holder.setAdjustment("");
                        holder.setPaymentMade("0");
                        holder.setIsCleared("0");
                        holder.setDateModified(holder.getStart_date());
                        BillTableManagement.insertBillData(_dbHelper.getWritableDatabase(), holder);
                        CustomerAddActivity.this.finish();
                    }
                } catch (NullPointerException npe) {
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

        _pickdate.setText(new StringBuilder()
                // Month is 0 based, just add 1
                .append( String.format("%02d",mmonth+1)).append("-").append( String.format("%02d",mday)).append("-")
                .append(myear).append(" "));

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

            // set selected date into textview
            _pickdate.setText(new StringBuilder()
                    .append( String.format("%02d",mmonth+1)).append("-")
                    .append( String.format("%02d",mday))
                    .append("-")
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
        _address2 = (EditText) findViewById(R.id.street);
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
            _rate.setText(Account.getDefaultRate(_dbHelper.getReadableDatabase()));

        _phone_textinput_layout = (TextInputLayout) findViewById(R.id.phone_textinput_layout);
        rate_layout = (TextInputLayout) findViewById(R.id.rate_layout);
        name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        last_name_layout = (TextInputLayout) findViewById(R.id.last_name_layout);
        street_layout = (TextInputLayout) findViewById(R.id.street_layout);
        balance_layout = (TextInputLayout) findViewById(R.id.balance_layout);
        flat_number_layout = (TextInputLayout) findViewById(R.id.flat_number_layout);
        autocomplete_layout = (TextInputLayout) findViewById(R.id.autocomplete_layout);
        milk_quantity_layout = (TextInputLayout) findViewById(R.id.milk_quantity_layout);
        ArrayList<String> areas = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
        for (int i = 0; i < areas.size(); ++i) {
            _areaList.add(AreaMapTableManagement.getAreabyAreaId(_dbHelper.getReadableDatabase(), areas.get(i)));
        }
        autoCompleteData = new String[_areaList.size()];
        for (int i = 0; i < _areaList.size(); i++) {
            VAreaMapper areacity = new VAreaMapper();
            areacity.setArea(_areaList.get(i).getArea());
            areacity.setAreaId(_areaList.get(i).getAreaId());
            areacity.setCityId(_areaList.get(i).getCityId());
            areacity.setCity(AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), _areaList.get(i).getCityId()));
            areacity.setCityArea(areacity.getArea() + areacity.getCity());
            _areacityList.add(areacity);


        }
        _cityAreaAutocomplete.setThreshold(1);//will start working from first character

        AreaCityAdapter adapter1 = new AreaCityAdapter(this, 0, R.id.te1, _areacityList);
        _cityAreaAutocomplete.setAdapter(adapter1);

        _cityAreaAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _cityAreaAutocomplete.setText(_areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
                selectedAreaId = _areacityList.get(position).getAreaId();
                selectedCityId = _areacityList.get(position).getCityId();
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
                    selectedAreaId = "";
                    selectedCityId = "";

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        _mSave.setText("Add");
//        ((FloatingActionButton) findViewById(R.id.editFab)).setVisibility(View.GONE);

        final Snackbar snackbar = Snackbar
                .make(_mCoordinatorLayout, "Please fill required fields!", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

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
