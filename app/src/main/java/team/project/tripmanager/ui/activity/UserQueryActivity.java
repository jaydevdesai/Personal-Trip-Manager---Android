package team.project.tripmanager.ui.activity;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.JsonSyntaxException;

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
import team.project.tripmanager.ui.fragment.PostQueryFragment;
import team.project.tripmanager.ui.fragment.QueryFragment;

public class UserQueryActivity extends BaseActivity {

    private RecyclerView queryListView;
    private QueryAdapter queryAdapter;
    private Logger logger = new Logger(getClass());
    AppCompatTextView errorTxt;
    FrameLayout postQueryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_query);
        errorTxt = findViewById(R.id.errorTxt);
        postQueryContainer = findViewById(R.id.postQueryContainer);
        AppCompatImageButton postQueryBtn = findViewById(R.id.postQueryBtn);
        postQueryBtn.setVisibility(View.GONE);
        queryListView = findViewById(R.id.queryList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        queryListView.setLayoutManager(layoutManager);
        queryListView.setItemAnimator(new DefaultItemAnimator());
        postQueryContainer.setVisibility(View.GONE);

        fetchQueriesFromServer();
    }

    private void fetchQueriesFromServer() {
        environment.getAPIService().getUserQueries().enqueue(new Callback<CommonResponse>() {
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
                        logger.debug(response.errorBody().string());
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        //Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        errorTxt.setText(errorResponse.getMessage());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch(JsonSyntaxException e) {
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
