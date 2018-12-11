package com.dakakolp.feapp.ui.fragments.listeners;

import android.view.View;

import com.dakakolp.feapp.ui.fragments.FileManagerFragment;

import java.util.ArrayList;

public interface DocumentSelectListener {
    void didSelectFiles(FileManagerFragment activity, ArrayList<String> files);

    void updateAppBarName(String name);

    void startDocumentSelectActivity(View view, int position);
}
