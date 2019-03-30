package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document extends Image {
    @SerializedName("document_id")
    @Expose
    private Integer id;

    @SerializedName("document_name")
    @Expose
    private String name;

    @SerializedName("document_image")
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
