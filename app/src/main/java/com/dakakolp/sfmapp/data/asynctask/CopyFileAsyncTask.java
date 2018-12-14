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

    private static final String TAG = "LogCopyFileAsyncTask";
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
    protected Boolean doInBackground(Void... voids) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File fileTargetLocation = new File(mCopyTo, mFileForCopy.getName());
            in = new FileInputStream(mFileForCopy);
            out = new FileOutputStream(fileTargetLocation);
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
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
