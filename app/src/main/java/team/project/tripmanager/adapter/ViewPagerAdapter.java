package team.project.tripmanager.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public List<Fragment> fragments;
    public List<Integer> fragmentIds;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        fragmentIds = new ArrayList<>();
    }

    public void addFragment(@IdRes Integer fragmentId,  Fragment fragment) {
        fragmentIds.add(fragmentId);
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int getPosition(int itemId) {
        return fragmentIds.indexOf(itemId);
    }

    public int getItemResId(int i) {
        return fragmentIds.get(i);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
