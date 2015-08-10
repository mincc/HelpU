package com.example.chungmin.helpu;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import fragments.CreateQuotationFragment;

public class Test extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment frag = new CreateQuotationFragment().newInstance(14);
            ft.add(R.id.llRating, frag);
            ft.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}