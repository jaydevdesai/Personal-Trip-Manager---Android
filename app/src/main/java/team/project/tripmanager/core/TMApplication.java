package team.project.tripmanager.core;

import android.app.Application;

public class TMApplication extends Application {
    private TMEnvironment enviroment;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public TMEnvironment getEnvironment() {
        if (enviroment == null) {
            enviroment = new TMEnvironment(this);
        }
        return enviroment;
    }
}
