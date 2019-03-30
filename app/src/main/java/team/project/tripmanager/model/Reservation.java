package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reservation extends Image {
    @SerializedName("reservation_id")
    @Expose
    private Integer id;

    @SerializedName("reservation_name")
    @Expose
    private String name;

    @SerializedName("reservation_image")
    @Expose
    private String imageLink;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageLink() {
        return imageLink;
    }
}
