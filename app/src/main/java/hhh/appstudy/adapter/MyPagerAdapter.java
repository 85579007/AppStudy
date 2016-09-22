package hhh.appstudy.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import hhh.appstudy.fragment.SingleFragment;

/**
 * Created by Administrator on 2016/6/28 0028.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private List<String> list;

    public MyPagerAdapter(FragmentManager fm, List list) {
        super(fm);
        this.list=list;
    }

    @Override
    public Fragment getItem(int position) {
        SingleFragment fragment=new SingleFragment();
        Bundle bundle=new Bundle();
        bundle.putString("tk",list.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
