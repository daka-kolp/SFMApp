package com.dakakolp.sfmapp.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dakakolp.sfmapp.R;
import com.dakakolp.sfmapp.ui.fragments.FileManagerFragment;
import com.dakakolp.sfmapp.ui.fragments.interfaces.DocumentSelectListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DocumentSelectListener {

    private ActionBar mActionBar;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private FileManagerFragment mDirectoryFragment;

    private boolean mIsCopyMode;
    private boolean mIsMoveMode;

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

        mDirectoryFragment = new FileManagerFragment();
        mFragmentTransaction.replace(R.id.fragment_container, mDirectoryFragment, mDirectoryFragment.toString());
        mFragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (mDirectoryFragment.onBackPressed()) {
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

            case R.id.file_manager_menu_move:

                break;
            case R.id.file_manager_menu_copy:

                break;
            case R.id.file_manager_menu_cancel:
                if (mIsMoveMode) {


                } else if (mIsCopyMode) {

                }
                break;
            case R.id.file_manager_menu_about:

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem copy = menu.findItem(R.id.file_manager_menu_copy);
        MenuItem move = menu.findItem(R.id.file_manager_menu_move);
        MenuItem cancel = menu.findItem(R.id.file_manager_menu_cancel);
        MenuItem about = menu.findItem(R.id.file_manager_menu_about);
        if (mIsCopyMode) {
            copy.setVisible(true);
            move.setVisible(false);
            cancel.setVisible(true);
            about.setVisible(false);
        } else if (mIsMoveMode) {
            copy.setVisible(false);
            move.setVisible(true);
            cancel.setVisible(true);
            about.setVisible(false);
        } else {
            copy.setVisible(false);
            move.setVisible(false);
            cancel.setVisible(false);
            about.setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void startDocumentSelectActivity(View view, int position) {
        mDirectoryFragment.openPopupMenu(view, position);

    }

    @Override
    public void didSelectFiles(FileManagerFragment activity,
                               ArrayList<String> files) {
        mDirectoryFragment.showInfoBox(files);
    }

    @Override
    public void updateAppBarName(String name) {
        if (name.isEmpty()) {
            mActionBar.setTitle("Simple file manager");
        } else {
            mActionBar.setTitle(name);
        }

    }


}
