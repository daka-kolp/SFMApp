package com.dakakolp.feapp.ui.fragments.helperclasses;

import java.io.File;

public class HistoryEntry {
    private int scrollItem, scrollOffset;
    private File dir;
    private String title;

    public HistoryEntry() {

    }

    public HistoryEntry(int scrollItem, int scrollOffset, File dir, String title) {
        this.scrollItem = scrollItem;
        this.scrollOffset = scrollOffset;
        this.dir = dir;
        this.title = title;
    }

    public int getScrollItem() {
        return scrollItem;
    }

    public void setScrollItem(int scrollItem) {
        this.scrollItem = scrollItem;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffset) {
        this.scrollOffset = scrollOffset;
    }

    public File getDir() {
        return dir;
    }

    public void setDir(File dir) {
        this.dir = dir;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
