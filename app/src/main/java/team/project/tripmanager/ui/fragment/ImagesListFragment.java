package team.project.tripmanager.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ImagesAdapter;


public class ImagesListFragment extends BaseFragment {

    RecyclerView reservationListView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_images_list, container, false);
        reservationListView = v.findViewById(R.id.imagesListView);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        reservationListView.setLayoutManager(layoutManager);
        reservationListView.setItemAnimator(new DefaultItemAnimator());
        ImagesAdapter imagesAdapter = new ImagesAdapter();
        reservationListView.setAdapter(imagesAdapter);
        return v;
    }

}
