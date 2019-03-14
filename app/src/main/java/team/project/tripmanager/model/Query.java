package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Query implements Serializable {

    @SerializedName("query_id")
    @Expose
    private Integer queryId;
    @SerializedName("query_text")
    @Expose
    private String queryText;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("creation_time")
    @Expose
    private String creationTime;

    public Integer getQueryId() {
        return queryId;
    }

    public String getQueryText() {
        return queryText;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getCreationTime() {
        return creationTime;
    }
}
