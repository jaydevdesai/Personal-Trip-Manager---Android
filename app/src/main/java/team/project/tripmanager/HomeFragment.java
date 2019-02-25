package team.project.tripmanager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {


    private TabAdapter tabAdapter;
    public ViewPager viewPager;
    private TabLayout tabLayout;


    int imageResId[] = { R.drawable.ic_explore_black_24dp, R.drawable.ic_business_center_black_24dp, R.drawable.ic_question_answer_black_24dp};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);

        tabAdapter = new TabAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        tabAdapter.addFragment(new ExploreFragment(), "Explore");
        tabAdapter.addFragment(new TripsFragment(), "Trips");
        tabAdapter.addFragment(new QueryFragment(), "Queries");


        viewPager.setAdapter(tabAdapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);


        customTabView(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                customTabView(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return view;
    }

    void customTabView(int position){
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(tabAdapter.getTabView(i));
        }

        TabLayout.Tab tab = tabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(tabAdapter.getSelectedTabView(position));
    }


    private class TabAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> fragmenNameList = new ArrayList<>();

        TabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        void addFragment(Fragment fragment, String name){fragmentList.add(fragment); fragmenNameList.add(name);}

        View getTabView(int position) {
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_layout, null);
            AppCompatTextView tv = v.findViewById(R.id.tabTitle);
            tv.setText(fragmenNameList.get(position));
            AppCompatImageView img =  v.findViewById(R.id.tabIcon);
            img.setImageResource(imageResId[position]);
            return v;
        }

        View getSelectedTabView(int position ){
            View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab_layout, null);
            AppCompatTextView tv = v.findViewById(R.id.tabTitle);
            tv.setText(fragmenNameList.get(position));
            tv.setTextColor(getResources().getColor(R.color.colorAccent));
            AppCompatImageView img =  v.findViewById(R.id.tabIcon);
            img.setImageResource(imageResId[position]);
            img.setImageTintList(getResources().getColorStateList(R.color.colorAccent));

            return v;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmenNameList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }


    }


}
