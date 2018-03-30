package com.example.aditya.notebuddy;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by aditya on 20/3/18.
 */

public class TabsPagerAdapter extends FragmentPagerAdapter {


    public TabsPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                // Top Rated fragment activity
                return new BranchFragment();
            case 1:
                // Games fragment activity
                return new PublicFragment();

        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
