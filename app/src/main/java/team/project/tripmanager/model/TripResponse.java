package team.project.tripmanager.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TripResponse {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("trips")
    @Expose
    private List<Trip> trips = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }
}
