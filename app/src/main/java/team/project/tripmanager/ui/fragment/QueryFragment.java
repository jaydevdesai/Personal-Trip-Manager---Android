package team.project.tripmanager.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.QueryAdapter;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.Query;

public class QueryFragment extends BaseFragment {

    private RecyclerView queryListView;
    private QueryAdapter queryAdapter;
    private Logger logger = new Logger(getClass());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query, container, false);

        queryListView = v.findViewById(R.id.queryList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);


        queryListView.setLayoutManager(layoutManager);
        queryListView.setItemAnimator(new DefaultItemAnimator());

        fetchQueriesFromServer();

        return v;
    }

    private void fetchQueriesFromServer() {
        environment.getAPIService().getQueries().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                logger.debug("response");
                if (!response.isSuccessful() && response.body() != null && response.body().getQueries() != null) {
                    showSomethingWentWrong();
                    logger.debug("response not successfull");
                } else {
                    List<Query> queries = response.body().getQueries();
                    queryAdapter = new QueryAdapter(queries);
                    queryListView.setAdapter(queryAdapter);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                logger.error(t);
                showSomethingWentWrong();
            }
        });
    }
}
