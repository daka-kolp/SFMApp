package com.dakakolp.sfmapp.data.asynctask;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.dakakolp.sfmapp.R;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class FileReaderAsyncTask extends AsyncTask<Void, String, String> {
    private static final String TAG = "LogFileReaderAsyncTask";
    private File mFileForRead;
    private TextView mTextView;

    public FileReaderAsyncTask(File fileForRead, TextView textView) {
        this.mFileForRead = fileForRead;
        this.mTextView = textView;
    }


    @Override
    protected String doInBackground(Void... voids) {
        if (mFileForRead.length() > 300000)
            cancel(true);
        String list = null;
        FileReader reader = null;
        try {
            reader = new FileReader(mFileForRead);
            StringBuilder stringBuilder = new StringBuilder();
            short size = 256;
            char[] buffer = new char[size];
            int counter;
            while ((counter = reader.read(buffer)) > 0) {
                if (counter < 256) {
                    buffer = Arrays.copyOf(buffer, counter);
                }
                publishProgress(stringBuilder.toString());
                stringBuilder.append(String.copyValueOf(buffer));
            }
            reader.close();
            list = stringBuilder.toString();
        } catch (OutOfMemoryError e) {
            publishProgress("Out of memory error...");
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
        return list;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        mTextView.setText(values[0]);
    }

    @Override
    protected void onCancelled() {
        mTextView.setText(R.string.out_of_memory);
    }
}
