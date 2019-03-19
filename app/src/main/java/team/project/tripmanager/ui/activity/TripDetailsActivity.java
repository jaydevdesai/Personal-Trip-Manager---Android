package team.project.tripmanager.ui.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.ui.fragment.CreateTripFragment;
import team.project.tripmanager.ui.fragment.EditTripFragment;
import team.project.tripmanager.utils.DateUtils;

public class TripDetailsActivity extends AppCompatActivity {

    AppCompatTextView tripTitleTv, tripDatesTv, placeNameTv;
    AppCompatImageButton editTripBtn;
    RecyclerView cardsListView;
    ArrayList<String> cardsList;
    ArrayList<Integer> cardsListImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        Trip tripDetails = (Trip) getIntent().getSerializableExtra("TripDetails");

        tripTitleTv = findViewById(R.id.tripTitle);
        tripDatesTv = findViewById(R.id.tripDates);
        placeNameTv = findViewById(R.id.placeName);
        editTripBtn = findViewById(R.id.editTripBtn);
        cardsListView = findViewById(R.id.cardsList);

        cardsList = new ArrayList<String>(Arrays.asList("Group", "Reservations", "Documents","Expenses", "Notes", "Shopping-List"));
        cardsListImage = new ArrayList<Integer>(Arrays.asList(R.drawable.ic_group_black_24dp,R.drawable.ic_train_black_24dp,R.drawable.ic_folder_black_24dp,R.drawable.ic_attach_money_black_24dp,R.drawable.ic_note_add_black_24dp,R.drawable.ic_add_shopping_cart_black_24dp));
        CardsListHolder cardsListHolder = new CardsListHolder();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        cardsListView.setLayoutManager(layoutManager);
        cardsListView.setItemAnimator(new DefaultItemAnimator());
        cardsListView.setAdapter(cardsListHolder);

        tripTitleTv.setText(tripDetails.getTripName());
        try {
            String tripdates = DateUtils.getFormattedDatePeriod(tripDetails.getStartDate(), tripDetails.getEndDate());
            tripDatesTv.setText(tripdates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        placeNameTv.setText("Visiting "+tripDetails.getPlaceName()+".");

        editTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditTripFragment editTripFragment = EditTripFragment.newInstance();
                editTripFragment.setCurrentTrip(tripDetails);
                editTripFragment.setCancelable(false);
                editTripFragment.show(getSupportFragmentManager(), "editTrip");
                getSupportFragmentManager().executePendingTransactions();
                editTripFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getSupportFragmentManager().beginTransaction().remove(editTripFragment).commit();

                    }
                });
            }
        });
    }

    public class CardsListHolder extends RecyclerView.Adapter<TripDetailsActivity.CardsListHolder.MyViewHolder> {

        class MyViewHolder extends RecyclerView.ViewHolder{

            AppCompatTextView cardTitleTv;
            AppCompatImageView cardImage;
            CardView cardView;

            MyViewHolder(@NonNull View itemView) {
                super(itemView);
                cardTitleTv = itemView.findViewById(R.id.cardTitle);
                cardImage = itemView.findViewById(R.id.cardImage);
                cardView = itemView.findViewById(R.id.cardView);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.trip_details_cards_list_row, viewGroup, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
            holder.cardTitleTv.setText(cardsList.get(i));
            holder.cardImage.setImageResource(cardsListImage.get(i));
        }


        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return cardsList.size();
        }
    }
}
