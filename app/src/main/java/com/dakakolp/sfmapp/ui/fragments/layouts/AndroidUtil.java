package com.dakakolp.sfmapp.ui.fragments.layouts;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.StateSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dakakolp.sfmapp.SFMApp;

import java.io.File;

public class AndroidUtil {

    private static final String LOG_ANDROID_UTIL = "logAndroidUtil";
    private static float sDensity;
    private static Point sDisplaySize = new Point();

    static {
        sDensity = SFMApp.getApp().getResources().getDisplayMetrics().density;
        checkDisplaySize();
    }

    public static int dp(float value) {
        return (int) Math.ceil(sDensity * value);
    }

    private static void checkDisplaySize() {
        try {
            WindowManager manager = (WindowManager) SFMApp.getApp().getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    display.getSize(sDisplaySize);
                    Log.d(LOG_ANDROID_UTIL, "display size = " + sDisplaySize.x + " " + sDisplaySize.y);
                }
            }
        } catch (Exception e) {
            Log.e(LOG_ANDROID_UTIL, e.getMessage());
        }
    }

    public static void clearDrawableAnimation(View view) {
        if (Build.VERSION.SDK_INT < 21 || view == null) {
            return;
        }
        Drawable drawable;
        if (view instanceof ListView) {
            drawable = ((ListView) view).getSelector();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
            }
        } else {
            drawable = view.getBackground();
            if (drawable != null) {
                drawable.setState(StateSet.NOTHING);
                drawable.jumpToCurrentState();
            }
        }
    }

    public static EditText initEditText(Context context, String file) {
        EditText inputNewLocation = new EditText(context);
        if (file != null)
            inputNewLocation.setText(file);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        inputNewLocation.setLayoutParams(layoutParams);
        return inputNewLocation;
    }
}
