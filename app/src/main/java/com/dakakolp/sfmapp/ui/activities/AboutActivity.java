package com.dakakolp.sfmapp.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dakakolp.sfmapp.BuildConfig;
import com.dakakolp.sfmapp.R;

public class AboutActivity extends AppCompatActivity {

    private TextView mVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mVersion = findViewById(R.id.app_version);
        mVersion.setText(BuildConfig.VERSION_NAME);
    }
}
