package team.project.tripmanager.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.QueryAdapter;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;
import team.project.tripmanager.model.Query;

public class QueryFragment extends BaseFragment {

    private RecyclerView queryListView;
    private QueryAdapter queryAdapter;
    private Logger logger = new Logger(getClass());
    AppCompatTextView errorTxt;
    FrameLayout postQueryContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_query, container, false);

        AppCompatImageButton postQueryBtn = v.findViewById(R.id.postQueryBtn);
        errorTxt = v.findViewById(R.id.errorTxt);
        postQueryContainer = v.findViewById(R.id.postQueryContainer);
        queryListView = v.findViewById(R.id.queryList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        queryListView.setLayoutManager(layoutManager);
        queryListView.setItemAnimator(new DefaultItemAnimator());

        postQueryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQueryContainer.setVisibility(View.VISIBLE);
                PostQueryFragment postQueryFragment = new PostQueryFragment();
                postQueryFragment.setTargetFragment(QueryFragment.this,1);
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .add(R.id.postQueryContainer, postQueryFragment)
                        .addToBackStack("postQuery")
                        .commit();
            }
        });

        fetchQueriesFromServer();

        return v;
    }

    public void refreshQueries(int code){
        if(code == 1) fetchQueriesFromServer();
        postQueryContainer.setVisibility(View.GONE);
        //Toast.makeText(getActivity(), "Refreshed", Toast.LENGTH_SHORT).show();
    }

    private void fetchQueriesFromServer() {
        environment.getAPIService().getQueries().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                logger.debug("response");
                if (!response.isSuccessful() && response.body() != null && response.body().getQueries() != null) {
                    showSomethingWentWrong();
                    logger.debug("response not successful");
                } else if(response.body() != null){
                    List<Query> queries = response.body().getQueries();
                    errorTxt.setVisibility(View.GONE);
                    queryListView.setVisibility(View.VISIBLE);
                    queryAdapter = new QueryAdapter(queries,environment.getPrefs());
                    queryListView.setAdapter(queryAdapter);
                } else if(response.errorBody() != null){
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        //Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        errorTxt.setText(errorResponse.getMessage());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    logger.debug("response.body() is null");
                } else {
                    errorTxt.setText("Something Went Wrong.");
                    //Toast.makeText(getActivity(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    logger.debug("respnse.errorBody() is null");
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
