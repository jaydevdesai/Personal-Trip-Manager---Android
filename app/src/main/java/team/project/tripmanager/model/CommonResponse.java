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

    @SerializedName("replies")
    @Expose
    private List<QueryReply> query_replies = null;

    @SerializedName("documents")
    @Expose
    private List<Document> documents = null;

    @SerializedName("reservations")
    @Expose
    private List<Reservation> reservations = null;

    @SerializedName("note")
    @Expose
    private Note note;

    @SerializedName("shopping_list")
    @Expose
    private List<Shopping> shoppingList = null;

    public String getMessage() {
        return message;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public List<QueryReply> getQueryReplies() { return query_replies; }

    public List<Document> getDocuments() {
        return documents;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public Note getNote() {
        return note;
    }

    public List<Shopping> getShoppingList() {
        return shoppingList;
    }
}
