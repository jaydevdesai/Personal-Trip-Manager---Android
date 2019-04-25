package team.project.tripmanager.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.text.ParseException;

import team.project.tripmanager.R;
import team.project.tripmanager.utils.DateUtils;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    private AppCompatTextView itemName, itemPrice, itemDate, cashCard;
    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        itemDate = itemView.findViewById(R.id.itemDate);
        cashCard = itemView.findViewById(R.id.cashCard);
    }

    public void setItemName(String name){
        itemName.setText(name);
    }

    public void setItemPrice(Double price){
        String pr = "Amount: "+ Double.valueOf(price);
        itemPrice.setText(pr);
    }

    public void setItemDate(String date) throws ParseException {
        date = "Date: "+DateUtils.getFormattedDate(date,"dd MMM");
        itemDate.setText(date);
    }

    public void setCashCard(Integer cash){
        if(cash == 0){
            cashCard.setText("CARD");
            cashCard.setBackgroundColor(cashCard.getContext().getResources().getColor(R.color.colorAccent));
        }
    }

}
