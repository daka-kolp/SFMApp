package com.dakakolp.sfmapp.data.asynctask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyFileAsyncTask extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = "LogCopyFileAsyncTask";
    private String mCopyTo;
    private File mFileForCopy;
    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public CopyFileAsyncTask(String copyTo, File fileForCopy, Context context) {
        this.mCopyTo = copyTo;
        this.mFileForCopy = fileForCopy;
        this.mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... files) {
        try {
            File fileTargetLocation = new File(mCopyTo, mFileForCopy.getName());
            Log.d(TAG, "doInBackground: " + mCopyTo + " " + mFileForCopy.getName());
            InputStream in = new FileInputStream(mFileForCopy);
            OutputStream out = new FileOutputStream(fileTargetLocation);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            return true;
        } catch (IOException e) {
            Log.d(TAG, "doInBackground: " + e.getMessage());
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aVoid) {
        if (aVoid) {
            Toast.makeText(mContext, "You have copied the file to " + mCopyTo, Toast.LENGTH_SHORT).show();
        } else {
            // TODO: 12/11/18 doesn't want work with SDCard
            Toast.makeText(mContext, "You haven't copied the file", Toast.LENGTH_SHORT).show();
        }
    }
}
