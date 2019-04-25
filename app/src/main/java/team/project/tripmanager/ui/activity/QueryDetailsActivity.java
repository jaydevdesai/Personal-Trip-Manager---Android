package team.project.tripmanager.ui.activity;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.QueryRepliesAdapter;
import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.listener.OnItemDeletedListener;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;
import team.project.tripmanager.model.Query;
import team.project.tripmanager.module.prefs.MainPrefs;
import team.project.tripmanager.ui.fragment.BaseFragment;
import team.project.tripmanager.utils.DateUtils;

public class QueryDetailsActivity extends BaseActivity implements OnItemDeletedListener {

    AppCompatTextView userNameTV, queryTextTV, queryDate;
    AppCompatImageView userPicIV;
    AppCompatImageButton postReplyBtn, deleteBtn;
    AppCompatEditText replyEdt;
    RecyclerView repliesListView;
    private Query query;
    private static MainPrefs mainPrefs;
    TMEnvironment tmEnvironment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_details);

        tmEnvironment = ((TMApplication)this.getApplicationContext()).getEnvironment();
        mainPrefs = environment.getPrefs();
        query = (Query) getIntent().getSerializableExtra("QueryDetails");
        userNameTV = findViewById(R.id.userName);
        queryDate =findViewById(R.id.queryDate);
        queryTextTV = findViewById(R.id.queryText);
        userPicIV = findViewById(R.id.userPic);
        replyEdt = findViewById(R.id.replyEdt);
        deleteBtn = findViewById(R.id.deleteQueryBtn);
        postReplyBtn = findViewById(R.id.postReplyBtn);
        repliesListView = findViewById(R.id.repliesListView);

        String name = query.getName();
        if(Objects.requireNonNull(mainPrefs.getEmail()).equals(query.getEmail())) {
            name = "You";
        } else {
            deleteBtn.setVisibility(View.GONE);
        }
        userNameTV.setText(name);
        queryTextTV.setText(query.getQueryText());
        try {
            queryDate.setText(DateUtils.getFormattedDate(query.getCreationTime(),"dd MMM yy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete this query?");
            alertDialogBuilder.setPositiveButton("YES", (arg0, arg1) -> {
                deleteQueryRequestToServer(query.getQueryId());
            });

            alertDialogBuilder.setNegativeButton("NO", null
            );

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        repliesListView.setLayoutManager(layoutManager);
        repliesListView.setItemAnimator(new DefaultItemAnimator());


        postReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postQueryReplyToServer();
            }
        });
        fetchQueryRepliesFromServer();
    }


    private void fetchQueryRepliesFromServer() {
        tmEnvironment.getAPIService().getQueriesReplies(query.getQueryId()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    QueryRepliesAdapter queryRepliesAdapter = new QueryRepliesAdapter(response.body().getQueryReplies(),mainPrefs, QueryDetailsActivity.this);
                    repliesListView.setAdapter(queryRepliesAdapter);
                } else {
                    try {
                        Gson gson = new Gson().newBuilder().setLenient().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postQueryReplyToServer(){
        tmEnvironment.getAPIService().postQueryReply(query.getQueryId(),replyEdt.getText().toString()).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    replyEdt.setText(null);
                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    fetchQueryRepliesFromServer();
                } else {
                    try {
                        Gson gson = new Gson().newBuilder().setLenient().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getApplicationContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteQueryRequestToServer(Integer queryId){

        environment.getAPIService().deleteQuery(queryId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QueryDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else if(response.errorBody() != null){
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(QueryDetailsActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.debug("Unsuccessful response");
                    Toast.makeText(QueryDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                logger.error(t);
                Toast.makeText(QueryDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleted() {
        fetchQueryRepliesFromServer();
    }
}
