package com.stest.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ldy on 6/12/16.
 */
public class main_selectedAdapter extends FragmentPagerAdapter {
    private FragmentManager manager;
    private List<Fragment> fragmentList=new ArrayList<>();


    public main_selectedAdapter(FragmentManager fm,List<Fragment>list1) {
        super(fm);
        manager=fm;
        fragmentList=list1;

    }
    

    @Override
    public Fragment getItem(int position) {
        Fragment one=fragmentList.get(position);
        return one;
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
