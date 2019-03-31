package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Shopping;
import team.project.tripmanager.viewholder.ShopListViewHolder;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListViewHolder> {
    private List<Shopping> shoppingList;

    public ShopListAdapter(List<Shopping> shoppingList) {
        this.shoppingList = shoppingList;
    }

    @NonNull
    @Override
    public ShopListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shop_list_cards_list_row, viewGroup, false);
        return new ShopListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopListViewHolder shopListViewHolder, int i) {
        Shopping shopping = shoppingList.get(i);
        shopListViewHolder.setItemName(shopping.getItemName());
        shopListViewHolder.setBought(shopping.getBought());
    }

    @Override
    public int getItemCount() {
        return shoppingList.size();
    }
}
