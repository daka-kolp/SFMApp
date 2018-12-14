package com.dakakolp.sfmapp.ui.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dakakolp.sfmapp.R;
import com.dakakolp.sfmapp.ui.fragments.FileManagerFragment;
import com.dakakolp.sfmapp.ui.fragments.interfaces.DocumentSelectListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DocumentSelectListener {

    public static final String FILE_FOR_READING = "FILE_FOR_READING";

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FileManagerFragment mFMFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mActionBar = getSupportActionBar();

        if (mActionBar != null) {
            mActionBar.setTitle("Simple file manager");
        }


        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFMFragment = new FileManagerFragment();
        mFragmentTransaction.replace(R.id.fragment_container, mFMFragment, mFMFragment.toString());
        mFragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (mFMFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.file_manager_menu_create_new_folder:
                makeDir();
                break;
            case R.id.file_manager_menu_copy_path:
                copyCurrentDirPathToBuffer();
                break;

            case R.id.file_manager_menu_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem createNewFolder = menu.findItem(R.id.file_manager_menu_create_new_folder);
        MenuItem copyPathToFolder = menu.findItem(R.id.file_manager_menu_copy_path);
        if (mFMFragment.getCurrentDir() != null) {
            createNewFolder.setVisible(true);
            copyPathToFolder.setVisible(true);
        } else {
            createNewFolder.setVisible(false);
            copyPathToFolder.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void startDocumentSelectActivity(View view, int position) {
        mFMFragment.openPopupMenu(view, position);

    }

    @Override
    public void didSelectFiles(FileManagerFragment activity,
                               ArrayList<String> files) {
        mFMFragment.showInfoDialog(files);
    }

    @Override
    public void updateAppBarName(String name) {
        if (name.isEmpty()) {
            mActionBar.setTitle("Simple file manager");
        } else {
            mActionBar.setTitle(name);
        }

    }

    @Override
    public void openFileReaderActivity(File file) {
        Intent intent = new Intent(this, ReadFileActivity.class);
        intent.putExtra(FILE_FOR_READING, file.getAbsolutePath());
        startActivity(intent);
    }


    public void makeDir() {
        mFMFragment.mkDir();
    }

    public static final String PATH_TO_CURRENT = "path to file";

    public void copyCurrentDirPathToBuffer() {
        ClipboardManager clipboardManager =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData;
        String path = mFMFragment.getCurrentDir().getAbsolutePath();
        clipData = ClipData.newPlainText(PATH_TO_CURRENT, path);
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(this, "AbsPath has been copied to buffer", Toast.LENGTH_SHORT).show();
    }
}
