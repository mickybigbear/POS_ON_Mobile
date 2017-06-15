package com.example.sin.projectone.payment;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by Micky on 6/15/2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public int numSection = 0;
    public SectionsPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        numSection = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return ScanPayment2.newInstance("","");
        } else{
            return ScanPayment2.newInstance("","");
        }
    }

    @Override
    public int getCount() {
        return numSection;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Scan";
            case 1:
                return "Camera";
        }
        return null;
    }
}
