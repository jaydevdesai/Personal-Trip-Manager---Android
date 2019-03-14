package team.project.tripmanager.module.prefs;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

import team.project.tripmanager.logger.Logger;
import team.project.tripmanager.model.AuthResponse;


public class MainPrefs {
    @NonNull
    private final Gson gson = new GsonBuilder().create();

    private Logger logger = new Logger(getClass());

    @NonNull
    private final PrefManager pref;

    private static MainPrefs instance;

    public static MainPrefs getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new MainPrefs(context);
        }
        return instance;
    }

    private MainPrefs(@NonNull Context context) {
        pref = new PrefManager(context, PrefManager.Pref.MAIN);
    }

    public void storeAuthTokenResponse(@NonNull AuthResponse response) {
        pref.put(PrefManager.Key.AUTH_JSON, gson.toJson(response));
    }

    @Nullable
    public String getAuthorizationHeader() {
        final AuthResponse auth = getCurrentAuth();
        if (auth == null || !auth.isSuccess()) {
            return null;
        } else {
            return auth.getAccessToken();
        }
    }

    @Nullable
    public String getEmail() {
        final AuthResponse auth = getCurrentAuth();
        if (auth == null || !auth.isSuccess()) {
            return null;
        } else {
            return auth.getEmail();
        }
    }

    @Nullable
    public AuthResponse getCurrentAuth() {
        final String json = pref.getString(PrefManager.Key.AUTH_JSON);
        if (json == null) {
            return null;
        }
        return gson.fromJson(json, AuthResponse.class);
    }
}

