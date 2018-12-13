package com.dakakolp.sfmapp.data.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFileAsyncTask extends AsyncTask<String, Void, Void> {

    public static final String TAG = "LogCopyFileAsyncTask";

    @Override
    protected Void doInBackground(String... files) {
        try {
            File fileTargetLocation = new File(files[0], files[1]);
            Log.d(TAG, "doInBackground: " + files[0] + " " + files[1]);
            InputStream in = new FileInputStream(files[1]);
            OutputStream out = new FileOutputStream(fileTargetLocation);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        }
        return null;
    }

}
