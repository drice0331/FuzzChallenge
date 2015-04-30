package com.example.drice.fuzzchallenge.ui;


import java.util.Locale;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;


import com.example.drice.fuzzchallenge.R;
import com.example.drice.fuzzchallenge.model.ListviewContent;
import com.example.drice.fuzzchallenge.util.Constants;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener, BaseFragment.Callbacks {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public String [] tabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                Log.d("MainActivity", "pos " + position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    private void init() {
        tabTitles = getResources().getStringArray(R.array.tab_titles);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
        BaseFragment f = (BaseFragment)mSectionsPagerAdapter.getItem(tab.getPosition());

        if (f == null) {
            // If not, instantiate and add it to the activity
            f = BaseFragment.newInstance(tabTitles[tab.getPosition()]);
            ft.add(android.R.id.content, f, tabTitles[tab.getPosition()]);
        } else {
            // If it exists, simply attach it in order to show it
            ft.attach(f);
        }

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        BaseFragment f = (BaseFragment)mSectionsPagerAdapter.getItem(tab.getPosition());
        if (f != null) {
            Log.d("MainActivity", "detaching fragment");
            // Detach the fragment, because another one is being attached
            ft.detach(f);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * Callback method when selecting item in listview from Fragment
     * @param item
     */
    @Override
    public void onListItemSelected(ListviewContent item) {
        if(item.getType().equals(Constants.CONTENT_TYPE_KEY_IMAGE)) {
            Intent intent = new Intent(this, FullImageActivity.class);
            intent.putExtra(Constants.DATA_KEY, item.getData());
            startActivity(intent);
        } else if(item.getType().equals(Constants.CONTENT_TYPE_KEY_TEXT)) {
            Intent intent = new Intent(this, TextWebViewActivity.class);
            startActivity(intent);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //getItem is called to instantiate the fragment for the given page.

            switch(position) {
                case 0:
                    return BaseFragment.newInstance(Constants.ALL_KEY);
                case 1:
                    return BaseFragment.newInstance(Constants.CONTENT_TYPE_KEY_TEXT);
                case 2:
                    return BaseFragment.newInstance(Constants.CONTENT_TYPE_KEY_IMAGE);
                default:
                    return BaseFragment.newInstance(Constants.ALL_KEY);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            if(tabTitles != null) {
                return tabTitles[position].toUpperCase(l);
            }
            return null;
        }
    }

}
