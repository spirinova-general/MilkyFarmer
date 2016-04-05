package com.milky.ui.main;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.milky.R;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.serviceclasses.AreaService;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceclasses.CustomersService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceclasses.AccountService;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.ui.adapters.PlaceAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.TextValidationMessage;
import com.milky.service.core.Account;
import com.milky.service.core.Area;
import com.milky.service.core.GlobalSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Neha on 11/19/2015.
 */
public class GlobalSetting extends AppCompatActivity implements AdapterView.OnItemClickListener, OnTaskCompleteListner {

    private static final String LOG_TAG = "LOG";
    private Toolbar _mToolbar;
    private TextInputLayout rate_layout;
    private TextInputLayout google_autocomplete_layout;
    private TextInputLayout tax_layouts;
    private TextInputLayout localityInputLayout;
    private TextInputLayout lastname_layout;
    private TextInputLayout mobile_layout;
    private EditText custCode, rate, tax, firstname, lastname, mobile, expirationDate, msgCount;
    private LinearLayout _mBottomLayout;
    private DatabaseHelper _dbHelper;
    //    private AutoCompleteTextView AreaAutocomplete;
    private List<Area> selectedareacityList = new ArrayList<>();

    private String[] autoCompleteData;
    private ImageView add;
    public static int Position = 0;
    //    private String selectedCityId = "", selectedAreaId = "";

    private Vibrator myVib;
    private String mob = "";
    private TextView changeNumber, _billGenerationDateView;
    private EditText _locality;
    private AutoCompleteTextView _cityArea;
    private ScrollView ScrollView01;
    private Button _billGenerationDate;
    private int _mday;
    private int _mmonth;
    private int _mYear;
    static final int DATE_DIALOG_ID = 999;
    private AccountService accountSettings;
    private GlobalSettingsService globalService;
    private AreaService areaService;

    @Override
    protected void onResume() {
        super.onResume();
            /*init xml resources*/

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gloable_setting_layout);

       /*Set Actionbar to layut*/
        setActionBar();

        initResources();
        /*
        * disable keyboard*/
//        disableKeyBoard();

        //On click listerner for bill Generation Date button
        _billGenerationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cal = Calendar.getInstance();
                _mYear = cal.get(Calendar.YEAR);
                _mmonth = cal.get(Calendar.MONTH);
                _mday = cal.get(Calendar.DAY_OF_MONTH) + 1;


                showDialog(DATE_DIALOG_ID);
            }
        });
    }


    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                DatePickerDialog _date = new DatePickerDialog(this, datePickerListener, _mYear, _mmonth,
                        _mday) {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (year < _mYear)
                            view.updateDate(_mYear, _mmonth, _mday);

                        if (monthOfYear < _mmonth && year == _mYear)
                            view.updateDate(_mYear, _mmonth, _mday);

                        if (dayOfMonth < _mday && year == _mYear && monthOfYear == _mmonth)
                            view.updateDate(_mYear, _mmonth, _mday);


                    }
                };
                return _date;
        }
        return null;
    }

    private String rollDate = "";
    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            _mYear = selectedYear;
            _mmonth = selectedMonth;
            _mday = selectedDay;

            _billGenerationDateView.setText(new StringBuilder()
                    .append(Constants.MONTHS[_mmonth]).append("-")
                    .append(String.format("%02d", _mday)).append("-")
                    .append(_mYear).append(" "));
            rollDate = String.valueOf(_mYear) +
                    "-" + String.format("%02d", _mmonth + 1) + "-" + String.format("%02d", _mday);

        }
    };

    private Calendar cal = Calendar.getInstance();

    private void initResources() {
        _billGenerationDateView = (TextView) findViewById(R.id.bill_generation_view);
        _billGenerationDate = (Button) findViewById(R.id.bill_generation_date);
        ScrollView01 = (ScrollView) findViewById(R.id.scrollView);
        rate_layout = (TextInputLayout) findViewById(R.id.rate_layout);
        google_autocomplete_layout = (TextInputLayout) findViewById(R.id.google_autocomplete_layout);
        tax_layouts = (TextInputLayout) findViewById(R.id.tax_percent_layout);
        custCode = (EditText) findViewById(R.id.custCode);
        rate = (EditText) findViewById(R.id.rate);
        tax = (EditText) findViewById(R.id.tax);
        firstname = (EditText) findViewById(R.id.first_name);
        lastname = (EditText) findViewById(R.id.last_name);
        mobile = (EditText) findViewById(R.id.mobile);
        changeNumber = (TextView) findViewById(R.id.change_number);
        expirationDate = (EditText) findViewById(R.id.expirationDate);
        msgCount = (EditText) findViewById(R.id.count);
        changeNumber.setVisibility(View.GONE);
        _cityArea = (AutoCompleteTextView) findViewById(R.id.autocomplete_city_area);
        _locality = (EditText) findViewById(R.id.locality);
        _mBottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
//        _editFab = (FloatingActionButton) findViewById(R.id.editFab);
        add = (ImageView) findViewById(R.id.add_button);
        localityInputLayout = (TextInputLayout) findViewById(R.id.autocomplete_layout);
        TextInputLayout name_layout = (TextInputLayout) findViewById(R.id.name_layout);
        lastname_layout = (TextInputLayout) findViewById(R.id.last_name_layout);
        mobile_layout = (TextInputLayout) findViewById(R.id.mobile_layout);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        _cityArea.setThreshold(1);
        _cityArea.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        _cityArea.setOnItemClickListener(this);

        /*Get DBhelper*/
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        accountSettings = new AccountService();
        globalService = new GlobalSettingsService();
        areaService = new AreaService();
         /*
        * Set text field listeners*/
//        custCode.addTextChangedListener(new TextValidationMessage(custCode_layout, this, false));
        rate.addTextChangedListener(new TextValidationMessage(rate, rate_layout, this, false, true, false, false, false));
        tax.addTextChangedListener(new TextValidationMessage(rate, tax_layouts, this, false, false, false, false, true));
        firstname.addTextChangedListener(new TextValidationMessage(firstname, name_layout, this, false, false, false, false, false));
        lastname.addTextChangedListener(new TextValidationMessage(lastname, lastname_layout, this, false, false, false, false, false));
        mobile.addTextChangedListener(new TextValidationMessage(mobile, mobile_layout, this, true, false, false, false, false));


        /*Set default cust code*/
        if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
            Account holder = accountSettings.getDetails();
            GlobalSettings globalSettings = globalService.getData();
            firstname.setText(holder.getFirstName());
            lastname.setText(holder.getLastName());
            rate.setText(String.valueOf(globalSettings.getDefaultRate()));
            tax.setText(String.valueOf(globalSettings.getTax()));
            mobile.setText(holder.getMobile());
            custCode.setText(holder.getFarmerCode());
            Calendar cal = Calendar.getInstance();
            try {
//                String format[] = expDate.split("\\.");
//                if(format.length==1 || format[1].length()==11)
//                cal.setTime(Constants.api_format_other.parse(expDate));
//                else
                cal.setTime(Constants.work_format.parse(globalSettings.getRollDate()));
                expirationDate.setText(Constants.MONTHS[cal.get(Calendar.MONTH)] + "-" + cal.get(Calendar.DAY_OF_MONTH) + "-" + cal.get(Calendar.YEAR));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            rollDate = globalSettings.getRollDate();
            try {
                Date date = Constants.work_format.parse(rollDate);
                cal.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            _billGenerationDateView.setText(Constants.MONTHS[cal.get(Calendar.MONTH)] + "-"
                    + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)) + "-" + String.valueOf(cal.get(Calendar.YEAR)));

            msgCount.setText(String.valueOf(accountSettings.getLeftSMS()));
        }
        custCode.setOnTouchListener(new View.OnTouchListener()

                                    {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            return true;
                                        }
                                    }

        );

        populateAreaView();
        mob = mobile.getText().

                toString();


        add.setOnClickListener(new View.OnClickListener()

                               {
                                   @Override
                                   public void onClick(View v) {
                                       if (_cityArea.getText().toString().equals("")) {
                                           google_autocomplete_layout.setError("Enter area and city");
                                       } else if (!_cityArea.getText().toString().contains(",")) {
                                           google_autocomplete_layout.setError("Valid format - <AREA> , <CITY>");
                                       } else {
                                           String places[] = _cityArea.getText().toString().split(",");
                                           if (places.length == 1) {
                                               google_autocomplete_layout.setError("Valid format - <AREA> , <CITY>");

                                           } else {
                                               google_autocomplete_layout.setError(null);
                                               if (areaSelected.equals("")) {
                                                   placesArray = _cityArea.getText().toString().split(",");

                                                   areaSelected = placesArray[0];
                                                   citySelected = placesArray[1];
                                               }
                                               if (!_locality.getText().toString().equals(""))
                                                   addLabel(_locality.getText() + "," + areaSelected + "," + citySelected, true);
                                               else


                                                   addLabel(areaSelected + "," + citySelected, true);

                                           }
                                       }


//                                        if(_locality.getText().toString().equals(""))
//                                       {
//                                           localityInputLayout.setError("Enter Locality");
//                                       }
//                                       else
//                                            localityInputLayout.setError(null);


                                   }
                               }

        );


    }

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        /*
        * Set Custome action bar
        * */
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.actionbar_layout, null);
        TextView title = (TextView) mCustomView.findViewById(R.id.title);

        ImageView deleteCustomer = (ImageView) mCustomView.findViewById(R.id.deleteCustomer);
        deleteCustomer.setVisibility(View.GONE);

        title.setText("Settings");
        LinearLayout saveManu = (LinearLayout) mCustomView.findViewById(R.id.saveManu);

        saveManu.setVisibility(View.VISIBLE);

        saveManu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)


            {
                if (rate.getText().toString().trim().equals("")) {
                    rate_layout.setError(getResources().getString(R.string.field_cant_empty));
//                } else if (Float.parseFloat(rate.getText().toString().trim()) <= 0) {
//                    rate_layout.setError(getResources().getString(R.string.fill_valid_amount));
                } else if ((rate.getText().toString().equals(".") || Double.parseDouble(rate.getText().toString())<1)) {
                    rate_layout.setError(getResources().getString(R.string.enter_valid_rate));
                } else if (tax.getText().toString().trim().equals("")) {
                    tax_layouts.setError(getResources().getString(R.string.field_cant_empty));

                } else if (tax.getText().toString().equals(".")) {
                    tax_layouts.setError(getResources().getString(R.string.enter_valid_tax));
                } else if (!_dbHelper.isTableNotEmpty(TableNames.AREA)) {
                    google_autocomplete_layout.setError("Please add atleast one area !");
                } else {
                    Calendar cl = Calendar.getInstance();
                    String date = String.valueOf(cl.get(Calendar.MONTH))
                            + "-" + String.valueOf(cl.get(Calendar.DAY_OF_MONTH)) + "-" + String.valueOf(cl.get(Calendar.YEAR));

                    GlobalSettings globalSettings = new GlobalSettings();
                    globalSettings.setTax(Double.parseDouble(tax.getText().toString().trim()));
                    globalSettings.setDefaultRate(Double.parseDouble(rate.getText().toString().trim()));
                    globalSettings.setRollDate(rollDate);
                    Account holder = new Account();
                    holder.setDateModified(date);
                    holder.setMobile(mobile.getText().toString());
                    holder.setFirstName(firstname.getText().toString());
                    holder.setLastName(lastname.getText().toString());
//                    Update details for Account and Global settings
                    accountSettings.update(holder);
                    globalService.update(globalSettings);
                    if (_dbHelper.isTableNotEmpty(TableNames.Bill))
                        new BillService().updateRollDate();
                    _dbHelper.close();
                    Toast.makeText(GlobalSetting.this, getResources().getString(R.string.data_saved_successfully), Toast.LENGTH_SHORT).show();
                    GlobalSetting.this.finish();
                }

            }
        });
        getSupportActionBar().setCustomView(mCustomView);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //inflate the global menu on option menu created
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                break;
            case R.id.cancel:
                break;
        }

    }

    private int selectedposition = 0;
    int a = 0;

    private void addLabel(String cityarea, final boolean isNewAdded) {

        LinearLayout root = (LinearLayout) findViewById(R.id.linearview);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        final TextView label = new TextView(this);
        label.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        label.setPadding(10, 5, 10, 5);
        if (!cityarea.matches("")) {

            label.setText(cityarea);
            final ImageView remove = new ImageView(this);
            myVib.vibrate(50);
            remove.setId(selectedposition);
            selectedposition++;
            // remove.setBackgroundColor(getResources().getColor(R.color.black));
            remove.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_black_close));
            //remove.setLayoutParams(new LinearLayout.LayoutParams(2, 2));
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    a = remove.getId();
                    if (!new CustomersService().isAreaAssociated(selectedareacityList.get(a).getAreaId())) {
                        if (new AreaService().deleteAreaById(selectedareacityList.get(a).getAreaId())) {
                            Toast.makeText(GlobalSetting.this, "Area removed!", Toast.LENGTH_SHORT).show();
                            ((ViewGroup) label.getParent()).removeView(label);
                            ((ViewGroup) remove.getParent()).removeView(remove);
                        }
                    } else
                        Toast.makeText(GlobalSetting.this, "Area is associated with customer !", Toast.LENGTH_SHORT).show();
                    _dbHelper.close();

                }
            });

            if (isNewAdded) {
                if (!areaService.hasAddress(_locality.getText().toString().trim(), areaSelected.trim(), citySelected.trim())) {
                    label.setTextColor(getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = 10;
                    layoutParams.topMargin = 10;
                    linearLayout.addView(label, layoutParams);
//                    root.addView(label, layoutParams);
                    LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Params.leftMargin = 10;
                    Params.topMargin = 10;
//                    root.addView(remove, layoutParams);
                    linearLayout.addView(remove, layoutParams);
                    root.addView(linearLayout);
                    localityInputLayout.setError(null);
//                    AccountAreaMapping.insertmappedareas(_dbHelper.getWritableDatabase(), _areacityList.get(Position));
                    Area holder = new Area();
                    holder.setArea(areaSelected.trim());
                    holder.setCity(citySelected.trim());
                    holder.setLocality(_locality.getText().toString().trim());

                    areaSelected = "";
                    citySelected = "";
                    long id = areaService.insert(holder);
                    holder.setAreaId((int) id);
                    selectedareacityList.add(holder);
                    _dbHelper.close();
                    _cityArea.setText("");
                    _locality.setText("");
                    google_autocomplete_layout.setError(null);
                    localityInputLayout.setError(null);
                } else {
                    google_autocomplete_layout.setError("This Area is already Selected");
                    localityInputLayout.setError(null);
                    areaSelected = "";
                    citySelected = "";
                    myVib.vibrate(100);
                }
//                if (_locality.getText().toString().equals("")) {
//                    if (!AreaCityTableManagement.hasArea(_dbHelper.getReadableDatabase(), areaSelected)
//                            && !AreaCityTableManagement.hasCity(_dbHelper.getReadableDatabase(), citySelected)
//                            ) {
//
//
//                        label.setTextColor(getResources().getColor(R.color.white));
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        layoutParams.leftMargin = 10;
//                        layoutParams.topMargin = 10;
//                        linearLayout.addView(label, layoutParams);
////                    root.addView(label, layoutParams);
//                        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        Params.leftMargin = 10;
//                        Params.topMargin = 10;
////                    root.addView(remove, layoutParams);
//                        linearLayout.addView(remove, layoutParams);
//                        root.addView(linearLayout);
//                        localityInputLayout.setError(null);
////                    AccountAreaMapping.insertmappedareas(_dbHelper.getWritableDatabase(), _areacityList.get(Position));
//                        VAreaMapper holder = new VAreaMapper();
//                        holder.setArea(areaSelected);
//                        holder.setCity(citySelected);
//                        holder.setLocality(_locality.getText().toString());
//
//                        areaSelected = "";
//                        citySelected = "";
//                        long id = AreaCityTableManagement.insertAreaDetail(_dbHelper.getWritableDatabase(), holder);
//                        holder.setAreaId(String.valueOf(id));
//                        selectedareacityList.add(holder);
//                        _dbHelper.close();
//                        _cityArea.setText("");
//                        _locality.setText("");
//                        google_autocomplete_layout.setError(null);
//                        localityInputLayout.setError(null);
//                    } else {
//                        google_autocomplete_layout.setError("This Area is already Selected");
//                        localityInputLayout.setError(null);
//                        myVib.vibrate(100);
//                    }
//
//                } else {
//                    if ((!AreaCityTableManagement.hasLocation(_dbHelper.getReadableDatabase(), _locality.getText().toString())
//                            || (!AreaCityTableManagement.hasCity(_dbHelper.getReadableDatabase(), citySelected) &&!AreaCityTableManagement.hasArea(_dbHelper.getReadableDatabase(), areaSelected) ))
//                            ) {
//
//                        label.setTextColor(getResources().getColor(R.color.white));
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        layoutParams.leftMargin = 10;
//                        layoutParams.topMargin = 10;
//                        linearLayout.addView(label, layoutParams);
////                    root.addView(label, layoutParams);
//                        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        Params.leftMargin = 10;
//                        Params.topMargin = 10;
////                    root.addView(remove, layoutParams);
//                        linearLayout.addView(remove, layoutParams);
//                        root.addView(linearLayout);
//                        localityInputLayout.setError(null);
////                    AccountAreaMapping.insertmappedareas(_dbHelper.getWritableDatabase(), _areacityList.get(Position));
//                        VAreaMapper holder = new VAreaMapper();
//                        holder.setArea(areaSelected);
//                        holder.setCity(citySelected);
//                        holder.setLocality(_locality.getText().toString());
////                        holder.setAreaId(String.valueOf(remove.getId()));
//                        areaSelected = "";
//                        citySelected = "";
//
//                        long id = AreaCityTableManagement.insertAreaDetail(_dbHelper.getWritableDatabase(), holder);
//                        holder.setAreaId(String.valueOf(id));
//                        selectedareacityList.add(holder);
//                        _dbHelper.close();
//                        _cityArea.setText("");
//                        _locality.setText("");
//                        google_autocomplete_layout.setError(null);
//                        localityInputLayout.setError(null);
//                    } else {
//                        google_autocomplete_layout.setError(null);
//                        localityInputLayout.setError("This locality is already Selected");
//                        myVib.vibrate(100);
//                    }
////
//                }
                ScrollView01.post(new Runnable() {

                    @Override
                    public void run() {
                        ScrollView01.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

            } else if (!isNewAdded) {
                _cityArea.setText("");
                _locality.setText("");
                localityInputLayout.setError(null);
                label.setTextColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 10;
                layoutParams.topMargin = 10;
                linearLayout.addView(label, layoutParams);
//                root.addView(linearLayout);
                LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Params.leftMargin = 10;
                Params.topMargin = 10;
                linearLayout.addView(remove, layoutParams);
                root.addView(linearLayout);


            } else {
                localityInputLayout.setError("Select valid area!");
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    // A place has been received; use requestCode to track the request.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i("", "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    PlaceAdapter adapter;

    private void parseAndBindDataToListview(String result) {
        try {
            JSONObject jsonObject2 = new JSONObject(result);
            JSONArray array = jsonObject2.getJSONArray("predictions");
            JSONObject[] jsonObject = new JSONObject[array.length()];
            for (int i = 0; i < array.length(); i++) {
                jsonObject[i] = array.getJSONObject(i);
            }

            adapter = new PlaceAdapter(GlobalSetting.this, R.layout.place_row, jsonObject);
//            listViewPlaces.setAdapter(adapter);
        } catch (Exception e) {
            e.toString();
        }
    }


    AlertDialog dialog;

    private void openWebView() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        dialog = alert.create();
//        alert.setTitle("Title here");

        WebView wv = new WebView(this) {
            @Override
            public boolean onCheckIsTextEditor() {
                return true;
            }
        };
        wv.loadUrl("file:///android_asset/places.html");
        wv.getSettings().setJavaScriptEnabled(true);
//        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.addJavascriptInterface(new JavaScriptInterface(this), "android");
//        wv.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//
//                return true;
//            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//
//            }
//        });
//        wv.requestFocus(View.FOCUS_DOWN);
//        wv.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_UP:
//                        if (!v.hasFocus()) {
//                            v.requestFocus();
//                        }
//                        break;
//                }
//                return false;
//            }
//        });

//        dialog.setCancelable(false);
        dialog.setView(wv);

        dialog.show();
    }

    @Override
    public void onTaskCompleted(String type, HashMap<String, String> listType) {

    }

    public class JavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        JavaScriptInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            place = toast;
            new pdHandler().sendEmptyMessage(0);
        }
    }

    private String place = "";
    private String[] placesArray;
    private String areaSelected = "", citySelected = "";

    class pdHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (dialog != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        _cityArea.setText(place);
                        google_autocomplete_layout.setHint("Area and city");
                        placesArray = _cityArea.getText().toString().split(",");
                        if (placesArray.length == 2) {
                            areaSelected = placesArray[0];
                        } else {
                            areaSelected = placesArray[0];
                            citySelected = placesArray[1];
                        }
                    }
                });
                dialog.dismiss();
                dialog = null;

            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    //------------ make your specific key ------------
    private static final String API_KEY = "AIzaSyAvF7oUI32MTNZgTIQoZ2AbwIhpjY35mpI";
//            "AIzaSyCkBjhO-0_wvtrL5B0beeD0O24_cqrsllQ";
//AIzaSyAU9ShujnIg3IDQxtPr7Q1qOvFVdwNmWc4

    public static ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());

            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        if(resultList !=null) {
                            filterResults.values = resultList;
                            filterResults.count = resultList.size();
                        }
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                }
            };
            return filter;
        }
    }

    private void populateAreaView() {
         /*Fill fields from db*/
        if (_dbHelper.isTableNotEmpty(TableNames.AREA))

        {
            List<Area> list = new AreaService().getStoredAddresses();
            selectedareacityList = list;
//            ArrayList<String> list = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
//            for (int i = 0; i < list.size(); ++i) {
//
//                selectedareasList.add(AreaMapTableManagement.getAreabyAreaId(_dbHelper.getReadableDatabase(), list.get(i)));
//
//            }
//            for (int j = 0; j < selectedareasList.size(); j++) {
//
//                VAreaMapper areacity = new VAreaMapper();
//                areacity.setArea(selectedareasList.get(j).getArea());
//                areacity.setAreaId(selectedareasList.get(j).getAreaId());
//                areacity.setCityId(selectedareasList.get(j).getCityId());
//                areacity.setCity(AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), selectedareasList.get(j).getCityId()));
//                areacity.setCityArea(areacity.getArea() + ", " + areacity.getCity());
//                selectedareacityList.add(areacity);
//
//
//            }

            for (int x = 0; x < list.size(); x++) {

                addLabel(list.get(x).getCityArea(), false);

            }
        }
    }


}



