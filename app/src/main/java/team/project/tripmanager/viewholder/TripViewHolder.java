package team.project.tripmanager.viewholder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import team.project.tripmanager.R;
import team.project.tripmanager.ui.activity.TripDetailsActivity;
import team.project.tripmanager.model.Trip;

public class TripViewHolder extends RecyclerView.ViewHolder {
    AppCompatImageView tripCoverImage;
    AppCompatTextView tripTitleTv, tripDatesTv;
    CardView tripCard;
    boolean fromExplore;
    public TripViewHolder(@NonNull View itemView) {
        super(itemView);
        tripTitleTv = itemView.findViewById(R.id.tripTitle);
        tripDatesTv = itemView.findViewById(R.id.tripDates);
        tripCoverImage = itemView.findViewById(R.id.tripCoverImage);
        tripCard = itemView.findViewById(R.id.tripCard);
    }

    public void setTripTitle(String tripName) {
        tripTitleTv.setText(tripName);
    }

    public void setStartEndDate(String formattedDate) {
        tripDatesTv.setText(formattedDate);
    }

    public void initializeClickListener(Trip trip) {
        tripCard.setOnClickListener(view -> {
            Intent intent = new Intent(tripCard.getContext(), TripDetailsActivity.class);
            intent.putExtra("TripDetails", trip);
            intent.putExtra("fromExplore", fromExplore);
            tripCard.getContext().startActivity(intent);
        });
    }

    public void setFromExplore(boolean fromExplore) {
        this.fromExplore = fromExplore;
    }
}
