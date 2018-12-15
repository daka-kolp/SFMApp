package com.dakakolp.sfmapp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView;

import com.dakakolp.sfmapp.R;
import com.dakakolp.sfmapp.data.asynctask.FileReaderAsyncTask;

import java.io.File;

public class ReadFileActivity extends AppCompatActivity {

    private TextView mTVFile;
    private ActionBar mActionBar;
    private FileReaderAsyncTask mAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_file);

        mTVFile = findViewById(R.id.file_data);
        mActionBar = getSupportActionBar();
        Intent intent = getIntent();
        String pathToFile = intent.getStringExtra(MainActivity.FILE_FOR_READING);

        if (pathToFile != null){
            File file = new File(pathToFile);
            mAsyncTask = new FileReaderAsyncTask(file, mTVFile);
            mActionBar.setTitle(file.getName());
            mAsyncTask.execute();
        }

        mTVFile.setEditableFactory(new Editable.Factory());
    }
}
