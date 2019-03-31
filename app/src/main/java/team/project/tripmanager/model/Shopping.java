package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shopping {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("bought")
    @Expose
    private Boolean bought;

    public Integer getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public Boolean getBought() {
        return bought;
    }
}
