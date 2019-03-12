package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ViewPagerAdapter;
import team.project.tripmanager.logger.Logger;

public class HomeFragment extends BaseFragment implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    public ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private Logger logger = new Logger(getClass());
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPagerAdapter.addFragment(R.id.menu_bottom_explore, new ExploreFragment());
        viewPagerAdapter.addFragment(R.id.menu_bottom_trips, new TripsFragment());
        viewPagerAdapter.addFragment(R.id.menu_bottom_queries, new QueryFragment());
        viewPager.addOnPageChangeListener(this);
        viewPager.setAdapter(viewPagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager.setCurrentItem(viewPagerAdapter.getPosition(R.id.menu_bottom_trips));

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        viewPager.setCurrentItem(viewPagerAdapter.getPosition(menuItem.getItemId()));
        return true;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {
        bottomNavigationView.setSelectedItemId(viewPagerAdapter.getItemResId(i));
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
