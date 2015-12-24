package com.milky.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.milky.R;

/**
 * Created by Lead1 on 11/21/2015.
 */
public class DialogScreen {
    private Context _mContext;
    private String _data;

    public DialogScreen(Context context, String data) {
        this._mContext = context;
        this._data = data;
        onCreateDialog(_data);
    }

    protected void onCreateDialog(String stringData) {


        // custom dialog
        final Dialog dialog = new Dialog(_mContext);
        dialog.setContentView(R.layout.clear_bill_popup);
        dialog.setTitle("Clear Bill");

        // set the custom dialog components
        EditText billamount = (EditText) dialog.findViewById(R.id.bill_amount);
        billamount.setText(stringData);
        Button ok = (Button) dialog.findViewById(R.id.ok);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);


        // if button is clicked, close the custom dialog
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // if button is clicked, close the custom dialog
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
