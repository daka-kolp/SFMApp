/*
 * This is the source code of Telegram for Android v. 1.7.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.dakakolp.feapp.ui.fragments.helperclasses.layouts;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dakakolp.feapp.R;

public class DetailCellLayout extends FrameLayout {

    private TextView textView;
    private TextView valueTextView;
    private TextView typeTextView;
    private ImageView imageView;
    private CheckBox checkBox;

    public DetailCellLayout(Context context) {
        super(context);

        textView = new TextView(context);
        textView.setTextColor(0xff212121);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setLines(1);
        textView.setMaxLines(1);
        textView.setSingleLine(true);
        textView.setGravity(Gravity.LEFT);
        addView(textView);
        LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.topMargin = AndroidUtil.dp(10);
        layoutParams.leftMargin = AndroidUtil.dp(71);
        layoutParams.rightMargin = AndroidUtil.dp(16);
        layoutParams.gravity = Gravity.LEFT;
        textView.setLayoutParams(layoutParams);

        valueTextView = new TextView(context);
        valueTextView.setTextColor(0xff8a8a8a);
        valueTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        valueTextView.setLines(1);
        valueTextView.setMaxLines(1);
        valueTextView.setSingleLine(true);
        valueTextView.setGravity(Gravity.LEFT);
        addView(valueTextView);
        layoutParams = (LayoutParams) valueTextView.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.topMargin = AndroidUtil.dp(35);
        layoutParams.leftMargin = AndroidUtil.dp(71);
        layoutParams.rightMargin = AndroidUtil.dp(16);
        layoutParams.gravity = Gravity.LEFT;
        valueTextView.setLayoutParams(layoutParams);

        typeTextView = new TextView(context);
        typeTextView.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
        typeTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        typeTextView.setGravity(Gravity.CENTER);
        typeTextView.setSingleLine(true);
        typeTextView.setTextColor(getResources().getColor(R.color.color_primary_dark));
        typeTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        typeTextView.setTypeface(Typeface.DEFAULT_BOLD);
        addView(typeTextView);
        layoutParams = (LayoutParams) typeTextView.getLayoutParams();
        layoutParams.width = AndroidUtil.dp(40);
        layoutParams.height = AndroidUtil.dp(40);
        layoutParams.leftMargin = AndroidUtil.dp(16);
        layoutParams.rightMargin = AndroidUtil.dp(0);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        typeTextView.setLayoutParams(layoutParams);

        imageView = new ImageView(context);
        addView(imageView);
        layoutParams = (LayoutParams) imageView.getLayoutParams();
        layoutParams.width = AndroidUtil.dp(40);
        layoutParams.height = AndroidUtil.dp(40);
        layoutParams.leftMargin = AndroidUtil.dp(16);
        layoutParams.rightMargin = AndroidUtil.dp(0);
        layoutParams.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        imageView.setLayoutParams(layoutParams);

        checkBox = new CheckBox(context);
        checkBox.setVisibility(GONE);
        addView(checkBox);
        layoutParams = (LayoutParams) checkBox.getLayoutParams();
        layoutParams.width = AndroidUtil.dp(22);
        layoutParams.height = AndroidUtil.dp(22);
        layoutParams.topMargin = AndroidUtil.dp(34);
        layoutParams.leftMargin = AndroidUtil.dp(38) ;
        layoutParams.rightMargin = 0;
        layoutParams.gravity = Gravity.LEFT;
        checkBox.setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtil.dp(64), MeasureSpec.EXACTLY));
    }

    public void setDataItem(String text, String value, String type, String thumb, int resId) {
        textView.setText(text);
        valueTextView.setText(value);
        if (type != null) {
            typeTextView.setVisibility(VISIBLE);
            typeTextView.setText(type);
        } else {
            typeTextView.setVisibility(GONE);
        }
        if (thumb != null || resId != 0) {
            if (thumb != null) {
//                imageView.setImage(thumb, "40_40", null);
            } else  {
                imageView.setImageResource(resId);
            }
            imageView.setVisibility(VISIBLE);
        } else {
            imageView.setVisibility(GONE);
        }
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checkBox.getVisibility() != VISIBLE) {
            checkBox.setVisibility(VISIBLE);
        }
        checkBox.setChecked(checked);
    }
}
