package team.project.tripmanager.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("access_token")
    @Expose
    private String accessToken;

    public boolean isSuccess() {
        return accessToken != null;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }
}