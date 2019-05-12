package com.ezzat.doctoruim.Control;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ezzat.doctoruim.View.Doctor.ClinicsFragment;
import com.ezzat.doctoruim.View.Doctor.ReservationsFragment;

public class PagerDoctorAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public PagerDoctorAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ReservationsFragment tab1 = new ReservationsFragment();
                return tab1;
            case 1:
                ClinicsFragment tab2 = new ClinicsFragment();
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
