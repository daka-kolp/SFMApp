package com.dakakolp.feapp.ui.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dakakolp.feapp.R;
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
import java.util.HashMap;
import java.util.List;

public class FileManagerFragment extends Fragment {

    private static final String LOG_FILE_MANAGER_FRAGMENT = "FileManagerFragment";
    private static String sTitleForUpdate = "";

    private Context mContext;
    private View mFileManagerView;
    private ArrayList<HistoryEntry> mHistory = new ArrayList<>();

    private ListView mListView;
    private ArrayList<ListItem> mItems = new ArrayList<>();
    private FileListAdapter listAdapter;
    private DocumentSelectListener mListener;

    private boolean isReceiverRegistered;
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
                            listRoots();
                        } else {
                            listFiles(mCurrentDir);
                        }
                    } catch (Exception e) {
                        Log.e(LOG_FILE_MANAGER_FRAGMENT, e.toString());
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

    public boolean onBackPress() {
        if (mHistory.size() > 0) {
            HistoryEntry histEntry = mHistory.remove(mHistory.size() - 1);
            sTitleForUpdate = histEntry.getTitle();
            updateTitleName(sTitleForUpdate);
            if (histEntry.getDir() != null) {
                listFiles(histEntry.getDir());
            } else {
                listRoots();
            }
            mListView.setSelectionFromTop(histEntry.getScrollItem(), histEntry.getScrollOffset());
            return false;
        } else {
            return true;
        }
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
        if (isReceiverRegistered) {
            mContext.unregisterReceiver(mBroadcastReceiver);
        }
        mListener = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        if (!isReceiverRegistered) {
            isReceiverRegistered = true;
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
                    chooseItem(i);
                }
            });

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    return chooseLongItem(i);

                }
            });

            listRoots();
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

    private void chooseItem(int i) {
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
                listRoots();
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
                showErrorBox("AccessError");
            } else if (file.length() > mSizeLimit) {
                showErrorBox("FileUploadLimit");
            } else if (!file.isDirectory()) {
                if (mListener != null) {
                    ArrayList<String> files = new ArrayList<String>();
                    files.add("Abs path:\n" + file.getAbsolutePath());
                    files.add("File size: " + StaticHelper.formatFileSize(file.length()));
                    files.add("Can read: " + String.valueOf(file.canRead()));
                    files.add("Can write: " + String.valueOf(file.canWrite()));
                    files.add("Can execute: " + String.valueOf(file.canExecute()));
                    files.add(String.format("Last modified:\n %tD, %<tr", new Date(file.lastModified())));
                    mListener.didSelectFiles(FileManagerFragment.this, files);
                }
            } else {
                showErrorBox("Choose correct file.");
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
                showErrorBox("AccessError");
            } else if (file.length() > mSizeLimit) {
                showErrorBox("FileUploadLimit");
            } else if (!file.isDirectory()) {
                if (mListener != null) {
                    ArrayList<String> files = new ArrayList<String>();
                    files.add("Abs path:\n" + file.getAbsolutePath());
                    files.add("File size: " + StaticHelper.formatFileSize(file.length()));
                    files.add("Can read: " + String.valueOf(file.canRead()));
                    files.add("Can write: " + String.valueOf(file.canWrite()));
                    files.add("Can execute: " + String.valueOf(file.canExecute()));
                    files.add(String.format("Last modified:\n %tD, %<tr", new Date(file.lastModified())));
                    mListener.didSelectFiles(FileManagerFragment.this, files);
                    return true;
                }
            } else {
                showErrorBox("Choose correct file.");
                return false;
            }
        }
        return false;
    }

    private void listRoots() {
        mCurrentDir = null;
        mItems.clear();
        String extStorage = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        ListItem ext = new ListItem();
        if (Build.VERSION.SDK_INT < 9
                || Environment.isExternalStorageRemovable()) {
            ext.setTitle("SdCard");
        } else {
            ext.setTitle("Internal Storage");
        }
        int icon = Build.VERSION.SDK_INT < 9
                || Environment.isExternalStorageRemovable() ? R.drawable.ic_external_storage
                : R.drawable.ic_home_storage;
        ext.setIcon(icon);
        ext.setSubtitle(getRootSubtitle(extStorage));
        ext.setFile(Environment.getExternalStorageDirectory());
        mItems.add(ext);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(
                    "/proc/mounts"));
            String line;
            HashMap<String, ArrayList<String>> aliases = new HashMap<String, ArrayList<String>>();
            ArrayList<String> result = new ArrayList<String>();
            String extDevice = null;
            while ((line = reader.readLine()) != null) {
                if ((!line.contains("/mnt") && !line.contains("/storage") && !line
                        .contains("/sdcard"))
                        || line.contains("asec")
                        || line.contains("tmpfs") || line.contains("none")) {
                    continue;
                }
                String[] info = line.split(" ");
                if (!aliases.containsKey(info[0])) {
                    aliases.put(info[0], new ArrayList<String>());
                }
                aliases.get(info[0]).add(info[1]);
                if (info[1].equals(extStorage)) {
                    extDevice = info[0];
                }
                result.add(info[1]);
            }
            reader.close();
            if (extDevice != null) {
                result.removeAll(aliases.get(extDevice));
                for (String path : result) {
                    try {
                        ListItem item = new ListItem();
                        if (path.toLowerCase().contains("sd")) {
                            ext.setTitle("SdCard");
                        } else {
                            ext.setTitle("ExternalStorage");
                        }
                        item.setIcon(R.drawable.ic_home_storage);
                        item.setSubtitle(getRootSubtitle(path));
                        item.setFile(new File(path));
                        mItems.add(item);
                    } catch (Exception e) {
                        Log.e(LOG_FILE_MANAGER_FRAGMENT, e.toString());
                    }
                }
            }
        } catch (Exception e) {
            Log.e(LOG_FILE_MANAGER_FRAGMENT, e.toString());
        }
        ListItem fs = new ListItem();
        fs.setTitle("/");
        fs.setSubtitle("SystemRoot");
        fs.setIcon(R.drawable.ic_directory);
        fs.setFile(new File("/"));
        mItems.add(fs);

        // try {
        // File telegramPath = new
        // File(Environment.getExternalStorageDirectory(), "Telegram");
        // if (telegramPath.exists()) {
        // fs = new ListItem();
        // fs.title = "Telegram";
        // fs.subtitle = telegramPath.toString();
        // fs.icon = R.drawable.ic_directory;
        // fs.file = telegramPath;
        // mItems.add(fs);
        // }
        // } catch (Exception e) {
        // FileLog.e(LOG_FILE_MANAGER_FRAGMENT", e);
        // }

        // AndroidUtilities.clearDrawableAnimation(mListView);
        // scrolling = true;
        listAdapter.notifyDataSetChanged();
    }

    private boolean listFiles(File dir) {
        if (!dir.canRead()) {
            if (dir.getAbsolutePath().startsWith(
                    Environment.getExternalStorageDirectory().toString())
                    || dir.getAbsolutePath().startsWith("/sdcard")
                    || dir.getAbsolutePath().startsWith("/mnt/sdcard")) {
                if (!Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)
                        && !Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED_READ_ONLY)) {
                    mCurrentDir = dir;
                    mItems.clear();
//                    String state = Environment.getExternalStorageState();
//                    if (Environment.MEDIA_SHARED.equals(state)) {
//
//                    } else {
//
//                    }
                    StaticHelper.clearDrawableAnimation(mListView);
                    // scrolling = true;
                    listAdapter.notifyDataSetChanged();
                    return true;
                }
            }
            showErrorBox("AccessError");
            return false;
        }

        File[] files;
        try {
            files = dir.listFiles();
        } catch (Exception e) {
            showErrorBox(e.getLocalizedMessage());
            return false;
        }
        if (files == null) {
            showErrorBox("UnknownError");
            return false;
        }
        mCurrentDir = dir;
        mItems.clear();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                if (lhs.isDirectory() != rhs.isDirectory()) {
                    return lhs.isDirectory() ? -1 : 1;
                }
                return lhs.getName().compareToIgnoreCase(rhs.getName());
                /*
                 * long lm = lhs.lastModified(); long rm = lhs.lastModified();
                 * if (lm == rm) { return 0; } else if (lm > rm) { return -1; }
                 * else { return 1; }
                 */
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
                String fname = file.getName();
                String[] sp = fname.split("\\.");
                String ext = sp.length > 1 ? sp[sp.length - 1] : "?";
                item.setExt(ext);
                item.setSubtitle(StaticHelper.formatFileSize(file.length()));
//                fname = fname.toLowerCase();
//                if (fname.endsWith(".jpg") || fname.endsWith(".png")
//                        || fname.endsWith(".gif") || fname.endsWith(".jpeg")) {
//                    item.setThumb(file.getAbsolutePath());
//                }
            }
            mItems.add(item);
        }
        ListItem item = new ListItem();
        item.setTitle("...");
        item.setSubtitle("Folder");
        item.setIcon(R.drawable.ic_directory);
        item.setFile(null);
        mItems.add(0, item);
        StaticHelper.clearDrawableAnimation(mListView);
        // scrolling = true;
        listAdapter.notifyDataSetChanged();
        return true;
    }


    public void showErrorBox(String error) {
        if (mContext == null) {
            return;
        }
        new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.app_name))
                .setMessage(error).setPositiveButton("OK", null).show();
    }

    public void showErrorBox(List<String> message) {
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

    private String getRootSubtitle(String path) {
        StatFs stat = new StatFs(path);
        long total = (long) stat.getBlockCount() * (long) stat.getBlockSize();
        long free = (long) stat.getAvailableBlocks()
                * (long) stat.getBlockSize();
        if (total == 0) {
            return "";
        }
        return "Free " + StaticHelper.formatFileSize(free) + " of " + StaticHelper.formatFileSize(total);
    }


}
