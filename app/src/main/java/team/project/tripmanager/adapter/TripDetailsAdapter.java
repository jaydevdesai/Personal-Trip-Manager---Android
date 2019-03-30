package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import team.project.tripmanager.R;
import team.project.tripmanager.ui.activity.TripDetailsActivity;
import team.project.tripmanager.viewholder.TripDetailsViewHolder;

public class TripDetailsAdapter extends RecyclerView.Adapter<TripDetailsViewHolder> {

    private final Integer tripId;
    private ArrayList<String> cardsList = new ArrayList<String>(Arrays.asList("Reservations", "Documents","Expenses", "Notes", "Shopping-List"));
    private ArrayList<Integer> cardsListImage = new ArrayList<Integer>(Arrays.asList(R.drawable.ic_train_black_24dp,R.drawable.ic_folder_black_24dp,R.drawable.ic_attach_money_black_24dp,R.drawable.ic_note_add_black_24dp,R.drawable.ic_add_shopping_cart_black_24dp));

    public TripDetailsAdapter(Integer tripId) {
        this.tripId = tripId;
    }

    @NonNull
    @Override
    public TripDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_details_cards_list_row, viewGroup, false);
        return new TripDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripDetailsViewHolder tripDetailsViewHolder, int i) {
        tripDetailsViewHolder.setCardTitle(cardsList.get(i));
        tripDetailsViewHolder.setCardImage(cardsListImage.get(i));
        tripDetailsViewHolder.initiateClick(cardsList.get(i), tripId);
    }

    @Override
    public int getItemCount() {
        return cardsList.size();
    }
}
