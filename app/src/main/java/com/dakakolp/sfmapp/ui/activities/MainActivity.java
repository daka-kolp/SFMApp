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

    private boolean mIsSelectMode;

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
            case R.id.file_manager_menu_select:

                break;
            case R.id.file_manager_menu_select_all:

                break;
            case R.id.file_manager_menu_clear_selection:

                break;
            case R.id.file_manager_menu_delete:

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem selectAll = menu.findItem(R.id.file_manager_menu_select_all);
        MenuItem clearSelection = menu.findItem(R.id.file_manager_menu_clear_selection);
        MenuItem select = menu.findItem(R.id.file_manager_menu_select);
        MenuItem delete = menu.findItem(R.id.file_manager_menu_delete);

        if (mIsSelectMode) {
            select.setVisible(false);
            selectAll.setVisible(true);
            clearSelection.setVisible(true);
            delete.setVisible(true);
        } else {
            select.setVisible(true);
            clearSelection.setVisible(false);
            selectAll.setVisible(false);
            delete.setVisible(false);
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
