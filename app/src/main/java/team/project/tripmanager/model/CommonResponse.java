package team.project.tripmanager.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("trips")
    @Expose
    private List<Trip> trips = null;

    @SerializedName("queries")
    @Expose
    private List<Query> queries = null;

    public String getMessage() {
        return message;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public List<Query> getQueries() {
        return queries;
    }
}
