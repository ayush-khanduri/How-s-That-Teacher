package com.example.sbuddy.feedback;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence titles[];
    private int NumOfTabs;

    public ViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumOfTabs) {
        super(fm);

        this.titles = mTitles;
        this.NumOfTabs = mNumOfTabs;
    }

    /*returns fragment for every position*/
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                return new Tab1();
            case 1:
                return new Tab2();
            case 2:
                return new Tab3();
            case 3:
                return new Tab4();
        }
        return null;
    }

    /*returns headings for tabs*/
    @Override
    public int getCount() {
        return NumOfTabs;
    }

    /*returns no_of_tabs*/
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}