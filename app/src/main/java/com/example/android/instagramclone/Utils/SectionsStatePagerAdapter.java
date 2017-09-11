package com.example.android.instagramclone.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.fragment;

/**
 * Created by Viji on 9/10/2017.
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final HashMap<Fragment,Integer> mFragments = new HashMap<>();
    private final HashMap<String,Integer> mFragmentNumbers = new HashMap<>();
    private final HashMap<Integer,String> mFragmentNames = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment,String fragmentName){

        mFragmentList.add(fragment);
        mFragments.put(fragment,mFragmentList.size()-1);
        mFragmentNumbers.put(fragmentName,mFragmentList.size()-1);
        mFragmentNames.put(mFragmentList.size()-1,fragmentName);
    }

    /**
     * get Fragment numbers from fragment name
     * @param FragmentName
     * @return
     */
    public Integer getFragmentNumber(String FragmentName){
        if (mFragmentNumbers.containsKey(FragmentName)){
            return mFragmentNumbers.get(FragmentName);
        }else{
            return null;
        }
    }

    /**
     * get Fragment numbers from fragment
     * @param fragment
     * @return
     */
    public Integer getFragmentNumber(Fragment fragment) {
        if (mFragmentNumbers.containsKey(fragment)) {
            return mFragmentNumbers.get(fragment);
        } else {
            return null;
        }
    }
    /**
     * get Fragment numbers from fragment name
     * @param FragmentNumber
     * @return
     */
    public String getFragmentName(Integer FragmentNumber) {
        if (mFragmentNames.containsKey(FragmentNumber)) {
            return mFragmentNames.get(FragmentNumber);
        } else {
            return null;
        }
    }



}
