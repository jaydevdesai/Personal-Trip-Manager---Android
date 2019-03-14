package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ExploreAdapter;

public class ExploreFragment extends BaseFragment {

    RecyclerView postsListView;
    ExploreAdapter exploreAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container,false);
        postsListView = v.findViewById(R.id.postsList);
        exploreAdapter = new ExploreAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        postsListView.setLayoutManager(layoutManager);
        postsListView.setItemAnimator(new DefaultItemAnimator());
        postsListView.setAdapter(exploreAdapter);
        return v;
    }


}
