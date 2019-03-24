package team.project.tripmanager.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;

public class PostQueryFragment extends BaseDialogFragment {

    private Logger logger = new Logger(getClass());
    AppCompatTextView cancelBtn, postBtn;
    AppCompatEditText queryTextEdt;
    Boolean CODE = false;

    public static PostQueryFragment newInstance(){
        return new PostQueryFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_post_query, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        cancelBtn = v.findViewById(R.id.cancelBtn);
        postBtn = v.findViewById(R.id.postBtn);
        queryTextEdt = v.findViewById(R.id.queryTextEdt);
        queryTextEdt.requestFocus();
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPostRequestToServer(Objects.requireNonNull(queryTextEdt.getText()).toString());
                postBtn.setEnabled(false);
                postBtn.setTextColor(getResources().getColor(R.color.buttonDisableColor));
            }
        });
        return  v;
    }

    private void createPostRequestToServer(String queryText){
        environment.getAPIService().postQuery(queryText).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    CODE = true;
                    getDialog().dismiss();
                } else if(response.errorBody() != null){
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(getActivity(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.debug("Unsuccessful response");
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                postBtn.setEnabled(true);
                postBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                postBtn.setEnabled(true);
                postBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        QueryFragment queryFragment = (QueryFragment) getTargetFragment();
        assert queryFragment != null;
        queryFragment.refreshQueries(CODE);
    }
}
