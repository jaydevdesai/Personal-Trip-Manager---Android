package team.project.tripmanager.viewholder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.core.TMApplication;
import team.project.tripmanager.core.TMEnvironment;
import team.project.tripmanager.listener.OnItemDeletedListener;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;

public class QueryRepliesViewHolder extends RecyclerView.ViewHolder {

    private TextView userNameTv, replyTextTV, dateTv;
    private ConstraintLayout queryReplyLayout;
    private OnItemDeletedListener listener;
    public QueryRepliesViewHolder(@NonNull View itemView) {
        super(itemView);
        queryReplyLayout = itemView.findViewById(R.id.queryReplyLayout);
        userNameTv = itemView.findViewById(R.id.userName);
        replyTextTV = itemView.findViewById(R.id.replyText);
        dateTv = itemView.findViewById(R.id.replyDate);
    }

    public void setName(String name) {
        userNameTv.setText(name);
    }

    public void setReplyText(String replyText) {
        replyTextTV.setText(replyText);
    }

    public  void setDate(String date){
        dateTv.setText(date);
    }

    public void initializeLongClick(Integer replyId, boolean notYou, OnItemDeletedListener listener){
        if(notYou) return;
        this.listener = listener;
        queryReplyLayout.setOnLongClickListener(v -> {
            deleteItem(replyId);
            return true;
        });
    }

    private void deleteItem(Integer replyId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(queryReplyLayout.getContext());
        alertDialogBuilder.setMessage("Are you sure you want to delete this reply?");
        alertDialogBuilder.setPositiveButton("YES", (arg0, arg1) -> {
            deleteItemRequestToServer(replyId);
        });
        alertDialogBuilder.setNegativeButton("NO", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteItemRequestToServer(Integer replyId){
        TMEnvironment environment = ((TMApplication) queryReplyLayout.getContext().getApplicationContext()).getEnvironment();
        environment.getAPIService().deleteQueryReply(replyId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(queryReplyLayout.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onDeleted();
                } else if(response.errorBody() != null){
                    try {
                        Gson gson  = new GsonBuilder().setLenient().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(queryReplyLayout.getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //logger.debug("Unsuccessful response");
                    Toast.makeText(queryReplyLayout.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                //logger.error(t);
                Toast.makeText(queryReplyLayout.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
