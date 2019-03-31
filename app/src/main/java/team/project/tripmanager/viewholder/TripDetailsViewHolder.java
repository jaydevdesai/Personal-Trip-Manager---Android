package team.project.tripmanager.viewholder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import team.project.tripmanager.R;
import team.project.tripmanager.ui.activity.DocumentsActivity;
import team.project.tripmanager.ui.activity.NoteActivity;
import team.project.tripmanager.ui.activity.ReservationActivity;
import team.project.tripmanager.ui.activity.ShoppingListActivity;

public class TripDetailsViewHolder extends RecyclerView.ViewHolder {

    private AppCompatTextView cardTitleTv;
    private AppCompatImageView cardImage;
    private CardView cardView;

    public TripDetailsViewHolder(@NonNull View itemView) {
        super(itemView);
        cardTitleTv = itemView.findViewById(R.id.cardTitle);
        cardImage = itemView.findViewById(R.id.cardImage);
        cardView = itemView.findViewById(R.id.cardView);
    }

    public void setCardTitle(String text) {
        cardTitleTv.setText(text);
    }

    public void setCardImage(Integer image) {
        cardImage.setImageResource(image);
    }

    public void initiateClick(String page, Integer tripId){
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (page) {
                    case "Reservations" :
                        intent = new Intent(cardView.getContext(), ReservationActivity.class);
                        intent.putExtra("page", page);
                        intent.putExtra("tripId", tripId);
                        cardView.getContext().startActivity(intent);
                        break;
                    case "Documents":
                        intent = new Intent(cardView.getContext(), DocumentsActivity.class);
                        intent.putExtra("page", page);
                        cardView.getContext().startActivity(intent);
                        break;
                    case "Notes" :
                        intent = new Intent(cardView.getContext(), NoteActivity.class);
                        intent.putExtra("tripId", tripId);
                        cardView.getContext().startActivity(intent);
                        break;
                    case "Shopping-List" :
                        intent = new Intent(cardView.getContext(), ShoppingListActivity.class);
                        cardView.getContext().startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
