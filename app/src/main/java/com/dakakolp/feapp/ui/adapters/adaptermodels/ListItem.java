package com.dakakolp.feapp.ui.adapters.adaptermodels;

import java.io.File;

public class ListItem {
    private int icon;
    private String title;
    private String subtitle;
    private String ext;
    private String thumb;
    private File file;

    public ListItem() {

    }

    public ListItem(int icon, String title, String subtitle, String ext, String thumb, File file) {
        this.icon = icon;
        this.title = title;
        this.subtitle = subtitle;
        this.ext = ext;
        this.thumb = thumb;
        this.file = file;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
