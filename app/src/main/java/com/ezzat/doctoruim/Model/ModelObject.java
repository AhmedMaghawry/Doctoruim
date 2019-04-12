package com.ezzat.doctoruim.Model;

import com.ezzat.doctoruim.R;

public enum ModelObject {

    First(R.layout.view_first),
    Second(R.layout.view_second),
    Third(R.layout.view_third);

    private int mLayoutResId;

    ModelObject(int layoutResId) {
        mLayoutResId = layoutResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}