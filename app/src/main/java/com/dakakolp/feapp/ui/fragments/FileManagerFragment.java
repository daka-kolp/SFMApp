package com.dakakolp.feapp.ui.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dakakolp.feapp.R;
import com.dakakolp.feapp.ui.activities.MainActivity;
import com.dakakolp.feapp.ui.adapters.FileListAdapter;
import com.dakakolp.feapp.ui.adapters.adaptermodels.ListItem;
import com.dakakolp.feapp.ui.fragments.helperclasses.HistoryEntry;
import com.dakakolp.feapp.ui.fragments.listeners.DocumentSelectListener;
import com.dakakolp.feapp.utils.StaticHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class FileManagerFragment extends Fragment {

    private static final String LOG_FILE_MANAGER_FRAGMENT = "LogFileManagerFragment";
    private static String sTitleForUpdate = "";

    private Context mContext;
    private View mFileManagerView;
    private ArrayList<HistoryEntry> mHistory = new ArrayList<>();

    private ListView mListView;
    private ArrayList<ListItem> mItems = new ArrayList<>();
    private FileListAdapter listAdapter;
    private DocumentSelectListener mListener;

    private boolean isBroadcastReceiverRegistered;
    private File mCurrentDir;

    private long mSizeLimit = 1024 * 1024 * 1024;

    //private HashMap<String, ListItem> selectedFiles = new HashMap<String, ListItem>();

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg, Intent intent) {
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        if (mCurrentDir == null) {
                            listRootFolders();
                        } else {
                            listFiles(mCurrentDir);
                        }
                    } catch (Exception e) {
                        Log.e(LOG_FILE_MANAGER_FRAGMENT, e.getMessage());
                    }
                }
            };
            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                mListView.postDelayed(r, 1000);
            } else {
                r.run();
            }
        }
    };

    public FileManagerFragment() {

    }

    private void updateTitleName(String title) {
        if (mListener != null) {
            mListener.updateAppBarName(title);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof DocumentSelectListener) {
            mListener = (DocumentSelectListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DocumentSelectListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBroadcastReceiverRegistered) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
        mListener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        if (!isBroadcastReceiverRegistered) {
            isBroadcastReceiverRegistered = true;
            IntentFilter filter = getIntentFilter();
            mContext.registerReceiver(mBroadcastReceiver, filter);
        }

        if (mFileManagerView == null) {
            mFileManagerView = inflater.inflate(
                    R.layout.document_select_layout,
                    container,
                    false);

            listAdapter = new FileListAdapter(mContext, mItems);
            mListView = mFileManagerView.findViewById(R.id.listView);
            mListView.setAdapter(listAdapter);

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    chooseItem(view, i);
                }
            });

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    return chooseLongItem(i);
                }
            });
            listRootFolders();
        } else {
            ViewGroup parent = (ViewGroup) mFileManagerView.getParent();
            if (parent != null) {
                parent.removeView(mFileManagerView);
            }
        }
        return mFileManagerView;
    }

    private IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        filter.addAction(Intent.ACTION_MEDIA_CHECKING);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addAction(Intent.ACTION_MEDIA_NOFS);
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_SHARED);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTABLE);
        filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filter.addDataScheme("file");
        return filter;
    }

    private void chooseItem(View view, int i) {
        if (i < 0 || i >= mItems.size()) {
            return;
        }
        HistoryEntry history;
        ListItem item = mItems.get(i);
        File file = item.getFile();
        //if return from folder, change title
        if (file == null) {
            history = mHistory.remove(mHistory.size() - 1);
            sTitleForUpdate = history.getTitle();
            updateTitleName(sTitleForUpdate);
            if (history.getDir() != null) {
                listFiles(history.getDir());
            } else {
                listRootFolders();
            }
            //where setSelectionFromTop(int position = scrollItem, int y = scrollOffset)
            mListView.setSelectionFromTop(history.getScrollItem(), history.getScrollOffset());
        } else if (file.isDirectory()) {
            history = new HistoryEntry();
            history.setScrollItem(mListView.getFirstVisiblePosition());
            history.setScrollOffset(mListView.getChildAt(0).getTop());
            history.setDir(mCurrentDir);
            history.setTitle(sTitleForUpdate);
            updateTitleName(sTitleForUpdate);
            if (!listFiles(file)) {
                return;
            }
            mHistory.add(history);
            sTitleForUpdate = item.getTitle();
            updateTitleName(sTitleForUpdate);
            mListView.setSelection(0);
        } else {
            if (!file.canRead()) {
                showInfoBox("AccessError");
            } else if (file.length() > mSizeLimit) {
                showInfoBox("FileUploadLimit");
            } else if (!file.isDirectory()) {
                if (mListener != null) {
                    mListener.startDocumentSelectActivity(view, i);
                }
            } else {
                showInfoBox("Choose correct file");
            }

        }
    }

    private boolean chooseLongItem(int i) {
        if (i < 0 || i >= mItems.size()) {
            return false;
        }
        ListItem item = mItems.get(i);
        File file = item.getFile();
        if (file != null && !file.isDirectory()) {
            if (!file.canRead()) {
                showInfoBox("AccessError");
            } else if (file.length() > mSizeLimit) {
                showInfoBox("FileUploadLimit");
            } else if (!file.isDirectory()) {
                if (mListener != null) {
                    mListener.didSelectFiles(FileManagerFragment.this, getDescriptionFile(file));
                    return true;
                }
            } else {
                showInfoBox("Choose correct file.");
                return false;
            }
        }
        return false;
    }

    private void showInfoBox(String error) {
        if (mContext == null) {
            return;
        }
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage(error).setPositiveButton("OK", null).show();
    }

    private ArrayList<String> getDescriptionFile(File file) {
        ArrayList<String> files = new ArrayList<>();
        files.add("Name: " + file.getName());
        files.add("Abs path:\n" + file.getAbsolutePath());
        files.add("File size: " + StaticHelper.formatFileSize(file.length()));
        files.add("Can read: " + String.valueOf(file.canRead()));
        files.add("Can write: " + String.valueOf(file.canWrite()));
        files.add("Can execute: " + String.valueOf(file.canExecute()));
        files.add(String.format("Last modified:\n %tD, %<tr", new Date(file.lastModified())));
        return files;
    }

    private void listRootFolders() {
        mCurrentDir = null;
        mItems.clear();

        String extStorageAbsPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//intStore
        initInternalStore(extStorageAbsPath);
//extStore
        initExternalStore(extStorageAbsPath);
//root
        ListItem fs = new ListItem();
        fs.setTitle("/");
        fs.setSubtitle("SystemRoot");
        fs.setIcon(R.drawable.ic_directory);
        fs.setFile(new File("/"));
        mItems.add(fs);
        listAdapter.notifyDataSetChanged();
    }

    private void initInternalStore(String extStorageAbsPath) {
        ListItem internalStorageItem = new ListItem();
        internalStorageItem.setTitle("InternalStorage");
        internalStorageItem.setIcon(R.drawable.ic_home_storage);
        internalStorageItem.setSubtitle(getInfoAboutFileSystemSpace(extStorageAbsPath));
        internalStorageItem.setFile(Environment.getExternalStorageDirectory());
        mItems.add(internalStorageItem);
    }

    private void initExternalStore(String extStorageAbsPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("/proc/mounts"));
            String infoAboutMountDevices;
            ArrayList<String> result = new ArrayList<>();

            while ((infoAboutMountDevices = reader.readLine()) != null) {
                if ((!infoAboutMountDevices.contains("/storage"))
                        || infoAboutMountDevices.contains("asec")
                        || infoAboutMountDevices.contains("tmpfs")
                        || infoAboutMountDevices.contains("none")) {
                    continue;
                }
                String[] info = infoAboutMountDevices.split(" ");

                if (!extStorageAbsPath.contains(info[1])) {
                    result.add(info[1]);
                }

            }
            reader.close();

            for (String path : result) {
                ListItem item = new ListItem();
                item.setTitle("ExternalStorage");
                item.setIcon(R.drawable.ic_external_storage);
                item.setSubtitle(getInfoAboutFileSystemSpace(path));
                item.setFile(new File(path));
                mItems.add(item);
            }
        } catch (Exception e) {
            Log.e(LOG_FILE_MANAGER_FRAGMENT, e.getMessage());
        }
    }

    private boolean listFiles(File dir) {
        if (!dir.canRead()) {
            if (dir.getAbsolutePath().startsWith(Environment.getExternalStorageDirectory().toString())
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
                mCurrentDir = dir;
                mItems.clear();
                StaticHelper.clearDrawableAnimation(mListView);
                listAdapter.notifyDataSetChanged();
                return true;
            }
            showInfoBox("DirectoryAccessError");
            return false;
        }

        File[] files;
        try {
            files = dir.listFiles();
        } catch (Exception e) {
            showInfoBox(e.getLocalizedMessage());
            return false;
        }
        if (files == null) {
            showInfoBox("UnknownError");
            return false;
        }
        mCurrentDir = dir;
        mItems.clear();

        //sort folders and files
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }
                return lhs.getName().compareToIgnoreCase(rhs.getName());
            }
        });

        for (File file : files) {
            if (file.getName().startsWith(".")) {
                continue;
            }
            ListItem item = new ListItem();
            item.setTitle(file.getName());
            item.setFile(file);
            if (file.isDirectory()) {
                item.setIcon(R.drawable.ic_directory);
                item.setSubtitle("Folder");
            } else {
                String fileName = file.getName();
                String[] sp = fileName.split("\\.");
                String extension = sp.length > 1 ? sp[sp.length - 1] : "?";
                item.setExtension(extension);
                item.setSubtitle(StaticHelper.formatFileSize(file.length()));
            }
            mItems.add(item);
        }

//add item-back to last folder
        ListItem item = new ListItem();
        item.setTitle("<-");
        item.setSubtitle("");
        item.setIcon(R.drawable.ic_directory);
        item.setFile(null);
        mItems.add(0, item);
        StaticHelper.clearDrawableAnimation(mListView);

        listAdapter.notifyDataSetChanged();
        return true;
    }

    private String getInfoAboutFileSystemSpace(String path) {
        StatFs stat = new StatFs(path);
        long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
        long free = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
        if (total == 0) {
            return null;
        }
        return "Free " + StaticHelper.formatFileSize(free) + " of " + StaticHelper.formatFileSize(total);
    }

    public void showInfoBox(List<String> message) {
        if (mContext == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : message) {
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage(stringBuilder.toString())
                .setPositiveButton("OK", null).show();
    }

    public void openPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.inflate(R.menu.menu_item);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.file_item_info:
                        Log.d(MainActivity.class.getName(), "file_item_info ");
                        return true;
                    case R.id.file_item_open:
                        Log.d(MainActivity.class.getName(), "file_item_open ");
                        return true;
                    case R.id.file_item_rename:
                        Log.d(MainActivity.class.getName(), "file_item_rename ");
                        return true;
                    case R.id.file_item_delete:
                        Log.d(MainActivity.class.getName(), "file_item_delete ");
                        return true;

                }
                return false;
            }
        });
        popupMenu.show();
    }

    public boolean onBackPressed() {
        if (mHistory.size() > 0) {
            HistoryEntry histEntry = mHistory.remove(mHistory.size() - 1);
            sTitleForUpdate = histEntry.getTitle();
            updateTitleName(sTitleForUpdate);
            if (histEntry.getDir() != null) {
                listFiles(histEntry.getDir());
            } else {
                listRootFolders();
            }
            mListView.setSelectionFromTop(histEntry.getScrollItem(), histEntry.getScrollOffset());
            return false;
        } else {
            return true;
        }
    }
}
