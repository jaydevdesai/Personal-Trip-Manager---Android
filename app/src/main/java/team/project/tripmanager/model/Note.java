package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Note {
    @SerializedName("note_id")
    @Expose
    private Integer noteId;
    @SerializedName("trip_id")
    @Expose
    private Integer tripId;
    @SerializedName("note_text")
    @Expose
    private String noteText;
    @SerializedName("update_time")
    @Expose
    private String updateTime;

    public Integer getNoteId() {
        return noteId;
    }

    public Integer getTripId() {
        return tripId;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getUpdateTime() {
        return updateTime;
    }
}
