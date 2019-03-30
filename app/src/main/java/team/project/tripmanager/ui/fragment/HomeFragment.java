package team.project.tripmanager.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Objects;

import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ViewPagerAdapter;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.ui.activity.ProfileActivity;

public class HomeFragment extends BaseFragment implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    public ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private Logger logger = new Logger(getClass());
    AppCompatTextView topTitleTV;
    AppCompatImageView topTitleIconIV;
    AppCompatImageButton createTripBtn;
    private ImageButton profieBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        topTitleTV = view.findViewById(R.id.topTitle);
        //topTitleIconIV = view.findViewById(R.id.topTitleIcon);
        createTripBtn = view.findViewById(R.id.createTripBtn);

        viewPager = view.findViewById(R.id.viewPager);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        profieBtn = view.findViewById(R.id.fh_ib_profile);
        profieBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });


        viewPagerAdapter = new ViewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager());
        viewPagerAdapter.addFragment(R.id.menu_bottom_explore, new ExploreFragment());
        viewPagerAdapter.addFragment(R.id.menu_bottom_trips, new TripsFragment());
        viewPagerAdapter.addFragment(R.id.menu_bottom_queries, new QueryFragment());
        viewPager.addOnPageChangeListener(this);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager.setCurrentItem(viewPagerAdapter.getPosition(R.id.menu_bottom_trips));


        createTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CreateTripFragment createTripFragment = CreateTripFragment.newInstance();
                createTripFragment.setCancelable(false);
                createTripFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "createTrip");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                createTripFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(createTripFragment).commit();
                        Objects.requireNonNull(viewPager.getAdapter()).notifyDataSetChanged();

                    }
                });
            }
        });

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        viewPager.setCurrentItem(viewPagerAdapter.getPosition(menuItem.getItemId()));
        topTitleTV.setText(menuItem.getTitle());
        //topTitleIconIV.setImageDrawable(menuItem.getIcon());
        if(!menuItem.getTitle().equals("Trips")){
            createTripBtn.setVisibility(View.GONE);
        } else {
            createTripBtn.setVisibility(View.VISIBLE);
        }
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
