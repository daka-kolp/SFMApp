package com.dakakolp.sfmapp.ui.fragments.interfaces;

import android.view.View;

import com.dakakolp.sfmapp.ui.fragments.FileManagerFragment;

import java.io.File;
import java.util.ArrayList;

public interface DocumentSelectListener {

    void didSelectFiles(FileManagerFragment activity, ArrayList<String> files);

    void updateAppBarName(String name);

    void startDocumentSelectActivity(View view, int position);

    void makeDir();

    void openFileReaderActivity(File file);
}
