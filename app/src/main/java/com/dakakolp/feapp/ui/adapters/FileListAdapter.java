package com.dakakolp.feapp.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.dakakolp.feapp.ui.adapters.adaptermodels.ListItem;
import com.dakakolp.feapp.ui.fragments.helperclasses.layouts.DetailCellLayout;

import java.util.List;


public class FileListAdapter extends BaseFragmentAdapter {
    private Context mContext;
    private List<ListItem> mItems;

    public FileListAdapter(Context context, List<ListItem> items) {
        mContext = context;
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int pos) {
        return mItems.get(pos).getSubtitle().length() > 0 ? 0 : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new DetailCellLayout(mContext);
        }
        DetailCellLayout textDetailCell = (DetailCellLayout) convertView;
        ListItem item = mItems.get(position);
        if (item.getIcon() != 0) {
            textDetailCell.setDataItem(item.getTitle(),
                            item.getSubtitle(), null,  item.getIcon());
        } else {
            String type = item.getExtension().toUpperCase().substring(0,
                    Math.min(item.getExtension().length(), 4));
            textDetailCell.setDataItem(item.getTitle(),
                            item.getSubtitle(), type,  0);
        }
        // if (item.file != null && actionBar.isActionModeShowed()) {
        // textDetailCell.setChecked(selectedFiles.containsKey(item.file.toString()),
        // !scrolling);
        // } else {
        // textDetailCell.setChecked(false, !scrolling);
        // }
        return convertView;
    }
}
