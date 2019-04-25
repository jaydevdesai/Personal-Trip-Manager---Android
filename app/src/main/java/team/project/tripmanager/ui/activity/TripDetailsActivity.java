package team.project.tripmanager.ui.activity;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.TripDetailsAdapter;
import team.project.tripmanager.model.CommonResponse;
import team.project.tripmanager.model.ErrorResponse;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.ui.fragment.CreateTripFragment;
import team.project.tripmanager.ui.fragment.EditTripFragment;
import team.project.tripmanager.utils.DateUtils;

public class TripDetailsActivity extends BaseActivity {

    AppCompatTextView tripTitleTv, tripDatesTv, placeNameTv;
    AppCompatImageButton editTripBtn, deleteBtn, goBackBtn;
    RecyclerView cardsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        Trip tripDetails = (Trip) getIntent().getSerializableExtra("TripDetails");
        boolean fromExplore = getIntent().getBooleanExtra("fromExplore", false);

        tripTitleTv = findViewById(R.id.tripTitle);
        tripDatesTv = findViewById(R.id.tripDates);
        placeNameTv = findViewById(R.id.placeName);
        editTripBtn = findViewById(R.id.editTripBtn);
        deleteBtn = findViewById(R.id.deleteTripBtn);
        goBackBtn = findViewById(R.id.goBackBtn);
        cardsListView = findViewById(R.id.cardsList);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        cardsListView.setLayoutManager(layoutManager);
        cardsListView.setItemAnimator(new DefaultItemAnimator());
        cardsListView.setAdapter(new TripDetailsAdapter(tripDetails.getId(), fromExplore));

        tripTitleTv.setText(tripDetails.getTripName());
        try {
            String tripdates = DateUtils.getFormattedDatePeriod(tripDetails.getStartDate(), tripDetails.getEndDate());
            tripDatesTv.setText(tripdates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        placeNameTv.setText("Visiting "+tripDetails.getPlaceName()+".");

        if (fromExplore) {
            editTripBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }

        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to delete this trip?");
                    alertDialogBuilder.setPositiveButton("YES", (arg0, arg1) -> {
                                deleteTripRequestToServer(tripDetails.getId());
                            });

            alertDialogBuilder.setNegativeButton("NO", (dialog, which) ->
                    finish()
            );

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });
        goBackBtn.setOnClickListener(v -> {
            onBackPressed();
        });
        editTripBtn.setOnClickListener(v -> {
            final EditTripFragment editTripFragment = EditTripFragment.newInstance();
            editTripFragment.setCurrentTrip(tripDetails);
            editTripFragment.setCancelable(true);
            editTripFragment.show(getSupportFragmentManager(), "editTrip");
            getSupportFragmentManager().executePendingTransactions();
            editTripFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(editTripFragment.getDelete()){
                        onBackPressed();
                    }
                    getSupportFragmentManager().beginTransaction().remove(editTripFragment).commit();

                }
            });
        });
    }

    private void deleteTripRequestToServer(Integer tripId){

        environment.getAPIService().deleteTrip(tripId).enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(TripDetailsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else if(response.errorBody() != null){
                    try {
                        errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(TripDetailsActivity.this, errorResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.debug("Unsuccessful response");
                    Toast.makeText(TripDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                logger.error(t);
                Toast.makeText(TripDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
