package team.project.tripmanager.viewholder;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import team.project.tripmanager.R;

public class ShopListViewHolder extends RecyclerView.ViewHolder {
    private TextView itemNameTv, boughtTv;

    public ShopListViewHolder(@NonNull View itemView) {
        super(itemView);
        itemNameTv = itemView.findViewById(R.id.shopping_item_name);
        boughtTv = itemView.findViewById(R.id.shopping_item_bought);
    }

    public void setItemName(String itemName) {
        itemNameTv.setText(itemName);
    }

    public void setBought(Boolean bought) {
        String boughtText = bought ? "Bought" : "Not bought";
        String boughtBackColor = bought ? "#8800FF00" : "#88FF0000";
        boughtTv.setText(boughtText);
        boughtTv.setBackgroundColor(Color.parseColor(boughtBackColor));
    }
}
