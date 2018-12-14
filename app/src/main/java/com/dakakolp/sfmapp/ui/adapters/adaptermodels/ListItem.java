package com.dakakolp.sfmapp.ui.adapters.adaptermodels;
import java.io.File;
public class ListItem {
    private int mIcon;
    private String mTitle;
    private String mSubtitle;
    private String mExtension;
    private File mFile;
    public ListItem(int icon, String title, String subtitle, String ext, File file) {
        this.mIcon = icon;
        this.mTitle = title;
        this.mSubtitle = subtitle;
        this.mExtension = ext;
        this.mFile = file;
    }
    public int getIcon() {
        return mIcon;
    }
    public void setIcon(int icon) {
        this.mIcon = icon;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        this.mTitle = title;
    }
    public String getSubtitle() {
        return mSubtitle;
    }
    public void setSubtitle(String subtitle) {
        this.mSubtitle = subtitle;
    }
    public String getExtension() {
        return mExtension;
    }
    public void setExtension(String extension) {
        this.mExtension = extension;
    }
    public File getFile() {
        return mFile;
    }
    public void setFile(File file) {
        this.mFile = file;
    }
}
