/*
 * This is the source code of Telegram for Android v. 1.7.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2014.
 */

package com.dakakolp.sfmapp.ui.fragments.layouts;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dakakolp.sfmapp.R;

public class DetailCellLayout extends FrameLayout {

    private TextView mTVTitle;
    private TextView mTVSubtitle;
    private TextView mTVTypeForIcon;
    private ImageView mIVIcon;
    private CheckBox mCBSelectItem;

    public DetailCellLayout(Context context) {
        super(context);

        mTVTitle = new TextView(context);
        mTVTitle.setTextColor(getResources().getColor(R.color.text_title));
        mTVTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTVTitle.setLines(1);
        mTVTitle.setMaxLines(1);
        mTVTitle.setSingleLine(true);
        mTVTitle.setGravity(Gravity.START);
        addView(mTVTitle);
        LayoutParams layoutParams = (LayoutParams) mTVTitle.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.topMargin = AndroidUtil.dp(10);
        layoutParams.leftMargin = AndroidUtil.dp(71);
        layoutParams.rightMargin = AndroidUtil.dp(56);
        layoutParams.gravity = Gravity.START;
        mTVTitle.setLayoutParams(layoutParams);

        mTVSubtitle = new TextView(context);
        mTVSubtitle.setTextColor(getResources().getColor(R.color.text_subtitle));
        mTVSubtitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
        mTVSubtitle.setLines(1);
        mTVSubtitle.setMaxLines(1);
        mTVSubtitle.setSingleLine(true);
        mTVSubtitle.setGravity(Gravity.START);
        addView(mTVSubtitle);
        layoutParams = (LayoutParams) mTVSubtitle.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.topMargin = AndroidUtil.dp(35);
        layoutParams.leftMargin = AndroidUtil.dp(71);
        layoutParams.rightMargin = AndroidUtil.dp(56);
        layoutParams.gravity = Gravity.START;
        mTVSubtitle.setLayoutParams(layoutParams);

        mTVTypeForIcon = new TextView(context);
        mTVTypeForIcon.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
        mTVTypeForIcon.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTVTypeForIcon.setGravity(Gravity.CENTER);
        mTVTypeForIcon.setSingleLine(true);
        mTVTypeForIcon.setTextColor(getResources().getColor(R.color.color_primary_dark));
        mTVTypeForIcon.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        mTVTypeForIcon.setTypeface(Typeface.DEFAULT_BOLD);
        addView(mTVTypeForIcon);
        layoutParams = (LayoutParams) mTVTypeForIcon.getLayoutParams();
        layoutParams.width = AndroidUtil.dp(40);
        layoutParams.height = AndroidUtil.dp(40);
        layoutParams.leftMargin = AndroidUtil.dp(16);
        layoutParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        mTVTypeForIcon.setLayoutParams(layoutParams);

        mIVIcon = new ImageView(context);
        addView(mIVIcon);
        layoutParams = (LayoutParams) mIVIcon.getLayoutParams();
        layoutParams.width = AndroidUtil.dp(40);
        layoutParams.height = AndroidUtil.dp(40);
        layoutParams.leftMargin = AndroidUtil.dp(16);
        layoutParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL;
        mIVIcon.setLayoutParams(layoutParams);

        mCBSelectItem = new CheckBox(context);
        mCBSelectItem.setVisibility(GONE);
        addView(mCBSelectItem);
        layoutParams = (LayoutParams) mCBSelectItem.getLayoutParams();
        layoutParams.width = LayoutParams.WRAP_CONTENT;
        layoutParams.height = LayoutParams.WRAP_CONTENT;
        layoutParams.rightMargin = AndroidUtil.dp(12);
        layoutParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL;
        mCBSelectItem.setLayoutParams(layoutParams);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(AndroidUtil.dp(64), MeasureSpec.EXACTLY));
    }

    public void setDataItem(String text, String value, String type, int resId) {
        mTVTitle.setText(text);
        mTVSubtitle.setText(value);
        if (type != null) {
            mTVTypeForIcon.setVisibility(VISIBLE);
            mTVTypeForIcon.setText(type);
        } else {
            mTVTypeForIcon.setVisibility(GONE);
        }
        if (resId != 0) {
            mIVIcon.setImageResource(resId);
            mIVIcon.setVisibility(VISIBLE);
        } else {
            mIVIcon.setVisibility(GONE);
        }
    }



    public void setChecked(boolean checked, boolean animated) {
        if (mCBSelectItem.getVisibility() != VISIBLE) {
            mCBSelectItem.setVisibility(VISIBLE);
        }
        mCBSelectItem.setChecked(checked);
    }
}
