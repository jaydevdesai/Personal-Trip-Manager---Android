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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.ExploreAdapter;
import team.project.tripmanager.adapter.TripAdapter;
import team.project.tripmanager.callback.CustomCallback;
import team.project.tripmanager.listener.EndlessRecyclerOnScrollListener;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Trip;

public class ExploreFragment extends BaseFragment {

    RecyclerView postsListView;
//    ExploreAdapter exploreAdapter;
    private List<Trip> exploreTrips;
    private TripAdapter tripAdapter;
    private Logger logger = new Logger(getClass());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_explore, container,false);
        postsListView = v.findViewById(R.id.postsList);
//        exploreAdapter = new ExploreAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);
        postsListView.setLayoutManager(layoutManager);
        postsListView.setItemAnimator(new DefaultItemAnimator());
//        postsListView.setAdapter(exploreAdapter);

        exploreTrips = new ArrayList<>();

        tripAdapter = new TripAdapter(exploreTrips, true);
        postsListView.setAdapter(tripAdapter);
        postsListView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                fetchExploreFromServer(exploreTrips.get(exploreTrips.size() - 1).getCreatedAt());
            }
        });
        fetchExploreFromServer(null);
        return v;
    }

    private void fetchExploreFromServer(String lastCreatedAt) {
        environment.getAPIService().getExploreTrips(lastCreatedAt).enqueue(new CustomCallback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                super.onResponse(call, response);
                if (response.body() != null && response.body().getTrips() != null) {
                    exploreTrips.addAll(response.body().getTrips());
                    tripAdapter.notifyDataSetChanged();
                } else {
                    logger.warn("response.body() != null && response.body().getTrips() != null False");
                }
            }
        });
    }
}
