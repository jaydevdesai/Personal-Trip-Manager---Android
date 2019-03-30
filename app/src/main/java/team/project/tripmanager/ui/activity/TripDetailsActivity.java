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
import android.view.WindowManager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import team.project.tripmanager.R;
import team.project.tripmanager.adapter.TripDetailsAdapter;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.ui.fragment.CreateTripFragment;
import team.project.tripmanager.ui.fragment.EditTripFragment;
import team.project.tripmanager.utils.DateUtils;

public class TripDetailsActivity extends BaseActivity {

    AppCompatTextView tripTitleTv, tripDatesTv, placeNameTv;
    AppCompatImageButton editTripBtn;
    RecyclerView cardsListView;

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

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        cardsListView.setLayoutManager(layoutManager);
        cardsListView.setItemAnimator(new DefaultItemAnimator());
        cardsListView.setAdapter(new TripDetailsAdapter(tripDetails.getId()));

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
                editTripFragment.setCancelable(true);
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

}
