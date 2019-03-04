package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("access_token")
    @Expose
    public String accessToken;

    public boolean isSuccess() {
        return accessToken != null;
    }
}