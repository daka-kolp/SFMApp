package com.dakakolp.sfmapp.ui.fragments.helpers;

import java.io.File;

public class HistoryEntry {

    private int mScrollItem;
    private int mScrollOffset;
    private File mDirectory;
    private String mTitle;

    public HistoryEntry() {

    }

    public HistoryEntry(int scrollItem, int scrollOffset, File dir, String title) {
        this.mScrollItem = scrollItem;
        this.mScrollOffset = scrollOffset;
        this.mDirectory = dir;
        this.mTitle = title;
    }

    public int getScrollItem() {
        return mScrollItem;
    }

    public void setScrollItem(int scrollItem) {
        this.mScrollItem = scrollItem;
    }

    public int getScrollOffset() {
        return mScrollOffset;
    }

    public void setScrollOffset(int scrollOffset) {
        this.mScrollOffset = scrollOffset;
    }

    public File getDirectory() {
        return mDirectory;
    }

    public void setDirectory(File directory) {
        this.mDirectory = directory;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
}
