package team.project.tripmanager.core;

import android.content.Context;

import team.project.tripmanager.api.APIClient;
import team.project.tripmanager.api.APIService;
import team.project.tripmanager.module.prefs.MainPrefs;

public class TMEnvironment {
    private Context context;
    private APIService apiService;

    public TMEnvironment(Context context) {
        this.context = context;
    }

    public MainPrefs getPrefs() {
        return MainPrefs.getInstance(context);
    }

    public APIService getAPIService() {
        if (apiService == null) {
            return APIClient.getInstance(context).getClient().create(APIService.class);
        }
        return apiService;
    }
}
