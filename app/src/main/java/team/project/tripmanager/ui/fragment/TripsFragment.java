package team.project.tripmanager.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import team.project.tripmanager.R;
import team.project.tripmanager.adapter.TripAdapter;
import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.CommonResponse;


public class TripsFragment extends BaseFragment {

    AppCompatEditText placePickerEdt;
    int PLACE_PICKER_REQUEST = 1;

    AppCompatImageView tripCoverImage;
    AppCompatImageButton createTripBtn;
    RecyclerView tripsListView;
    private Logger logger = new Logger(getClass());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_trips, container, false);

        tripsListView = v.findViewById(R.id.tripsList);
        tripCoverImage = v.findViewById(R.id.tripCoverImage);
        createTripBtn = v.findViewById(R.id.createTripBtn);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        tripsListView.setLayoutManager(layoutManager);
        tripsListView.setItemAnimator(new DefaultItemAnimator());


        createTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CreateTripFragment createTripFragment = CreateTripFragment.newInstance();
                createTripFragment.setCancelable(false);
                createTripFragment.show(getActivity().getSupportFragmentManager().beginTransaction(), "createTrip");
                getActivity().getSupportFragmentManager().executePendingTransactions();
                createTripFragment.getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(createTripFragment).commit();
//                        new GetTrips().execute();
                        fetchTripsFromServer();
                    }
                });
            }
        });
        //new GetTrips().execute();
        fetchTripsFromServer();
        return v;
    }

    private void fetchTripsFromServer() {
        environment.getAPIService().getTrips().enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                if (response.body() == null) {
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    logger.debug("response.body() is null");
                    return;
                }
                Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                TripAdapter tripAdapter = new TripAdapter(response.body().getTrips());
                tripsListView.setAdapter(tripAdapter);
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                logger.error(t);
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
