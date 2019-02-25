package team.project.tripmanager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TripDetails implements Serializable {

    private String tripId, tripName, placeName, startDate, endDate;

    void setTripId(String tripId){ this.tripId = tripId;}
    void setTripName(String tripName){this.tripName = tripName;}
    void setPlaceName(String placeName){this.placeName = placeName;}
    void setStartDate(String startDate){this.startDate = startDate;}
    void setEndDate(String endDate) {this.endDate = endDate;}

    public String getTripId() {
        return tripId;
    }

    String getTripName() {
        return tripName;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getFormattedStartDate() throws ParseException {
        return new SimpleDateFormat("dd MMM").format(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
    }

    public String getFormattedEndDate() throws ParseException {
        return new SimpleDateFormat("dd MMM, yy").format(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
    }
}
