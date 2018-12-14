package com.dakakolp.sfmapp.data.asynctask;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.Editable;
import android.util.Log;
import android.widget.TextView;

import com.dakakolp.sfmapp.R;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileReaderAsyncTask extends AsyncTask<Void, String, Void> {
    private static final String TAG = "LogFileReaderAsyncTask";
    private File mFileForRead;
    @SuppressLint("StaticFieldLeak")
    private TextView mTextView;

    public FileReaderAsyncTask(File fileForRead, TextView textView) {
        this.mFileForRead = fileForRead;
        this.mTextView = textView;
        textView.setText(null, TextView.BufferType.EDITABLE);
    }


    @Override
    protected Void doInBackground(Void... voids) {

        FileReader reader = null;
        try {
            reader = new FileReader(mFileForRead);
            short size = 256;
            char[] buffer = new char[size];
            int counter;
            while ((counter = reader.read(buffer)) > 0) {
                if (counter < 256) {
                    buffer = Arrays.copyOf(buffer, counter);
                }
                String string = String.valueOf(buffer);
                publishProgress(string);
            }
            reader.close();
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        ((Editable) mTextView.getText()).append(values[0]);
    }

}
