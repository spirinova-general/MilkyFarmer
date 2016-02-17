package com.milky;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;

public class ExpiryActivity extends Activity {
private Button _ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expiry);
        initResources();
    }
    private void initResources()
    {
        _ok = (Button) findViewById(R.id.ok);
        _ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
