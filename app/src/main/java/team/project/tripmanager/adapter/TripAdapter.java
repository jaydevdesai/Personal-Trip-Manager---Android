package team.project.tripmanager.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import team.project.tripmanager.R;
import team.project.tripmanager.model.Trip;
import team.project.tripmanager.utils.DateUtils;
import team.project.tripmanager.viewholder.TripViewHolder;

public class TripAdapter extends RecyclerView.Adapter<TripViewHolder> {
    private List<Trip> trips;

    public TripAdapter(List<Trip> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trips_list_row, viewGroup, false);
        return new TripViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder tripViewHolder, int position) {
        Trip trip = trips.get(position);
        tripViewHolder.setTripTitle(trip.getTripName());
        try {
            String formattedDate = DateUtils.getFormattedDate(trip.getStartDate(), trip.getEndDate());
            tripViewHolder.setStartEndDate(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tripViewHolder.initializeClickListener(trip);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    private String formattedDate(String startDate, String endDate) throws ParseException {
        DateFormat desiredDateFormat = new SimpleDateFormat("dd MMM");
        DateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("%s - %s", desiredDateFormat.format(serverDateFormat.parse(startDate)), desiredDateFormat.format(serverDateFormat.parse(startDate)));
    }
}
