package com.ezzat.doctoruim.Control;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ezzat.doctoruim.View.Admin.DoctorsFragment;
import com.ezzat.doctoruim.View.Admin.PatientsFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DoctorsFragment tab1 = new DoctorsFragment();
                return tab1;
            case 1:
                PatientsFragment tab2 = new PatientsFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
