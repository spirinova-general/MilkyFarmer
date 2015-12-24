package com.milky.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.milky.R;
import com.milky.service.databaseutils.AccountAreaMapping;
import com.milky.service.databaseutils.AreaMapTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.GlobalSettingTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.adapters.AreaCityAdapter;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.utils.EnableEditableFields;
import com.milky.utils.TextValidationMessage;
import com.milky.viewmodel.VAreaMapper;
import com.milky.viewmodel.VGlobalSettings;

import java.util.ArrayList;

/**
 * Created by Neha on 11/19/2015.
 */
public class GlobalSetting extends AppCompatActivity {
    private Toolbar _mToolbar;
    private TextInputLayout rate_layout, custCode_layout, tax_layouts, autocomplete_layout;
    private EditText custCode, rate, tax;
    private InputMethodManager inputMethodManager;
    private LinearLayout _mBottomLayout;
    //    private FloatingActionButton _editFab;
    private DatabaseHelper _dbHelper;
    private AutoCompleteTextView AreaAutocomplete;
    private ArrayList<VAreaMapper> _areaList, _areacityList = new ArrayList<>(), selectedareasList = new ArrayList<>(), selectedareacityList = new ArrayList<>();
    private String[] autoCompleteData;
    private Button add;
    public static int Position = 0;
    private String selectedCityId = "", selectedAreaId = "";
    private AreaCityAdapter adapter1;
    private Vibrator myVib;
    private InputMethodManager inputManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gloable_setting_layout);

       /*Set Actionbar to layut*/
        setActionBar();
        /*init xml resources*/
        initResources();

        /*
        * disable keyboard*/
//        disableKeyBoard();

        custCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }


    private void initResources() {
        inputMethodManager =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        rate_layout = (TextInputLayout) findViewById(R.id.rate_layout);
        custCode_layout = (TextInputLayout) findViewById(R.id.custCode_layout);
        tax_layouts = (TextInputLayout) findViewById(R.id.tax_percent_layout);
        custCode = (EditText) findViewById(R.id.custCode);
        rate = (EditText) findViewById(R.id.rate);
        tax = (EditText) findViewById(R.id.tax);
        AreaAutocomplete = (AutoCompleteTextView) findViewById(R.id.autocomplete_city_area);
        _mBottomLayout = (LinearLayout) findViewById(R.id.bottomLayout);
//        _editFab = (FloatingActionButton) findViewById(R.id.editFab);
        add = (Button) findViewById(R.id.add_button);
        autocomplete_layout = (TextInputLayout) findViewById(R.id.autocomplete_layout);
        myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        /*Get DBhelper*/
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
         /*
        * Set text field listeners*/
//        custCode.addTextChangedListener(new TextValidationMessage(custCode_layout, this, false));
        rate.addTextChangedListener(new TextValidationMessage(rate, rate_layout, this, false));
        tax.addTextChangedListener(new TextValidationMessage(rate, tax_layouts, this, false));

         /* Let fields be enabled if edit button has been clicked only.
        * */

//        rate.setOnTouchListener(new EnableEditableFields(rate, this, inputMethodManager));
//        tax.setOnTouchListener(new EnableEditableFields(tax, this, inputMethodManager));
        /*Set default cust code*/
        custCode.setText("1Axc");
        /*Fill fields from db*/
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT_AREA_MAPPING)) {
            ArrayList<String> list = AccountAreaMapping.getArea(_dbHelper.getReadableDatabase());
            for (int i = 0; i < list.size(); ++i) {

                selectedareasList.add(AreaMapTableManagement.getAreabyAreaId(_dbHelper.getReadableDatabase(), list.get(i)));

            }
            for (int j = 0; j < selectedareasList.size(); j++) {

                VAreaMapper areacity = new VAreaMapper();
                areacity.setArea(selectedareasList.get(j).getArea());
                areacity.setAreaId(selectedareasList.get(j).getAreaId());
                areacity.setCityId(selectedareasList.get(j).getCityId());
                areacity.setCity(AreaMapTableManagement.getCityNameById(_dbHelper.getReadableDatabase(), selectedareasList.get(j).getCityId()));
                areacity.setCityArea(areacity.getArea() + areacity.getCity());
                selectedareacityList.add(areacity);


            }
            for (int x = 0; x < selectedareacityList.size(); x++) {
                addLabel(selectedareacityList.get(x).getCityArea(), false);
            }
        }
        _areaList = AreaMapTableManagement.getAreaById(_dbHelper.getReadableDatabase(), Constants.ACCOUNT_ID);

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
//        AreaAutocomplete.setFocusable(false);
//        AreaAutocomplete.setFocusableInTouchMode(false);
        AreaAutocomplete.setThreshold(1);
        adapter1 = new AreaCityAdapter(this, 0, R.id.te1, _areacityList);
        AreaAutocomplete.setAdapter(adapter1);
        AreaAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Position = position;
                AreaAutocomplete.setText(_areacityList.get(position).getArea() + ", " + _areacityList.get(position).getCity());
                selectedAreaId = _areacityList.get(position).getAreaId();
                selectedCityId = _areacityList.get(position).getCityId();
                selectedareacityList.add(_areacityList.get(position));

                AreaAutocomplete.setSelection(AreaAutocomplete.getText().length());
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLabel(AreaAutocomplete.getText().toString(), true);

            }
        });

        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_GLOBAL_SETTINGS)) {
            VGlobalSettings holder = GlobalSettingTableManagement.getGlobalSettingDetail(_dbHelper.getReadableDatabase());
            rate.setText(holder.getDefaultRate());
            tax.setText(holder.getTax());


        }
    }

    private void setActionBar() {
        _mToolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(_mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle(getResources().getString(R.string.global_setting));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.editFab:
//                /* on click Edit button*/
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//                rate.requestFocus();
//                inputMethodManager.showSoftInput(rate, InputMethodManager.SHOW_IMPLICIT);
//                _mBottomLayout.setVisibility(View.VISIBLE);
////                _editFab.setVisibility(View.GONE);
//                rate.setSelection(rate.getText().length());
//                isEditModeEnabled = true;
//                AreaAutocomplete.setFocusable(true);
//                add.setEnabled(true);
//                AreaAutocomplete.setFocusableInTouchMode(true);
//                EnableEditableFields.setIsEnabled(true);
//                break;
            case R.id.save:


                if (rate.getText().toString().trim().equals("")) {
                    rate_layout.setError(getResources().getString(R.string.field_cant_empty));
                } else if (tax.getText().toString().trim().equals("")) {
                    tax_layouts.setError(getResources().getString(R.string.field_cant_empty));
                } else if (!_dbHelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT_AREA_MAPPING)) {
                    autocomplete_layout.setError("Please select some area !");
                } else {

                    if (_dbHelper.isTableNotEmpty(TableNames.TABLE_GLOBAL_SETTINGS)) {
                        VGlobalSettings holder = new VGlobalSettings();
                        holder.setTax(tax.getText().toString().trim());
                        holder.setDefaultRate(rate.getText().toString().trim());
                        holder.setDateModified(String.valueOf(System.currentTimeMillis()));
                        GlobalSettingTableManagement.updateGlobalSettingData(_dbHelper.getWritableDatabase(), holder, Constants.ACCOUNT_ID);
                    } else {
                        VGlobalSettings holder = new VGlobalSettings();
                        holder.setId(custCode.getText().toString());
                        holder.setTax(tax.getText().toString().trim());
                        holder.setAccountId(Constants.ACCOUNT_ID);
                        holder.setDefaultRate(rate.getText().toString().trim());
                        holder.setDateModified(String.valueOf(System.currentTimeMillis()));
                        GlobalSettingTableManagement.insertGlobalSettingData(_dbHelper.getWritableDatabase(), holder);

                    }
                    Toast.makeText(GlobalSetting.this, getResources().getString(R.string.data_saved_successfully), Toast.LENGTH_SHORT).show();

                    GlobalSetting.this.finish();


                }
                break;
            case R.id.cancel:
//                EnableEditableFields.setIsEnabled(false);
//                isEditModeEnabled = false;
//        _editFab.setVisibility(View.VISIBLE);
//                _mBottomLayout.setVisibility(View.GONE);
//                AreaAutocomplete.setFocusable(false);
//                AreaAutocomplete.setFocusableInTouchMode(false);
//                disableKeyBoard();
//                add.setEnabled(false);
                //Clear focus from first field
//                custCode.clearFocus();
//                tax.clearFocus();
//                rate.clearFocus();
                break;
        }

    }

//    private void disableKeyBoard() {
//        //Block default keyboard
//        new EnableEditableFields(custCode, this, inputMethodManager).blockDefaultKeys();
//        new EnableEditableFields(rate, this, inputMethodManager).blockDefaultKeys();
//        new EnableEditableFields(tax, this, inputMethodManager).blockDefaultKeys();
//
//    }

    private int selectedposition = 0;
    int a = 0;

    private void addLabel(String cityarea, final boolean isNewAdded) {

        LinearLayout root = (LinearLayout) findViewById(R.id.linearview);
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

                    if (AccountAreaMapping.deleteArea(_dbHelper.getWritableDatabase(), selectedareacityList.get(a).getAreaId())) {
                        Toast.makeText(GlobalSetting.this, "Area removed!", Toast.LENGTH_SHORT).show();
                        ((ViewGroup) label.getParent()).removeView(label);
                        ((ViewGroup) remove.getParent()).removeView(remove);
                    }

                }
            });
            if (isNewAdded && !selectedAreaId.equals("")) {
                if (!AccountAreaMapping.hasData(_dbHelper.getReadableDatabase(), _areacityList.get(Position).getAreaId())) {
                    AreaAutocomplete.setText("");
                    label.setTextColor(getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    layoutParams.leftMargin = 10;
                    layoutParams.topMargin = 10;
                    root.addView(label, layoutParams);
                    LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Params.leftMargin = 10;
                    Params.topMargin = 10;
                    root.addView(remove, layoutParams);
                    autocomplete_layout.setError(null);
                    selectedAreaId = "";
                    selectedCityId = "";
                    AccountAreaMapping.insertmappedareas(_dbHelper.getWritableDatabase(), _areacityList.get(Position));
                } else {
                    autocomplete_layout.setError("This Area is already Selected");
                    myVib.vibrate(100);
                }
            } else if (!isNewAdded) {
                AreaAutocomplete.setText("");
                autocomplete_layout.setError(null);
                label.setTextColor(getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.leftMargin = 10;
                layoutParams.topMargin = 10;
                root.addView(label, layoutParams);
                LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                Params.leftMargin = 10;
                Params.topMargin = 10;
                root.addView(remove, layoutParams);
            } else {
                autocomplete_layout.setError("Select valid area!");
            }

        }
    }


}
