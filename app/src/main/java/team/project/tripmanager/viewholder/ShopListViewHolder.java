package team.project.tripmanager.viewholder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
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
import team.project.tripmanager.ui.activity.ShoppingListActivity;

public class ShopListViewHolder extends RecyclerView.ViewHolder {
    private TextView itemNameTv, boughtTv;
    private CardView shopLayout;
    private OnItemDeletedListener listener;
    public ShopListViewHolder(@NonNull View itemView) {
        super(itemView);
        itemNameTv = itemView.findViewById(R.id.shopping_item_name);
        boughtTv = itemView.findViewById(R.id.shopping_item_bought);
        shopLayout = itemView.findViewById(R.id.shopLayout);
    }

    public void setItemName(String itemName) {
        itemNameTv.setText(itemName);
    }

    public void setBought(Boolean bought) {
        String boughtText = bought ? "Bought" : "Not bought";
        int boughtBackColor = bought ? shopLayout.getResources().getColor(R.color.bought) : shopLayout.getResources().getColor(R.color.notBought);
        boughtTv.setText(boughtText);
        boughtTv.setBackgroundColor(boughtBackColor);
    }



    public void initializeLongClick(Integer itemId, boolean fromExplore,OnItemDeletedListener listener){
        if(fromExplore) return;
        this.listener = listener;
        shopLayout.setOnLongClickListener(v -> {
            deleteItem(itemId);
            return true;
        });
    }

    private void deleteItem(Integer itemId){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(shopLayout.getContext());
        alertDialogBuilder.setMessage("Are you sure you want to delete this item?");
        alertDialogBuilder.setPositiveButton("YES", (arg0, arg1) -> {
            deleteItemRequestToServer(itemId);
        });
        alertDialogBuilder.setNegativeButton("NO", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteItemRequestToServer(Integer itemId){
        TMEnvironment environment = ((TMApplication) shopLayout.getContext().getApplicationContext()).getEnvironment();
        environment.getAPIService().deleteItem(itemId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(shopLayout.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    listener.onDeleted();
                } else if(response.errorBody() != null){
                    try {
                        Gson gson  = new GsonBuilder().setLenient().create();
                        ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(shopLayout.getContext(), errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //logger.debug("Unsuccessful response");
                    Toast.makeText(shopLayout.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                //logger.error(t);
                Toast.makeText(shopLayout.getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
