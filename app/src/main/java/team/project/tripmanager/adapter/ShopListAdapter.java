package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import team.project.tripmanager.R;
import team.project.tripmanager.viewholder.ShopListViewHolder;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListViewHolder> {
    @NonNull
    @Override
    public ShopListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shop_list_cards_list_row, viewGroup, false);
        return new ShopListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListViewHolder shopListViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
