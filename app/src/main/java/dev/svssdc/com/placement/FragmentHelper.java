package dev.svssdc.com.placement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FragmentHelper extends FragmentPagerAdapter{
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    int noOfTabs;
    public FragmentHelper(FragmentManager supportFragmentManager,int noOfTabs) {
        super(supportFragmentManager);
        this.noOfTabs = noOfTabs;
    }
    public FragmentHelper(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
//        switch(position){
//            case 0:
//                BlankFragment tab_1 = new BlankFragment();
//                return tab_1;
//            case 1:
//                BlankFragment tab_2 = new BlankFragment();
//                return tab_2;
//            case 2:
//                BlankFragment tab_3 = new BlankFragment();
//                return tab_3;
//            case 3:
//                BlankFragment tab_4 = new BlankFragment();
//                return tab_4;
//            default:
//                return null;
//        }
        return mFragmentList.get(position);
    }
    public void addFragment(Fragment fragment,String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}