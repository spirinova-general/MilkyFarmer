package com.milky.utils;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.milky.R;

/**
 * Created by Neha on 12/2/2015.
 */
public class TextValidationMessage implements TextWatcher {
    private TextInputLayout _textInputLayout;
    private Context _context;
    private boolean _isPhone = false;
    private static boolean isValid = false;
    private boolean _rate, _quantity, _balance,_tax;
    private EditText phoneText;

    public TextValidationMessage(final EditText phoneText, final TextInputLayout txtInputlt, final Context con, final boolean phone, final boolean rate, final boolean quantity, final boolean balance,final boolean tax) {
        this._textInputLayout = txtInputlt;
        this._context = con;
        this._isPhone = phone;
        this.phoneText = phoneText;
        this._rate = rate;
        this._quantity = quantity;
        this._balance = balance;
        this._tax=tax;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (s.length() > 0) {
            if (_isPhone && !isValidMobile(s.toString())) {
//                _textInputLayout.setErrorEnabled(true);
                _textInputLayout.setError(_context.getResources().getString(R.string.invalid_phone_no));
                isValid = false;
            } else {
//                _textInputLayout.setErrorEnabled(false);
                _textInputLayout.setError(null);
                isValid = true;

            }
        } else {
//            _textInputLayout.setErrorEnabled(true);
            _textInputLayout.setError(_context.getResources().getString(R.string.field_cant_empty));
            isValid = false;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if (_isPhone && !isValidMobile(s.toString())) {
//                _textInputLayout.setErrorEnabled(true);
                _textInputLayout.setError(_context.getResources().getString(R.string.invalid_phone_no));
                isValid = false;
            } else {
//                _textInputLayout.setErrorEnabled(false);
                _textInputLayout.setError(null);
                isValid = true;
            }
        } else {
//            _textInputLayout.setErrorEnabled(true);
            _textInputLayout.setError(_context.getResources().getString(R.string.field_cant_empty));
            isValid = false;
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (editable.length() > 0) {
            if (_isPhone && !isValidMobile(editable.toString())) {
//                _textInputLayout.setErrorEnabled(true);
                _textInputLayout.setError(_context.getResources().getString(R.string.invalid_phone_no));
                isValid = false;
            } else if (_rate && editable.toString().equals(".")) {
                _textInputLayout.setError(_context.getResources().getString(R.string.enter_valid_rate));

            } else if (_quantity && editable.toString().equals(".")) {
                _textInputLayout.setError(_context.getResources().getString(R.string.enter_valid_quantity));

            } else if (_balance && editable.toString().equals(".")) {
                _textInputLayout.setError(_context.getResources().getString(R.string.enter_valid_balance));

            }
            else if (_tax && editable.toString().equals(".")) {
                _textInputLayout.setError(_context.getResources().getString(R.string.enter_valid_tax));

            }else {
//                _textInputLayout.setErrorEnabled(false);
                _textInputLayout.setError(null);
                isValid = true;


            }
        } else {
//            _textInputLayout.setErrorEnabled(true);
            _textInputLayout.setError(_context.getResources().getString(R.string.field_cant_empty));
            isValid = false;
        }

    }

    public boolean isValidMobile(String text) {

        if (android.util.Patterns.PHONE.matcher(text).matches() && text.length() == 10) {
            return true;
        }
        return false;
    }

}
